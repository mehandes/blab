package org.blab.blender.river;

/**
 * The callback to provide asynchronous handling of River's task execution results.
 */
@FunctionalInterface
public interface Callback<T> {
  /**
   * The callback method to provide asynchronous handling of request completion.
   * 
   * @param response - payload returned by successfull request execution
   * @param exception - exception that occures during request execution
   */
  void onCompletion(T response, Exception exception);
}
