package org.blab.types;

import org.apache.avro.Schema;
import org.apache.avro.generic.GenericRecord;

/**
 * Type mapper for inter-service type conversions.
 * 
 * @param T - mappable class;
 */
public interface TypeMapper<T> {
    public static final String PROPERTIES_BASE = "mapper";

    /**
     * Convert T to corresponding byte array.
     * 
     * @param obj - object to convert
     * @return Byte array representing given object.
     * @throws ObjectViolationException given object cannot be converted into byte
     *                                  array.
     */
    byte[] mapToBytes(T obj) throws ObjectViolationException;

    /**
     * Convert byte array to T.
     * 
     * @param bytes - bytes array from which object will be extracted
     * @return Object corresponding to given byte representation.
     * @throws ObjectViolationException given array connot be converted into object.
     */
    T mapFromBytes(byte[] bytes) throws ObjectViolationException;

    /**
     * Convert T to Avro record corresponding to specified schema.
     * 
     * @param schema - Avro schema
     * @param obj    - object to convert
     * @return Avro record representing given object by given schema.
     * @throws UnsupportedSchemaException given schema does not supported by current
     *                                    implementation.
     * @throws ObjectViolationException   given object cannot be mapped using
     *                                    specified schema.
     */
    GenericRecord mapToAvro(Schema schema, T obj) throws TypeException;

    /**
     * Convert Avro record to T.
     * 
     * @param record - Avro record to convert
     * @return T object corresponding to record's schema.
     * @throws UnsupportedSchemaException given schema does not supported by current
     *                                    implementation.
     */
    T mapFromAvro(GenericRecord record) throws UnsupportedSchemaException;
}
