package org.blab.blender.river;

import java.time.Duration;
import java.util.List;
import java.util.Set;

/** Interface for consuming messages from River. */
public interface Consumer {
  public static final String PROPERTIES_BASE = "river.consumer";

  /**
   * Fetch data for the lades specified using {@link #subscribe(Set)} or {@link #subscribe(String)}
   * methods.
   * 
   * <p>
   * This method returns immediately if there are records available. Otherwise, it will await the
   * passed timeout. If the timeout expires, an empty record set will be returned.
   * </p>
   * 
   * @param timeout - maximum time to block (0 <= t <= Long.MAX_VALUE)
   * @return List of events since last fetch.
   * @throws RiverException if there are unrecoverable errors (e.g. configuration or connection
   *         exceptions) occurs.
   * @throws IllegalStateException if the consumer was closed before calling this method or if the
   *         consumer is not subscribed to any lade.
   */
  List<Event> poll(Duration timeout);

  /**
   * Subscribe to all lades from the presented set.
   * 
   * <p>
   * Skips lades that are already being monitored.
   * </p>
   * 
   * <p>
   * Throws an exception if invalid lades or pattern provided.
   * </p>
   * 
   * @param lades - lades to subscribe
   * @throws RiverException if there are unrecoverable errors (e.g. configuration or connection
   *         exceptions).
   * @throws IllegalStateException if the consumer was closed before calling this method.
   * @throws IllegalArgumentException if invalid lades or pattern provided.
   */
  void subscribe(Set<String> lades);

  /**
   * Subscribe to all lades matching the specified pattern.
   * 
   * <p>
   * Skips lades that are already being monitored.
   * </p>
   * 
   * <p>
   * Throws an exception if invalid pattern provided.
   * </p>
   * 
   * @param pattern - pattern to subscribe
   * @throws RiverException if there are unrecoverable errors (e.g. configuration or connection
   *         exceptions).
   * @throws IllegalStateException if the consumer was closed before calling this method.
   * @throws IllegalArgumentException if invalid pattern provided.
   */
  void subscribe(String pattern);

  /**
   * Unsubscribe from all lades.
   * 
   * @throws RiverException if there are unrecoverable errors (e.g. configuration or connection
   *         exceptions).
   * @throws IllegalStateException if the consumer was closed before calling this method.
   */
  void unsubscribe();

  /** Close all connections with River. */
  void close();
}
