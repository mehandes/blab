package org.blab.blender.registry.domain;

@FunctionalInterface
public interface SchemaValidator {
  /**
   * Validate given schema.
   * 
   * @param schema - schema to validate
   * @return {@code true} if the scheme is valid.
   * @throws NullPointerException if the scheme is {@code null}.
   * @throws ValidationException if the scheme is invalid.
   */
  boolean validate(Schema schema) throws ValidationException;
}
