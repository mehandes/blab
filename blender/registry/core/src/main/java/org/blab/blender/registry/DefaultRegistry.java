package org.blab.blender.registry;

import java.util.List;
import java.util.Optional;
import org.blab.blender.registry.domain.PatternIntersectionException;
import org.blab.blender.registry.domain.PatternValidator;
import org.blab.blender.registry.domain.SchemaRecord;
import org.blab.blender.registry.domain.SchemaValidator;
import org.blab.blender.registry.domain.ValidationException;
import org.blab.blender.registry.repository.SchemaRecordRepository;

public class DefaultRegistry implements Registry {
  private final SchemaRecordRepository repository;
  private final SchemaValidator schemaValidator;
  private final PatternValidator patternValidator;

  public DefaultRegistry(
      SchemaRecordRepository repository,
      SchemaValidator schemaValidator,
      PatternValidator patternValidator) {
    this.repository = repository;
    this.schemaValidator = schemaValidator;
    this.patternValidator = patternValidator;
  }

  @Override
  public boolean create(SchemaRecord record) throws ValidationException {
    if (record == null) throw new NullPointerException();

    schemaValidator.validate(record.getSchema());
    patternValidator.validate(record.getPattern());

    return repository.create(record);
  }

  @Override
  public boolean update(SchemaRecord record) throws ValidationException {
    if (record == null) throw new NullPointerException();

    schemaValidator.validate(record.getSchema());
    patternValidator.validate(record.getPattern());

    return repository.update(record);
  }

  @Override
  public boolean remove(String id) {
    return repository.remove(id);
  }

  @Override
  public void clear() {
    repository.clear();
  }

  @Override
  public Optional<SchemaRecord> getById(String id) {
    return repository.getById(id);
  }

  @Override
  public Optional<SchemaRecord> getByTopic(String topic) throws PatternIntersectionException {
    List<SchemaRecord> records = repository.getByTopic(topic);
    if (records.size() > 1) throw new PatternIntersectionException(records);
    return records.size() == 0 ? Optional.empty() : Optional.of(records.get(0));
  }

  @Override
  public List<SchemaRecord> getAll() {
    return repository.getAll();
  }
}
