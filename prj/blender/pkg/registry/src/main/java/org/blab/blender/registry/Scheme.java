package org.blab.blender.registry;

import java.util.UUID;
import java.lang.Cloneable;

import org.apache.avro.Schema;
import org.apache.avro.Schema.Parser;

/**
 * Schema by which services define outputs and configurations.
 *
 * Single schema can be shared by multiple services.
 */
public class Scheme implements Cloneable {
  private final UUID id;

  private String schema;
  private String namespace;
  private String name;

  /**
   * Validate given schema according to specifications.
   *
   * @return parsed Schema if it's valid.
   * @throws ValidationException  if schema is invalid.
   * @throws NullPointerException if schema is null.
   */
  public static Schema validate(String schema) {
    if (schema == null)
      throw new NullPointerException();

    try {
      Schema parsed = new Parser().parse(schema);
      Schema.Type type = parsed.getType();

      if (type.equals(Schema.Type.ENUM) ||
          type.equals(Schema.Type.FIXED) ||
          type.equals(Schema.Type.RECORD))
        return parsed;
      else
        throw new Exception("Unsupported top level type.");
    } catch (Exception e) {
      throw new ValidationException(e.getMessage());
    }
  }

  /**
   * Generate new scheme with specified schema.
   * 
   * @throws ValidationException  if schema if invalid.
   * @throws NullPointerException if schema is null.
   */
  public Scheme(String schema) {
    Schema parsed = validate(schema);

    this.id = UUID.randomUUID();
    this.schema = schema;
    this.name = parsed.getName();
    this.namespace = parsed.getNamespace();
  }

  private Scheme(UUID id, String schema) {
    this.id = id;
    this.schema = schema;
  }

  /**
   * @throws ValidationException  if schema is invalid.
   * @throws NullPointerException if schema is null.
   */
  public void setSchema(String schema) {
    Schema parsed = validate(schema);

    this.schema = schema;
    this.name = parsed.getName();
    this.namespace = parsed.getNamespace();
  }

  public UUID getId() {
    return this.id;
  }

  public String getSchema() {
    return this.schema;
  }

  /** Retrieve schema namespace. */
  public String getNamespace() {
    return this.namespace;
  }

  /** Retrieve schema name. */
  public String getName() {
    return this.name;
  }

  @Override
  public Scheme clone() {
    return new Scheme(this.id, this.schema);
  }
}
