package org.blab.blender.registry.domain;

/** An entry containing the basic schema that blender works with. */
public class SchemaRecord {
  /** Unique identifier for the record. */
  private final String id;

  /** A string representation of the schema into which all messages in blender are converted. */
  private String schema;

  /** Pattern for topics that match the schema. Can be single topic. */
  private String pattern;

  public SchemaRecord(String id, String schema, String pattern) {
    if (id == null || schema == null || pattern == null)
      throw new NullPointerException("Schema cannot contain null values.");

    this.id = id;
    this.schema = schema;
    this.pattern = pattern;
  }

  public String getId() {
    return id;
  }

  public String getSchema() {
    return schema;
  }

  public void setSchema(String schema) {
    if (schema == null)
      throw new NullPointerException("Schema cannot contain null values.");

    this.schema = schema;
  }

  public String getPattern() {
    return pattern;
  }

  public void setPattern(String pattern) {
    if (pattern == null)
      throw new NullPointerException("Schema cannot contain null values.");

    this.pattern = pattern;
  }
}
