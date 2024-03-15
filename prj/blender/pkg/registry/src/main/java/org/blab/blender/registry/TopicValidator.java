package org.blab.blender.registry;

public interface TopicValidator {
  /**
   * Validate given topic according business requirements.
   *
   * @return {@code true} if given topic is valid.
   * @throws NullPointerException if given topic is null.
   * @throws ValidationException  if given topic is invalid.
   */
  boolean validate(String topic);
}
