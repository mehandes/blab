package org.blab.river.vcas;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeoutException;
import java.util.stream.Collectors;

import org.blab.river.*;
import org.blab.utils.RingBuffer;
import org.blab.utils.net.BlockingSocketChannel;

public class VcasConsumer implements RiverConsumer {
  /** Indicates current consumer status. */
  private final Status status;

  /** Stores current set of subscriptions (processed and not). */
  private final Set<String> subscriptions;

  /** The buffer containing unprocessed events since last poll. */
  private final RingBuffer<Event> buffer;

  /** Indicated, whether new subscriptions added or not. */
  private boolean modified;

  /** Thread in which socket background task evaluating. */
  private Thread socketThread;

  public VcasConsumer(Properties properties) {
    status = new Status();
    subscriptions = new HashSet<>();
    buffer = new RingBuffer<>(1024);

    try {
      Map<String, Object> props = validateProperties(properties);
      socketThread = new SocketReader(props);
      socketThread.start();
    } catch (ConfigurationException e) {
      status.setCorrupted(e);
    }
  }

  public Map<String, Object> validateProperties(Properties properties)
      throws ConfigurationException {
    Map<String, String> violations = new HashMap<>();
    Map<String, Object> validated = new HashMap<>();

    if (!properties.containsKey(RiverConsumer.PROPERTIES_HOST))
      violations.put(RiverConsumer.PROPERTIES_HOST, "Not found.");
    else
      validated.put(RiverConsumer.PROPERTIES_HOST, properties.get(RiverConsumer.PROPERTIES_HOST));

    if (!properties.containsKey(RiverConsumer.PROPERTIES_PORT))
      violations.put(RiverConsumer.PROPERTIES_PORT, "Not found.");
    else
      try {
        validated.put(
            RiverConsumer.PROPERTIES_PORT,
            properties.get(RiverConsumer.PROPERTIES_PORT));
      } catch (Exception e) {
        violations.put(RiverConsumer.PROPERTIES_PORT, "Incorrect.");
      }

    if (violations.isEmpty()) return validated;
    else throw new ConfigurationException(violations);
  }

  @Override
  public List<Event> poll(long timeout) {
    if (status.getCode().equals(Status.Code.CLOSED))
      throw new IllegalStateException("Consumer was stopped.");
    if (status.getCode().equals(Status.Code.CORRUPTED)) throw status.getError();
    if (subscriptions.isEmpty()) throw new IllegalStateException("There are no subscriptions yet.");

    try {
      return buffer.pollAtLeast(1, timeout);
    } catch (InterruptedException | TimeoutException e) {
      return Collections.emptyList();
    }
  }

  @Override
  public void subscribe(Set<String> lades) {
    if (status.getCode().equals(Status.Code.CLOSED))
      throw new IllegalStateException("Consumer was stopped.");
    if (status.getCode().equals(Status.Code.CORRUPTED)) throw status.getError();
    modified = subscriptions.addAll(lades);
  }

  @Override
  public void subscribe(String pattern) {
    if (status.getCode().equals(Status.Code.CLOSED))
      throw new IllegalStateException("Consumer was stopped.");
    if (status.getCode().equals(Status.Code.CORRUPTED)) throw status.getError();

    // TODO Create task for topic resolution and pattern matching
  }

  @Override
  public void unsubscribe() {
    if (status.getCode().equals(Status.Code.CLOSED))
      throw new IllegalStateException("Consumer was stopped.");
    if (status.getCode().equals(Status.Code.CORRUPTED)) throw status.getError();

    subscriptions.clear();
    modified = true;
  }

  @Override
  public void close() {
    if (status.getCode().equals(Status.Code.CORRUPTED)) throw status.getError();
    socketThread.interrupt();
  }

  /** Background task which communicates with VCAS. */
  class SocketReader extends Thread {
    private final BlockingSocketChannel bsc;
    private final InetSocketAddress address;

    private final State connect = new Connect();
    private final State subscribe = new Subscribe();
    private final State read = new Read();

    SocketReader(Map<String, Object> properties) {
      bsc = new BlockingSocketChannel(1024);
      address =
          new InetSocketAddress(
              (String) properties.get(RiverConsumer.PROPERTIES_HOST),
              (Integer) properties.get(RiverConsumer.PROPERTIES_PORT));
    }

    @Override
    public void run() {
      try {
        for (State s = connect; true; s = s.call()) {}
      } catch (Exception e) {
        status.setCorrupted(new RiverException(e));
      }
    }

    @Override
    public void interrupt() {
      super.interrupt();
    }

    interface State extends Callable<State> {}

    class Connect implements State {
      private int attempts = 0;

      @Override
      public State call() throws Exception {
        System.out.println("Connecting...");
        try {
          bsc.open(address);
          return subscribe;
        } catch (Exception e) {
          if (attempts < 3) {
            attempts++;
            return this;
          } else throw e;
        }
      }
    }

    class Subscribe implements State {
      private final Set<String> sub = new HashSet<>();

      @Override
      public State call() {
        if (modified)
          try {
            for (String s : subscriptions.stream().filter(l -> !sub.contains(l)).toList())
              subscribe(s);

            sub.addAll(subscriptions);
          } catch (IOException e) {
            return connect;
          }

        return read;
      }

      private void subscribe(String lade) throws IOException {
        System.out.println("Subscribing: " + lade);
        bsc.write(String.format("name:%s|method:subscr", lade).getBytes(StandardCharsets.US_ASCII));
      }

      private Map<String, String> parseMessage(byte[] message) {
        return Arrays.stream(new String(message, StandardCharsets.US_ASCII).split("\\|"))
            .map(e -> e.split(":"))
            .collect(Collectors.toMap(e -> e[0], e -> e[1]));
      }
    }

    class Read implements State {
      @Override
      public State call() {
        try {
          bsc.read().stream().map(this::parseMessage).forEach(buffer::put);
          return subscribe;
        } catch (Exception e) {
          return connect;
        }
      }

      public Event parseMessage(byte[] message) {
        Map<String, String> fields =
            Arrays.stream(new String(message, StandardCharsets.US_ASCII).split("\\|"))
                .map(s -> s.split(":"))
                .collect(Collectors.toMap(s -> s[0], s -> s[1]));

        return new Event(
            fields.get("name"),
            String.join(
                    "|",
                    fields.entrySet().stream()
                        .filter(e -> !e.getKey().equals("name"))
                        .map(f -> String.format("%s:%s", f.getKey(), f.getValue()))
                        .toList())
                .getBytes(StandardCharsets.US_ASCII));
      }
    }
  }
}
