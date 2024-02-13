package org.blab.types;

/** Parent exception for all conversion errors. */
public class TypeException extends Exception {
    public TypeException() {
        super();
    }

    public TypeException(String message) {
        super(message);
    }
}
