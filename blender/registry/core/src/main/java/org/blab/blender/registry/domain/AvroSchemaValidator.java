package org.blab.blender.registry.domain;

import java.util.HashMap;
import java.util.Map;
import org.apache.avro.Schema;
import org.apache.avro.SchemaParseException;
import org.apache.avro.Schema.Type;

/** Schema validator for Avro schemas. */
public class AvroSchemaValidator implements SchemaValidator {
  @Override
  public void validate(String schema) throws ValidationException {
    if (schema == null)
      throw new NullPointerException();

    try {
      var avroSchema = new Schema.Parser().parse(schema);

      if (avroSchema.getType() != Type.RECORD)
        throw new ValidationException("The top level structure must be a record.");

      Map<String, String> errors = new HashMap<>();

      for (var field : avroSchema.getFields())
        if (!checkForPrimitive(field.schema().getType()))
          errors.put(field.name(), "Unsupported type. Fields must be primitive.");

      if (errors.size() != 0)
        throw new ValidationException(errors, "Incorrect fields.");
    } catch (SchemaParseException e) {
      throw new ValidationException("Schema cannot be interpreted as an Avro schema.");
    }
  }

  public boolean checkForPrimitive(Type type) {
    return (type == Type.BOOLEAN) || (type == Type.BYTES) || (type == Type.DOUBLE)
        || (type == Type.FLOAT) || (type == Type.INT) || (type == Type.LONG) || (type == Type.NULL)
        || (type == Type.STRING);
  }
}
