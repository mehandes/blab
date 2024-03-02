package org.blab.lambda.demo;

import org.blab.river.Event;
import org.blab.river.RiverConsumer;
import org.blab.river.RiverException;
import org.blab.river.RiverProducer;
import org.blab.river.mqtt.MqttProducer;
import org.blab.river.vcas.VcasConsumer;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class Worker implements Consumer<Configuration> {
  private final Lambda<Event, List<Event>> lambda;
  private RiverConsumer consumer;
  private RiverProducer producer;
  private Configuration configuration;
  private RiverException error;

  private final ReentrantLock consumerLock;

  public Worker(Lambda<Event, List<Event>> lambda) {
    System.out.println("Worker created.");
    this.lambda = lambda;
    this.consumerLock = new ReentrantLock();
  }

  public void start() {
    System.out.println("Worker started.");

    while (error == null) {
      consumerLock.lock();

      try {
        if (consumer == null) continue;

        List<Event> events = consumer.poll(-1);
        events.forEach(event -> lambda.apply(event).forEach(r -> producer.send(r, null)));
      } catch (Exception e) {
        throw new RuntimeException(e);
      } finally {
        consumerLock.unlock();
      }
    }
  }

  @Override
  public void accept(Configuration configuration) {
    if (isChanged(configuration)) {
      this.configuration = configuration;
      consumerLock.lock();

      Properties properties = new Properties();
      properties.put("hostname", configuration.hostname());
      properties.put("port", configuration.port());

      if (configuration.frames().isEmpty()) throw new RuntimeException("Empty frames.");

      try {
        Properties producerProperties = new Properties();
        producerProperties.put("hostname", "172.16.1.201");
        producerProperties.put("port", 1883);

        producer = new MqttProducer(producerProperties);
        consumer = new VcasConsumer(properties);
        consumer.subscribe(
            this.configuration.frames().stream()
                .map(Configuration.Frame::lade)
                .collect(Collectors.toSet()));
      } catch (RiverException e) {
        error = e;
      } finally {
        consumerLock.unlock();
      }
    }

    lambda.refresh(configuration);
  }

  public boolean isChanged(Configuration configuration) {
    return this.configuration == null
        || !this.configuration.hostname().equals(configuration.hostname())
        || this.configuration.port() != configuration.port();
  }
}
