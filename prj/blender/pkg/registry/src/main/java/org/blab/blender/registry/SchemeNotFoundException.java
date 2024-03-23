package org.blab.blender.registry;

public class SchemeNotFoundException extends RegistryException {
  public SchemeNotFoundException() {
    super();
  }

  public SchemeNotFoundException(String message) {
    super(message);
  }

  public SchemeNotFoundException(String message, Throwable cause) {
    super(message, cause);
  }

  public SchemeNotFoundException(Throwable cause) {
    super(cause);
  }
}
