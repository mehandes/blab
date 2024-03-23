package org.blab.blender.registry;

public class ChannelNotFoundException extends RegistryException {
  public ChannelNotFoundException() {
    super();
  }

  public ChannelNotFoundException(String message) {
    super(message);
  }

  public ChannelNotFoundException(String message, Throwable cause) {
    super(message, cause);
  }

  public ChannelNotFoundException(Throwable cause) {
    super(cause);
  }
}
