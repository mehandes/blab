package org.blab.blender.registry.domain;

public interface PatternValidator {
  /**
   * Validate the given pattern against domain-specific restrictions or specific regular expression
   * syntax (if supported).
   * 
   * @param pattern - pattern to validate
   * @throws NullPointerException if the pattern is {@code null}.
   * @throws ValidationException if the pattern is invalid.
   */
  void validate(String pattern) throws ValidationException;
}
