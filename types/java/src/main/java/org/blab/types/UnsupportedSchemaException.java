package org.blab.types;

import org.apache.avro.Schema;

public class UnsupportedSchemaException extends TypeException {
    private Schema schema;

    public UnsupportedSchemaException(Schema schema) {
        super();
        this.schema = schema;
    }

    public UnsupportedSchemaException(Schema schema, String message) {
        super(message);
        this.schema = schema;
    }

    public Schema getSchema() {
        return schema;
    }
}
