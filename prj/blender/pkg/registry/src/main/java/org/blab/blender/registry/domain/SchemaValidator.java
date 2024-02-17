package org.blab.blender.registry.domain;

@FunctionalInterface
public interface SchemaValidator {
  /**
   * Validate given schema. Throws exception if schema is invalid.
   *
   * @param schema - schema to validate
   * @return {@code true} if the schema is valid.
   * @throws NullPointerException if the schema is {@code null}.
   * @throws ValidationException if the schema is invalid.
   */
  void validate(String schema) throws ValidationException;
}
