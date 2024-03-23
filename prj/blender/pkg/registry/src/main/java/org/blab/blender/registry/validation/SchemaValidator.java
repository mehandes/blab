package org.blab.blender.registry.validation;

import org.apache.avro.Schema;
import org.apache.avro.Schema.Parser;

/** Domain-specific schema validator. */
public class SchemaValidator {
  /**
   * Validate given schema according to business requirements.
   *
   * @return parsed Schema if it's valid.
   * @throws ValidationException  if schema is invalid.
   * @throws NullPointerException if schema is null.
   */
  public static Schema validate(String schema) {
    if (schema == null)
      throw new NullPointerException();

    try {
      Schema parsed = new Parser().parse(schema);
      Schema.Type type = parsed.getType();

      if (type.equals(Schema.Type.ENUM) ||
          type.equals(Schema.Type.FIXED) ||
          type.equals(Schema.Type.RECORD))
        return parsed;
      else
        throw new Exception("Unsupported top level type.");
    } catch (Exception e) {
      throw new ValidationException(e.getMessage());
    }
  }
}
