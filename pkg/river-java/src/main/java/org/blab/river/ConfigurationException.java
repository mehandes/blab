package org.blab.blender.river;

public class ConfigurationException extends RiverException {
  private String name;
  private Object value;

  public ConfigurationException() {
    super();
  }

  public ConfigurationException(String message) {
    super(message);
  }

  public ConfigurationException(String name, Object value, String message) {
    super(message);
    this.name = name;
    this.value = value;
  }

  public String getName() {
    return name;
  }

  public Object getValue() {
    return value;
  }
}
