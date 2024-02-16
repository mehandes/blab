package org.blab.blender.registry.domain;

import java.util.List;

public class PatternIntersectionException extends Exception {
  private List<String> patterns;

  public PatternIntersectionException(List<String> patterns) {
    super();
    this.patterns = patterns;
  }

  public PatternIntersectionException(List<String> patterns, String message) {
    super(message);
    this.patterns = patterns;
  }

  public List<String> getPatterns() {
    return patterns;
  }
}
