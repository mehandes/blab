package org.blab.blender.river;

public class AuthenticationException extends ConnectionException {
  public AuthenticationException() {
    super();
  }

  public AuthenticationException(String message) {
    super(message);
  }
}
