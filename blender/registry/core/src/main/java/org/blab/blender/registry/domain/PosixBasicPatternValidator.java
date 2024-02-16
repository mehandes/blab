package org.blab.blender.registry.domain;

/** Pattern validator for 'Posix Basic' regular excepression character set. */
public class PosixBasicPatternValidator implements PatternValidator {
  @Override
  public void validate(String pattern) throws ValidationException {
    if (pattern == null) throw new NullPointerException("The pattern must be non-null.");
    if (pattern.isBlank()) throw new ValidationException("Pattern cannot be empty.");

    // 36, 40-42, 44-46, 48-58, 63, 65-94, 97-125 - valid
    for (char c : pattern.toCharArray()) {
      int i = (int) c;

      if (i < '$'
          || ('$' < i && i < '(')
          || i == '+'
          || i == '/'
          || (':' < i && i < '?')
          || ('?' < i && i < 'A')
          || ('^' < i && i < 'a')
          || '}' < i)
        throw new ValidationException(String.format("Pattern contains invalid character: '%c'", c));
    }
  }
}
