package org.blab.blender.registry.domain;

import java.util.Map;

public class ValidationException extends RuntimeException {
  private Map<String, String> violations;

  public ValidationException(String message) {
    super(message);
  }

  public ValidationException(Map<String, String> violations, String message) {
    super(message);
    this.violations = violations;
  }

  public Map<String, String> getViolations() {
    return violations;
  }
}
