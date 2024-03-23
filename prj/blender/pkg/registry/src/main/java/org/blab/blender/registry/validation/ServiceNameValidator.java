package org.blab.blender.registry.validation;

public class ServiceNameValidator {
  /**
   * Validate given name according business requirements.
   *
   * @return provided name if it is valid.
   * @throws NullPointerException if given name is null.
   * @throws ValidationException if given name is invalid.
   */
  public static String validate(String name) {
    if (name == null) throw new NullPointerException("Name must be non-null.");
    if (name.isBlank()) throw new ValidationException("Name must be non-blank.");
    return name;
  }
}
