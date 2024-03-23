package org.blab.blender.registry;

import org.blab.blender.registry.validation.SchemaValidator;
import org.blab.blender.registry.validation.ValidationException;
import org.junit.jupiter.api.Assertions;
import org.apache.avro.*;
import org.junit.Test;

public class SchemeTest {
  @Test
  public void validateSchemaNull() {
    Assertions.assertThrows(
        NullPointerException.class,
        () -> {
          SchemaValidator.validate(null);
        });
  }

  @Test
  public void validateSchemaMisspelled() {
    Assertions.assertThrows(
        ValidationException.class,
        () -> {
          SchemaValidator.validate("schema");
        });
  }

  @Test
  public void validateSchemaUnsupportedTopType() {
    Schema schema = SchemaBuilder.builder("org.blab")
        .doubleType();

    Assertions.assertThrows(
        ValidationException.class,
        () -> {
          SchemaValidator.validate(schema.toString());
        });
  }

  @Test
  public void validateSchemaTopTypeRecord() {
    Schema schema = SchemaBuilder.builder("org.blab")
        .record("record")
        .fields()
        .endRecord();

    Assertions.assertEquals(
        schema,
            SchemaValidator.validate(schema.toString()));
  }

  @Test
  public void validateSchemaTopTypeEnum() {
    Schema schema = SchemaBuilder.builder("org.blab")
        .enumeration("enum")
        .defaultSymbol("ONE")
        .symbols("ONE", "TWO", "THREE");

    Assertions.assertEquals(
        schema,
            SchemaValidator.validate(schema.toString()));
  }

  @Test
  public void validateSchemaTopTypeFixed() {
    Schema schema = SchemaBuilder.builder("org.blab")
        .fixed("fixed")
        .size(16);

    Assertions.assertEquals(
        schema,
        SchemaValidator.validate(schema.toString()));
  }
}
