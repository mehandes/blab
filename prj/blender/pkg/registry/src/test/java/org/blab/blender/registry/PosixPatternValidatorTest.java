package org.blab.blender.registry;

import org.blab.blender.registry.domain.PatternValidator;
import org.blab.blender.registry.domain.PosixBasicPatternValidator;
import org.blab.blender.registry.domain.ValidationException;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;

public class PosixPatternValidatorTest {
  private static final String ALPHA = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
  private static final String DIGIT = "0123456789";
  private static final String VALID_SET = "()[]{}$*,-.\\^|:?" + DIGIT + ALPHA + ALPHA.toLowerCase();

  private PatternValidator validator;

  @Before
  public void before() {
    validator = new PosixBasicPatternValidator();
  }

  @Test
  public void validCharacterSetTest() {
    Assertions.assertDoesNotThrow(() -> validator.validate(VALID_SET));
  }

  @Test
  public void invalidCharacterSetTest() {
    for (int i = 0; i < 128; i++) {
      String s = new StringBuilder(1).append((char) i).toString();

      if (!VALID_SET.contains(s))
        Assertions.assertThrows(ValidationException.class, () -> validator.validate(s));
    }
  }

  @Test
  public void emptryPatternTest() {
    Assertions.assertThrows(ValidationException.class, () -> validator.validate(""));
  }
}
