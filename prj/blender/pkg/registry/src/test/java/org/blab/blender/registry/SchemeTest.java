package org.blab.blender.registry;

import org.junit.jupiter.api.Assertions;
import org.apache.avro.*;
import org.junit.Test;

public class SchemeTest {
  @Test
  public void validateSchemaNull() {
    Assertions.assertThrows(
        NullPointerException.class,
        () -> {
          Scheme.validate(null);
        });
  }

  @Test
  public void validateSchemaMisspelled() {
    Assertions.assertThrows(
        IllegalArgumentException.class,
        () -> {
          Scheme.validate("schema");
        });
  }

  @Test
  public void validateSchemaUnsupportedTopType() {
    Schema schema = SchemaBuilder.builder("org.blab")
        .doubleType();

    Assertions.assertThrows(
        IllegalArgumentException.class,
        () -> {
          Scheme.validate(schema.toString());
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
        Scheme.validate(schema.toString()));
  }

  @Test
  public void validateSchemaTopTypeEnum() {
    Schema schema = SchemaBuilder.builder("org.blab")
        .enumeration("enum")
        .defaultSymbol("ONE")
        .symbols("ONE", "TWO", "THREE");

    Assertions.assertEquals(
        schema,
        Scheme.validate(schema.toString()));
  }

  @Test
  public void validateSchemaTopTypeFixed() {
    Schema schema = SchemaBuilder.builder("org.blab")
        .fixed("fixed")
        .size(16);

    Assertions.assertEquals(
        schema,
        Scheme.validate(schema.toString()));
  }
}
