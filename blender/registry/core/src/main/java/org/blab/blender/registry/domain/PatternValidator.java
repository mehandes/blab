package org.blab.blender.registry.domain;

@FunctionalInterface
public interface PatternValidator {
  /**
   * Validate given pattern.
   * 
   * @param pattern - pattern to validate
   * @return {@code true} if the pattern is valid.
   * @throws NullPointerException if the pattern is {@code null}.
   * @throws ValidationException if the pattern is invalid.
   */
  boolean validate(String pattern) throws ValidationException;
}
