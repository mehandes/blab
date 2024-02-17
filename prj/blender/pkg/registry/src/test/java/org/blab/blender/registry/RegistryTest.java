package org.blab.blender.registry;

import org.apache.avro.SchemaBuilder;
import org.blab.blender.registry.domain.AvroSchemaValidator;
import org.blab.blender.registry.domain.PatternIntersectionException;
import org.blab.blender.registry.domain.PosixBasicPatternValidator;
import org.blab.blender.registry.domain.SchemaRecord;
import org.blab.blender.registry.domain.ValidationException;
import org.blab.blender.registry.repository.InMemorySchemaRecordRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;

public class RegistryTest {
  private static final String VALID_SCHEMA =
      SchemaBuilder.record("name").namespace("space").fields().endRecord().toString();
  private Registry registry;

  @Before
  public void before() {
    registry =
        new DefaultRegistry(
            new InMemorySchemaRecordRepository(),
            new AvroSchemaValidator(),
            new PosixBasicPatternValidator());
  }

  @Test
  public void createNullRecordTest() {
    Assertions.assertThrows(NullPointerException.class, () -> registry.create(null));
  }

  @Test
  public void createRecordWithInvalidSchemaTest() {
    Assertions.assertThrows(
        ValidationException.class,
        () -> registry.create(new SchemaRecord("name", "[]", "pattern")));
  }

  @Test
  public void createRecordWithInvalidPatternTest() {
    Assertions.assertThrows(
        ValidationException.class,
        () -> registry.create(new SchemaRecord("name", VALID_SCHEMA, "")));
  }

  @Test
  public void createRecordThatAlreadyExistsTest() {
    Assertions.assertTrue(registry.create(new SchemaRecord("name", VALID_SCHEMA, "pattern")));
    Assertions.assertFalse(registry.create(new SchemaRecord("name", VALID_SCHEMA, "pattern")));
    Assertions.assertEquals(registry.getAll().size(), 1);
  }

  @Test
  public void updateNullRecordTest() {
    Assertions.assertThrows(NullPointerException.class, () -> registry.update(null));
  }

  @Test
  public void updateRecordThatExistsTest() {
    Assertions.assertTrue(registry.create(new SchemaRecord("name", VALID_SCHEMA, "pattern")));
    Assertions.assertTrue(registry.update(new SchemaRecord("name", VALID_SCHEMA, "pattern-new")));
    Assertions.assertEquals(registry.getAll().size(), 1);
  }

  @Test
  public void updateRecordThatDoesNotExistsTest() {
    Assertions.assertFalse(registry.update(new SchemaRecord("name", VALID_SCHEMA, "pattern")));
  }

  @Test
  public void updateRecordWithInvalidSchemaTest() {
    Assertions.assertTrue(registry.create(new SchemaRecord("name", VALID_SCHEMA, "pattern")));

    Assertions.assertThrows(
        ValidationException.class,
        () -> registry.update(new SchemaRecord("name", "[]", "pattern")));
  }

  @Test
  public void updateRecordWithInvalidPatternTest() {
    Assertions.assertTrue(registry.create(new SchemaRecord("name", VALID_SCHEMA, "pattern")));

    Assertions.assertThrows(
        ValidationException.class,
        () -> registry.update(new SchemaRecord("name", VALID_SCHEMA, "")));
  }

  @Test
  public void removeNullIdTest() {
    Assertions.assertThrows(NullPointerException.class, () -> registry.remove(null));
  }

  @Test
  public void removeRecordThatExistsTest() {
    Assertions.assertTrue(registry.create(new SchemaRecord("name", VALID_SCHEMA, "pattern")));
    Assertions.assertTrue(registry.remove("name"));
    Assertions.assertTrue(registry.getAll().isEmpty());
  }

  @Test
  public void removeRecordThatDoesNotExistsTest() {
    Assertions.assertFalse(registry.remove("name"));
  }

  @Test
  public void clearTest() {
    Assertions.assertTrue(registry.create(new SchemaRecord("name1", VALID_SCHEMA, "pattern1")));
    Assertions.assertTrue(registry.create(new SchemaRecord("name2", VALID_SCHEMA, "pattern2")));
    Assertions.assertTrue(registry.create(new SchemaRecord("name3", VALID_SCHEMA, "pattern3")));
    registry.clear();

    Assertions.assertTrue(registry.getAll().isEmpty());
  }

  @Test
  public void getByIdNullIdTest() {
    Assertions.assertThrows(NullPointerException.class, () -> registry.getById(null));
  }

  @Test
  public void getByIdRecordExistsTest() {
    Assertions.assertTrue(registry.create(new SchemaRecord("name", VALID_SCHEMA, "pattern")));
    Assertions.assertTrue(registry.getById("name").isPresent());
  }

  @Test
  public void getByIdRecordDoesNotExistsTest() {
    Assertions.assertFalse(registry.getById("name").isPresent());
  }

  @Test
  public void getByTopicNullTopicTest() {
    Assertions.assertThrows(NullPointerException.class, () -> registry.getByTopic(null));
  }

  @Test
  public void getByTopicSingleExistsTest() {
    Assertions.assertTrue(registry.create(new SchemaRecord("name", VALID_SCHEMA, "pattern")));
    Assertions.assertDoesNotThrow(
        () -> Assertions.assertTrue(registry.getByTopic("pattern").isPresent()));
  }

  @Test
  public void getByTopicMultipleExistsTest() {
    Assertions.assertTrue(registry.create(new SchemaRecord("name1", VALID_SCHEMA, "pattern")));
    Assertions.assertTrue(registry.create(new SchemaRecord("name2", VALID_SCHEMA, "pattern")));
    Assertions.assertThrows(
        PatternIntersectionException.class, () -> registry.getByTopic("pattern"));
  }

  @Test
  public void getByTopicZeroExistsTest() {
    Assertions.assertDoesNotThrow(
        () -> Assertions.assertFalse(registry.getByTopic("pattern").isPresent()));
  }

  @Test
  public void getAllZeroExistsTest() {
    Assertions.assertTrue(registry.getAll().isEmpty());
  }

  @Test
  public void getAllNonZeroExistsTest() {
    Assertions.assertTrue(registry.create(new SchemaRecord("name1", VALID_SCHEMA, "pattern")));
    Assertions.assertTrue(registry.create(new SchemaRecord("name2", VALID_SCHEMA, "pattern")));
    Assertions.assertTrue(registry.create(new SchemaRecord("name3", VALID_SCHEMA, "pattern")));
    Assertions.assertEquals(registry.getAll().size(), 3);
  }
}
