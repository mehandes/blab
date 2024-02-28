package org.blab.river;

import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/** Interface for consuming messages from River. */
public interface RiverConsumer {
  String PROPERTIES_BASE = "river.consumer";
  String PROPERTIES_HOST = "river.consumer.host";
  String PROPERTIES_PORT = "river.consumer.port";

  /**
   * Fetch data for the lades specified using {@link #subscribe(Set)} or {@link #subscribe(String)}
   * methods.
   *
   * <p>This method returns immediately if there are records available. Otherwise, it will await the
   * passed timeout. If the timeout expires, an empty record set will be returned.
   *
   * @param timeout maximum time in milliseconds to block. Negative timeout treated as infinity.
   * @return List of events since last fetch.
   * @throws RiverException if there are unrecoverable errors (e.g. configuration or connection
   *     exceptions) occurs.
   * @throws IllegalStateException if the consumer was closed before calling this method or if the
   *     consumer is not subscribed to any lade.
   * @throws InterruptedException if the thread interrupted during timeout.
   */
  List<Event> poll(long timeout) throws InterruptedException;

  /**
   * Subscribe to all lades from the presented set.
   *
   * <p>Skips lades that are already being monitored.
   *
   * <p>Throws an exception if invalid lades or pattern provided.
   *
   * @param lades lades to subscribe
   * @throws RiverException if there are unrecoverable errors (e.g. configuration or connection
   *     exceptions).
   * @throws IllegalStateException if the consumer was closed before calling this method.
   * @throws IllegalArgumentException if invalid lades provided.
   */
  void subscribe(Set<String> lades);

  /**
   * Subscribe to all lades matching the specified pattern.
   *
   * <p>Skips lades that are already being monitored.
   *
   * <p>Throws an exception if invalid pattern provided.
   *
   * @param pattern pattern to subscribe
   * @throws RiverException if there are unrecoverable errors (e.g. configuration or connection
   *     exceptions).
   * @throws IllegalStateException if the consumer was closed before calling this method.
   * @throws IllegalArgumentException if invalid pattern provided.
   */
  void subscribe(String pattern);

  /**
   * Unsubscribe from all lades.
   *
   * @throws RiverException if there are unrecoverable errors (e.g. configuration or connection
   *     exceptions).
   * @throws IllegalStateException if the consumer was closed before calling this method.
   */
  void unsubscribe();

  /**
   * Close all connections with River.
   *
   * @throws RiverException if there are unrecoverable errors (e.g. configuration or connection *
   *     exceptions).
   */
  void close();
}
