package org.blab.blender.registry;

public class SchemeDuplicateException extends RegistryException {
  public SchemeDuplicateException() {
    super();
  }

  public SchemeDuplicateException(String message) {
    super(message);
  }

  public SchemeDuplicateException(String message, Throwable cause) {
    super(message, cause);
  }

  public SchemeDuplicateException(Throwable cause) {
    super(cause);
  }
}
