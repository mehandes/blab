package org.blab.blender.registry;

import org.blab.blender.registry.domain.SchemaRecord;
import org.blab.blender.registry.repository.InMemoryRecordRepository;
import org.blab.blender.registry.repository.SchemaRecordRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;

public class SchemaRecordRepositoryTest {
  private SchemaRecordRepository repository;

  @Before
  public void BeforeEach() {
    repository = new InMemoryRecordRepository();
  }

  @Test
  public void createNullRecordTest() {
    Assertions.assertThrows(NullPointerException.class, () -> repository.create(null));
  }

  @Test
  public void createTest() {
    Assertions.assertTrue(repository.create(new SchemaRecord("name1", "schema1", "pattern1")));
    Assertions.assertFalse(repository.create(new SchemaRecord("name1", "schema2", "pattern2")));
    Assertions.assertTrue(repository.create(new SchemaRecord("name2", "schema1", "pattern1")));
    Assertions.assertEquals(repository.getAll().size(), 2);
  }

  @Test
  public void updateNullRecordTest() {
    Assertions.assertThrows(NullPointerException.class, () -> repository.update(null));
  }

  @Test
  public void updateRecordExistsTest() {
    Assertions.assertTrue(repository.create(new SchemaRecord("name", "schema", "pattern")));
    Assertions.assertTrue(repository.update(new SchemaRecord("name", "schema", "pattern-new")));
    Assertions.assertEquals(repository.getAll().size(), 1);
  }

  @Test
  public void updateRecordDoesNotExistsTest() {
    Assertions.assertFalse(repository.update(new SchemaRecord("name", "schema", "pattern")));
  }

  @Test
  public void removeNullIdTest() {
    Assertions.assertThrows(NullPointerException.class, () -> repository.remove(null));
  }

  @Test
  public void removeRecordExistsTest() {
    Assertions.assertTrue(repository.create(new SchemaRecord("name", "schema", "pattern")));
    Assertions.assertTrue(repository.remove("name"));
    Assertions.assertTrue(repository.getAll().isEmpty());
  }

  @Test
  public void removeRecordDoesNotExistsTest() {
    Assertions.assertFalse(repository.remove("name"));
  }

  @Test
  public void clearTest() {
    Assertions.assertTrue(repository.create(new SchemaRecord("name1", "schema1", "pattern1")));
    Assertions.assertTrue(repository.create(new SchemaRecord("name2", "schema2", "pattern2")));
    Assertions.assertTrue(repository.create(new SchemaRecord("name3", "schema3", "pattern3")));
    repository.clear();

    Assertions.assertTrue(repository.getAll().isEmpty());
  }

  @Test
  public void getByIdNullIdTest() {
    Assertions.assertThrows(NullPointerException.class, () -> repository.getById(null));
  }

  @Test
  public void getByIdRecordExistsTest() {
    Assertions.assertTrue(repository.create(new SchemaRecord("name", "schema", "pattern")));
    Assertions.assertTrue(repository.getById("name").isPresent());
  }

  @Test
  public void getByIdRecordDoesNotExistsTest() {
    Assertions.assertFalse(repository.getById("name").isPresent());
  }

  @Test
  public void getByTopicNullTopicTest() {
    Assertions.assertThrows(NullPointerException.class, () -> repository.getByTopic(null));
  }

  @Test
  public void getByTopicSingleExistsTest() {
    Assertions.assertTrue(repository.create(new SchemaRecord("name", "schema", "pattern")));
    Assertions.assertEquals(repository.getByTopic("pattern").size(), 1);
  }

  @Test
  public void getByTopicMultipleExistsTest() {
    Assertions.assertTrue(repository.create(new SchemaRecord("name1", "schema", "pattern")));
    Assertions.assertTrue(repository.create(new SchemaRecord("name2", "schema", "pattern")));
    Assertions.assertTrue(repository.create(new SchemaRecord("name3", "schema", "pattern")));
    Assertions.assertEquals(repository.getByTopic("pattern").size(), 3);
  }

  @Test
  public void getByTopicZeroExistsTest() {
    Assertions.assertEquals(repository.getByTopic("pattern").size(), 0);
  }

  @Test
  public void getAllZeroExistsTest() {
    Assertions.assertTrue(repository.getAll().isEmpty());
  }

  @Test
  public void getAllNonZeroExistsTest() {
    Assertions.assertTrue(repository.create(new SchemaRecord("name1", "schema", "pattern")));
    Assertions.assertTrue(repository.create(new SchemaRecord("name2", "schema", "pattern")));
    Assertions.assertTrue(repository.create(new SchemaRecord("name3", "schema", "pattern")));
    Assertions.assertEquals(repository.getAll().size(), 3);
  }
}
