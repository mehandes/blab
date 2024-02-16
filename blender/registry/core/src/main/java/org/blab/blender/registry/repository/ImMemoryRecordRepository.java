package org.blab.blender.registry.repository;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Pattern;
import org.blab.blender.registry.domain.SchemaRecord;

public class ImMemoryRecordRepository implements SchemaRecordRepository {
  private Map<String, SchemaRecord> data;

  public ImMemoryRecordRepository() {
    data = new LinkedHashMap<>();
  }

  @Override
  public boolean create(SchemaRecord record) {
    if (record == null)
      throw new NullPointerException();

    if (data.containsKey(record.getId()))
      return false;
    else
      data.put(record.getId(), record);

    return true;
  }

  @Override
  public boolean update(SchemaRecord record) {
    if (record == null)
      throw new NullPointerException();

    if (!data.containsKey(record.getId()))
      return false;
    else
      data.put(record.getId(), record);

    return true;
  }

  @Override
  public boolean remove(String id) {
    if (id == null)
      throw new NullPointerException();

    if (!data.containsKey(id))
      return false;
    else
      data.remove(id);

    return true;
  }

  @Override
  public void clear() {
    data.clear();
  }

  @Override
  public Optional<SchemaRecord> getById(String id) {
    if (id == null)
      throw new NullPointerException();

    return Optional.ofNullable(data.get(id));
  }

  @Override
  public List<SchemaRecord> getByTopic(String topic) {
    if (topic == null)
      throw new NullPointerException();

    List<SchemaRecord> result = new ArrayList<>();

    data.entrySet().forEach(entry -> {
      Pattern pattern = Pattern.compile(entry.getValue().getPattern());

      if (pattern.matcher(topic).matches())
        result.add(entry.getValue());
    });

    return result;
  }

  @Override
  public List<SchemaRecord> getAll() {
    return data.entrySet().stream().map(entry -> entry.getValue()).toList();
  }

}
