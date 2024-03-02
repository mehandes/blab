package org.blab.river.mqtt;

import org.blab.river.*;
import org.blab.utils.RingBuffer;
import org.eclipse.paho.mqttv5.client.IMqttClient;
import org.eclipse.paho.mqttv5.client.MqttClient;
import org.eclipse.paho.mqttv5.client.MqttConnectionOptionsBuilder;
import org.eclipse.paho.mqttv5.client.persist.MemoryPersistence;
import org.eclipse.paho.mqttv5.common.MqttException;
import org.eclipse.paho.mqttv5.common.MqttMessage;

import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Properties;
import java.util.UUID;
import java.util.concurrent.TimeoutException;

public class MqttProducer implements RiverProducer {
  private RingBuffer<Task> tasks;
  private IMqttClient client;
  private RiverException error;

  private boolean isClosed;

  public MqttProducer(Properties properties) {
    this.tasks = new RingBuffer<>(64);

    try {
      String url = String.format("tcp://%s:%d", properties.get("hostname"), (Integer) properties.get("port"));
      String id = UUID.randomUUID().toString();
      String username = (String) properties.getOrDefault("username", "");
      String password = (String) properties.getOrDefault("password", "");

      this.client = new MqttClient(url, id, new MemoryPersistence());
      this.client.connect(new MqttConnectionOptionsBuilder()
              .automaticReconnect(true)
              .username(username)
              .password(password.getBytes(StandardCharsets.US_ASCII))
              .build());

      new Thread(new MessageSender()).start();
    } catch (MqttException e) {
      error = new RiverException(e);
    } catch (ClassCastException e) {
      throw new ConfigurationException(Map.of("port", "Invalid."));
    }

  }

  @Override
  public void send(Event event, Callback<Event> callback) {
    if (error != null) throw error;
    if (isClosed) throw new IllegalStateException("Producer was closed.");

    tasks.put(new Task(event, callback));
  }

  @Override
  public void close() {
    while (tasks.peek().isPresent() && error == null)
      try {
        Thread.sleep(1000);
      } catch (InterruptedException e) {
        throw new RiverException(e);
      }

    if (error != null) throw error;

    if (client.isConnected()) {
      try {
        client.disconnect();
        isClosed = true;
      } catch (MqttException e) {
        throw new RiverException(e);
      }
    }
  }

  record Task(Event event, Callback<Event> callback) {}

  class MessageSender implements Runnable {
    @Override
    public void run() {
      while (error == null) {
        Task task = null;

        try {
          task = tasks.poll(-1);
          client.publish(task.event().lade(), new MqttMessage(task.event().message()));

          if (task.callback() != null)
            task.callback().onCompletion(task.event(), null);
        } catch (InterruptedException e) {
          error = new RiverException(e);
        } catch (TimeoutException e) {
          continue;
        } catch (MqttException e) {
          if (task.callback() != null)
            task.callback().onCompletion(null, e);

          error = new RiverException(e);
        }
      }
    }
  }
}
