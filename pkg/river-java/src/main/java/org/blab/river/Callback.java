package org.blab.river;

/** The callback to provide asynchronous handling of River's task execution results. */
@FunctionalInterface
public interface Callback<T> {
  /**
   * The callback method to provide asynchronous handling of request completion.
   *
   * @param response - payload returned by successful request execution
   * @param exception - exception that occurs during request execution
   */
  void onCompletion(T response, Exception exception);
}
