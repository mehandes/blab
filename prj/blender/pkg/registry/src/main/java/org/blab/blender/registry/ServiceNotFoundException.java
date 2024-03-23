package org.blab.blender.registry;

public class ServiceNotFoundException extends RegistryException {
  public ServiceNotFoundException() {
    super();
  }

  public ServiceNotFoundException(String message) {
    super(message);
  }

  public ServiceNotFoundException(String message, Throwable cause) {
    super(message, cause);
  }

  public ServiceNotFoundException(Throwable cause) {
    super(cause);
  }
}
