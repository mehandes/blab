package org.blab.river;

/** Base class for all unrecoverable River exceptions. */
public class RiverException extends RuntimeException {
  public RiverException() {
    super();
  }

  public RiverException(String message) {
    super(message);
  }
}
