package org.blab.blender.registry;

import java.util.Set;

public class ChannelInUseException extends RegistryException {
  private final Set<Service> users;

  public ChannelInUseException(Set<Service> users) {
    super();
    this.users = users;
  }

  public ChannelInUseException(Set<Service> users, String message) {
    super(message);
    this.users = users;
  }

  public ChannelInUseException(Set<Service> users, String message, Throwable cause) {
    super(message, cause);
    this.users = users;
  }

  public ChannelInUseException(Set<Service> users, Throwable cause) {
    super(cause);
    this.users = users;
  }
}
