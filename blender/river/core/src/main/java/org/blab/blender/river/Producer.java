package org.blab.blender.river;

/**
 * Interface for publishing messages to River.
 */
public interface Producer {
    public static final String PROPERTIES_BASE = "river.producer";

    /**
     * Create
     * 
     * @param event
     * @param callback
     */
    void send(Event event, Callback callback);

    /** Close producer. Blocks unit all buffered requests complete. */
    void close();
}
