package org.blab.river;

/** River's unit of work. */
public record Event(String lade, byte[] message) {}
