package org.blab.river.mqtt;

import org.blab.river.Callback;
import org.blab.river.Event;
import org.blab.river.RiverProducer;

import java.util.Properties;

public class MqttProducer implements RiverProducer {
  public MqttProducer(Properties properties) {

  }

  @Override
  public void send(Event event, Callback<Event> callback) {

  }

  @Override
  public void close() {

  }
}
