package org.blab.lambda.demo;

import org.blab.river.Event;
import org.blab.river.RiverConsumer;
import org.blab.river.RiverException;
import org.blab.river.vcas.VcasConsumer;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.function.Consumer;

public class Worker implements Consumer<Configuration> {
  private final Lambda<Event, List<Event>> lambda;
  private RiverConsumer consumer;
  private Configuration configuration;
  private RiverException error;

  public Worker(Lambda<Event, List<Event>> lambda) {
    this.lambda = lambda;
  }

  public void start() {
    while (error == null) {
      if (consumer == null) return;

      try {
        consumer.poll(1000).forEach(e -> System.out.println(lambda.apply(e)));
      } catch (Exception e) {
        throw new RuntimeException(e);
      }
    }
  }

  @Override
  public void accept(Configuration configuration) {
    if (configuration == null || isChanged(configuration)) {
      this.configuration = configuration;

      Properties properties = new Properties();
      properties.put("hostname", configuration.hostname());
      properties.put("port", configuration.port());

      try {
        consumer = new VcasConsumer(properties);
      } catch (RiverException e) {
        error = e;
      }
    }

    lambda.refresh(configuration);
  }

  public boolean isChanged(Configuration configuration) {
    return !this.configuration.hostname().equals(configuration.hostname())
        || this.configuration.port() != configuration.port();
  }
}
