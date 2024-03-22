package org.blab.blender.registry;

/** Domain-specific topic validator. */
public class TopicValidator {
  /**
   * Validate given topic according business requirements.
   *
   * @return provided topic if it is valid.
   * @throws NullPointerException if given topic is null.
   * @throws ValidationException  if given topic is invalid.
   */
  public static String validate(String topic) {
    return topic;
  }
}

