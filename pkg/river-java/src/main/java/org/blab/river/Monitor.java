package org.blab.blender.river;

import java.util.Set;

/** Interface for monitoring River's state. */
public interface Monitor {
  public static final String PROPERTIES_BASE = "river.monitor";

  /**
   * Get the current set of all active lades. Should be called periodically to find out latest
   * information.
   * 
   * @return Set of current active lades.
   * @throws RiverException if there are unrecoverable errors (e.g. configuration or connection
   *         exceptions) occurs.
   * @throws IllegalStateException if the monitor was closed before calling this method.
   */
  Set<String> getActiveLades();

  /** Close all connections with River. */
  void close();
}
