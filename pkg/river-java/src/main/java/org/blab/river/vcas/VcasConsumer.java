package org.blab.river.vcas;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import org.blab.river.ConfigurationException;
import org.blab.river.Event;
import org.blab.river.RiverConsumer;
import org.blab.river.Status;
import org.blab.utils.Buffer;

public class VcasConsumer implements RiverConsumer {
  /** Indicates current consumer status. */
  private final Status status;

  /** Stores current set of subscriptions (processed and not). */
  private final Set<String> subscribtions;

  /** The buffer containing unprocessed events since last poll. */
  private final Buffer<Event> buffer;

  /** Indicated, whether new subscriptions added or not. */
  private boolean modified;

  public VcasConsumer(Properties properties) {
    status = new Status();
    subscribtions = new HashSet<>();
    buffer = new Buffer<>();

    try {
      Map<String, Object> props = validateProperties(properties);

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
            Integer.parseUnsignedInt(properties.getProperty(RiverConsumer.PROPERTIES_PORT)));
      } catch (Exception e) {
        violations.put(RiverConsumer.PROPERTIES_PORT, "Incorrect.");
      }

    if (violations.isEmpty()) return validated;
    else throw new ConfigurationException(violations);
  }

  @Override
  public List<Event> poll(long timeout, TimeUnit unit) {
    return null;
  }

  @Override
  public void subscribe(Set<String> lades) {}

  @Override
  public void subscribe(String pattern) {}

  @Override
  public void unsubscribe() {}

  @Override
  public void close() {}
}
