package org.blab.types;

import org.apache.avro.Schema;
import org.apache.avro.generic.GenericRecord;

/**
 * Type mapper for inter-service type conversions. The provided schemas are assumed to be correct.
 *
 * @param T - type to be mapped
 */
public interface TypeMapper<T> {
  public static final String PROPERTIES_BASE = "mapper";

  /**
   * Convert T to corresponding byte array.
   *
   * @param obj - object to convert
   * @return Object's binary representation.
   * @throws IllegalArgumentException if the given object cannot be converted to a byte array.
   */
  byte[] mapToBytes(T obj);

  /**
   * Convert byte array to T.
   *
   * @param bytes - byte array from which the object will be retrieved
   * @return The object corresponding to the given binary representation.
   * @throws IllegalArgumentException if the given array connot be converted to an object.
   */
  T mapFromBytes(byte[] bytes);

  /**
   * Convert an object of type T to an Avro record matching the specified schema.
   *
   * @param schema - Avro schema
   * @param obj - object to convert
   * @return An Avro record that represents a given object according to a given schema.
   * @throws IllegalArgumentException if the given object cannot be mapped using the specified
   *     schema.
   */
  GenericRecord mapToAvro(Schema schema, T obj);

  /**
   * Convert the Avro record to an object of type T.
   *
   * @param record - Avro record to convert
   * @return T object corresponding to record's schema.
   */
  T mapFromAvro(GenericRecord record);
}
