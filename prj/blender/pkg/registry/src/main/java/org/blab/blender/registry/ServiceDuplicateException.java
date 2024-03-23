package org.blab.blender.registry;

public class ServiceDuplicateException extends RegistryException {
  public ServiceDuplicateException() {
    super();
  }

  public ServiceDuplicateException(String message) {
    super(message);
  }

  public ServiceDuplicateException(String message, Throwable cause) {
    super(message, cause);
  }

  public ServiceDuplicateException(Throwable cause) {
    super(cause);
  }
}
