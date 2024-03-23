package org.blab.blender.registry;

public class ChannelDuplicateException extends RegistryException {
  public ChannelDuplicateException() {
    super();
  }

  public ChannelDuplicateException(String message) {
    super(message);
  }

  public ChannelDuplicateException(String message, Throwable cause) {
    super(message, cause);
  }

  public ChannelDuplicateException(Throwable cause) {
    super(cause);
  }
}
