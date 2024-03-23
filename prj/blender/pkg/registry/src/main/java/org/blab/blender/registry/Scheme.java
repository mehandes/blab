package org.blab.blender.registry;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;
import java.lang.Cloneable;

import org.apache.avro.Schema;
import org.blab.blender.registry.validation.SchemaValidator;
import org.blab.blender.registry.validation.ValidationException;

public class Scheme implements Cloneable {
  private final UUID id;
  private String schema;
  private String name;
  private String namespace;

  /**
   * @throws NullPointerException if schema is null.
   * @throws ValidationException if schema if invalid.
   */
  public Scheme(String schema) {
    this(UUID.randomUUID(), schema);
  }

  public static Scheme map(ResultSet resultSet) throws SQLException {
    return new Scheme(
        UUID.fromString(resultSet.getString("scheme_id_")), resultSet.getString("scheme_schema_"));
  }

  private Scheme(UUID id, String schema) {
    Schema parsed = SchemaValidator.validate(schema);

    this.id = id;
    setSchema(schema);
  }

  public void setSchema(String schema) {
    Schema parsed = SchemaValidator.validate(schema);

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

  public String getName() {
    return this.name;
  }

  public String getNamespace() {
    return this.namespace;
  }

  @Override
  public Scheme clone() {
    return new Scheme(this.id, this.schema);
  }

  @Override
  public String toString() {
    return "Scheme{"
        + "id="
        + id
        + ", schema='"
        + schema
        + '\''
        + ", namespace='"
        + namespace
        + '\''
        + ", name='"
        + name
        + '\''
        + '}';
  }
}
