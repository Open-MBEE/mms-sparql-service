package org.openmbee.services.sparql.exceptions;

public class PermissionException extends RuntimeException {

    private Object r;

    public PermissionException(Object r) {
        this.r = r;
    }

    public Object getResult() {
        return r;
    }
}
