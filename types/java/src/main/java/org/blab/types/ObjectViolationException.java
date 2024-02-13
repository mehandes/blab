package org.blab.types;

import java.util.List;

public class ObjectViolationException extends TypeException {
    private Object object;
    private List<FieldViolation> violations;

    public ObjectViolationException(Object object, List<FieldViolation> violations, String message) {
        super(message);
        this.object = object;
        this.violations = violations;
    }

    public ObjectViolationException(Object object, List<FieldViolation> violations) {
        super();
        this.object = object;
        this.violations = violations;
    }

    public Object getObject() {
        return object;
    }

    public List<FieldViolation> getViolations() {
        return violations;
    }

}