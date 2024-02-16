package org.blab.blender.registry;

import org.blab.blender.registry.domain.PatternValidator;
import org.blab.blender.registry.domain.PosixBasicPatternValidator;
import org.blab.blender.registry.domain.ValidationException;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;

public class PosixPatternValidatorTest {
  private static final String ALPHA = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
  private static final String DIGIT = "0123456789";
  private static final String VALID_SET = "()[]{}$*,-.\\^|:?" + DIGIT + ALPHA + ALPHA.toLowerCase();

  @Test
  public void validCharacterSetTest() {
    PatternValidator validator = new PosixBasicPatternValidator();
    Assertions.assertDoesNotThrow(() -> validator.validate(VALID_SET));
  }

  @Test
  public void invalidCharacterSetTest() {
    PatternValidator validator = new PosixBasicPatternValidator();

    for (int i = 0; i < 128; i++) {
      String s = new StringBuilder(1).append((char) i).toString();

      if (!VALID_SET.contains(s))
        Assertions.assertEquals(String.format("Pattern contains invalid character: '%c'", (char) i),
            Assertions.assertThrows(ValidationException.class, () -> validator.validate(s))
                .getMessage());
    }
  }
}
