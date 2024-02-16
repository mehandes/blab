package org.blab.blender.registry.domain;

import java.util.List;

public class PatternIntersectionException extends Exception {
  private List<SchemaRecord> records;

  public PatternIntersectionException(List<SchemaRecord> records) {
    super();
    this.records = records;
  }

  public PatternIntersectionException(List<SchemaRecord> records, String message) {
    super(message);
    this.records = records;
  }

  public List<SchemaRecord> getRecords() {
    return records;
  }
}
