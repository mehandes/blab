  package org.blab.river;

/** Interface for publishing messages to River. */
public interface RiverProducer {
  public static final String PROPERTIES_BASE = "river.producer";

  /**
   * Dispatch the event asynchronously and call the provided callback when the operation completes.
   *
   * @param event - event to dispatch
   * @param callback - user-provided callback that is called when the operation completes
   *     (sucessfully or not)
   * @throws IllegalStateException if the producer was closed before calling this method.
   * @throws RiverException if there are unrecoverable errors (e.g. configuration or connection
   *     exceptions) occurs.
   */
  void send(Event event, Callback<Event> callback);

  /**
   * Close all connections with River. Waits for all pending tasks to complete.
   *
   * @throws RiverException if there are unrecoverable errors (e.g. configuration or connection *
   *    *     exceptions).
   * */
  void close();
}
