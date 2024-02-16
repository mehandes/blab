package org.blab.blender.registry.domain;

@FunctionalInterface
public interface SchemaValidator {
  /**
   * Validate given schema.
   * 
   * @param schema - schema to validate
   * @return {@code true} if the schema is valid.
   * @throws NullPointerException if the schema is {@code null}.
   * @throws ValidationException if the schema is invalid.
   */
  boolean validate(String schema) throws ValidationException;
}
