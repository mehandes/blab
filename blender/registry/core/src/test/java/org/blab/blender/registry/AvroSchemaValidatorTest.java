package org.blab.blender.registry;

import org.apache.avro.SchemaBuilder;
import org.blab.blender.registry.domain.AvroSchemaValidator;
import org.blab.blender.registry.domain.SchemaValidator;
import org.blab.blender.registry.domain.ValidationException;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;

public class AvroSchemaValidatorTest {
  private SchemaValidator validator = new AvroSchemaValidator();

  @Test
  public void validateNullTest() {
    Assertions.assertThrows(NullPointerException.class, () -> validator.validate(null));
  }

  @Test
  public void validateEmptryStringTest() {
    Assertions.assertThrows(ValidationException.class, () -> validator.validate(""));
  }

  @Test
  public void validateBrokenSchemaTest() {
    final String schema = SchemaBuilder.record("name").namespace("space").fields().endRecord()
        .toString().replace("type", "tpye");
    Assertions.assertThrows(ValidationException.class, () -> validator.validate(schema));
  }

  @Test
  public void validateNonRecordSchemaTest() {
    final String schema = SchemaBuilder.builder("space").booleanType().toString();
    Assertions.assertThrows(ValidationException.class, () -> validator.validate(schema));
  }

  @Test
  public void validateRecordWithNonPrimitiveFieldsTest() {
    final String schema = SchemaBuilder.record("name").namespace("space").fields().name("map")
        .type().map().values().stringType().noDefault().endRecord().toString();
    Assertions.assertThrows(ValidationException.class, () -> validator.validate(schema));
  }

  @Test
  public void validateCorrectSchemaTest() {
    final String schema = SchemaBuilder.record("name").namespace("space").fields()
        .requiredBoolean("bool").requiredBytes("bytes").requiredDouble("double")
        .requiredFloat("float").requiredInt("int").requiredLong("long").requiredString("string")
        .endRecord().toString();

    Assertions.assertDoesNotThrow(() -> validator.validate(schema));
  }
}
