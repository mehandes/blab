package org.blab.lambda.demo;

import org.blab.river.Event;
import org.blab.river.RiverConsumer;

import java.util.List;
import java.util.Properties;
import java.util.function.Consumer;

public class Worker implements Consumer<Configuration> {
  private Lambda<Event, List<Event>> lambda;
  private RiverConsumer consumer;

  public Worker(Lambda<Event, List<Event>> lambda) {
    this.lambda = lambda;
  }

  public void start() {
    while (true) {
      consumer
          .poll(2000)
          .forEach(
              event -> {
                List<Event> out = lambda.apply(event);
                if (out != null) System.out.println(out);
              });
    }
  }

  @Override
  public void accept(Configuration properties) {
    lambda.refresh(properties);
  }
}
