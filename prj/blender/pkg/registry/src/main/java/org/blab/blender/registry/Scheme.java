package org.blab.blender.registry;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;
import java.lang.Cloneable;

import org.apache.avro.Schema;

/** Schema by which services define outputs and configurations. */
public class Scheme implements Cloneable {
  /** Hard scheme identifier. */
  private final UUID id;

  private String schema;
  private String namespace;
  private String name;

  /**
   * @throws ValidationException if schema if invalid.
   * @throws NullPointerException if schema is null.
   */
  public Scheme(String schema) {
    this(UUID.randomUUID(), schema);
  }

  private Scheme(UUID id, String schema) {
    Schema parsed = SchemaValidator.validate(schema);

    this.id = id;
    this.schema = schema;
    this.name = parsed.getName();
    this.namespace = parsed.getNamespace();
  }

  public static Scheme map(ResultSet resultSet) throws SQLException {
    return new Scheme(
            UUID.fromString(resultSet.getString("id_")), resultSet.getString("schema_"));
  }

  /**
   * @throws ValidationException if schema is invalid.
   * @throws NullPointerException if schema is null.
   */
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

  @Override
  public String toString() {
    return "Scheme{" +
            "id=" + id +
            ", schema='" + schema + '\'' +
            ", namespace='" + namespace + '\'' +
            ", name='" + name + '\'' +
            '}';
  }
}
