package org.blab.river.vcas;

import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

import org.blab.river.*;
import org.blab.utils.net.BlockingSocketChannel;
import org.blab.utils.net.BlockingSocketException;
import org.blab.utils.net.BlockingSocketReader;
import org.blab.utils.net.BlockingSocketWriter;

public class VcasConsumer implements RiverConsumer {
  private final BlockingSocketChannel channel;
  private final BlockingSocketWriter writer;
  private final BlockingSocketReader reader;

  private final ReentrantLock readLock;
  private final ReentrantLock writeLock;

  private final Set<String> lades;
  private boolean isClosed;

  public VcasConsumer(Properties properties) {
    channel = new BlockingSocketChannel(1024);
    readLock = new ReentrantLock();
    writeLock = new ReentrantLock();

    lades = new HashSet<>();

    writer = channel.writer();
    reader = channel.reader();

    channel.connect(new InetSocketAddress("localhost", 1234));
    new Thread(writer).start();
    new Thread(reader).start();
  }

  @Override
  public List<Event> poll(long timeout) throws InterruptedException {
    readLock.lock();

    if (isClosed) throw new IllegalStateException("The consumer was closed.");
    if (lades.isEmpty()) throw new IllegalStateException("There are no subscriptions yet.");

    try {
      return reader.poll(timeout).stream().map(this::parseMessage).toList();
    } catch (BlockingSocketException e) {
      throw new RiverException(e);
    } finally {
      readLock.unlock();
    }
  }

  private Event parseMessage(byte[] message) {
    Map<String, String> fields =
        Arrays.stream(new String(message, StandardCharsets.US_ASCII).split("\\|"))
            .map(s -> s.split(":"))
            .collect(Collectors.toMap(s -> s[0], s -> s[1]));

    return new Event(
        fields.get("name"),
        fields.entrySet().stream()
            .filter(e -> !e.getKey().equals("name"))
            .map(s -> String.format("%s:%s", s.getKey(), s.getValue()))
            .collect(Collectors.joining("|"))
            .getBytes(StandardCharsets.US_ASCII));
  }

  @Override
  public void subscribe(Set<String> l) {
    writeLock.lock();

    if (isClosed) throw new IllegalStateException("The consumer was closed.");

    if (lades.addAll(l)) {
      try {
        l.stream()
            .filter(lade -> !lades.contains(lade))
            .forEach(
                lade -> {
                  lades.add(validateLade(lade));
                  requestSubscribe(lade);
                });
      } catch (BlockingSocketException e) {
        throw new RiverException(e);
      } finally {
        writeLock.unlock();
      }
    }
  }

  private String validateLade(String lade) {
    if (lade.isBlank())
      throw new IllegalArgumentException(String.format("Invalid lade provided: %s", lade));

    return lade;
  }

  @Override
  public void subscribe(String pattern) {
    throw new UnsupportedOperationException("Unimplemented.");
  }

  private void requestSubscribe(String lade) {
    writer.write(
        String.format("name:%s|method:subscr\n", lade).getBytes(StandardCharsets.US_ASCII));
  }

  @Override
  public void unsubscribe() {
    writeLock.lock();

    if (isClosed) throw new IllegalStateException("The consumer was closed.");

    try {
      lades.forEach(this::requestUnsubscribe);
    } catch (BlockingSocketException e) {
      throw new RiverException(e);
    } finally {
      writeLock.unlock();
    }
  }

  private void requestUnsubscribe(String lade) {
    writer.write(String.format("name:%s|method:free\n", lade).getBytes(StandardCharsets.US_ASCII));
  }

  @Override
  public void close() {
    isClosed = true;
    if (channel.isConnected()) channel.close();
  }
}
