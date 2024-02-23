package org.blab.river;

import java.util.Map;

public class ConfigurationException extends RiverException {
  private Map<String, String> violations;

  public ConfigurationException() {
    super();
  }

  public ConfigurationException(String message) {
    super(message);
  }

  public ConfigurationException(Map<String, String> violations, String message) {
    super(message);
    this.violations = violations;
  }

  public ConfigurationException(Map<String, String> violations) {
    super();
    this.violations = violations;
  }

  public Map<String, String> getViolations() {
    return violations;
  }
}
