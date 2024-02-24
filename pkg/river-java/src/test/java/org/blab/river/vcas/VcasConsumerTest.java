package org.blab.river.vcas;

import org.blab.river.RiverConsumer;
import java.util.Properties;
import java.util.Set;

public class VcasConsumerTest {
  public static void main(String[] args) {
    Properties properties = new Properties();

    properties.put(RiverConsumer.PROPERTIES_HOST, "172.16.1.110");
    properties.put(RiverConsumer.PROPERTIES_PORT, 20041);

    RiverConsumer consumer = new VcasConsumer(properties);
    consumer.subscribe(Set.of("VEPP/CCD/1M1L/x"));

    while (true) System.out.println(consumer.poll(-1));
  }
}
