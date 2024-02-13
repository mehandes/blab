package org.blab.blender.river;

/**
 * The callback to provide asynchronous handling of River's events.
 */
@FunctionalInterface
public interface Callback {
    /**
     * The callback method to provide asynchronous handling of request completion.
     * 
     * @param event     - event that acts during request execution
     * @param exception - exception that occures during request execution
     */
    void onCompletion(Event event, Exception exception);
}
