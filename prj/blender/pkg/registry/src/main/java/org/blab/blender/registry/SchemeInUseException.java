package org.blab.blender.registry;

import java.util.Set;

public class SchemeInUseException extends RegistryException {
  private final Set<Channel> users;

  public SchemeInUseException(Set<Channel> users) {
    super();
    this.users = users;
  }

  public SchemeInUseException(Set<Channel> users, String message) {
    super(message);
    this.users = users;
  }

  public SchemeInUseException(Set<Channel> users, String message, Throwable cause) {
    super(message, cause);
    this.users = users;
  }

  public SchemeInUseException(Set<Channel> users, Throwable cause) {
    super(cause);
    this.users = users;
  }

  public Set<Channel> getUsers() {
    return users;
  }
}
