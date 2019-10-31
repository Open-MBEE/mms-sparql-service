package org.openmbee.services.sparql.exceptions;

import org.openmbee.services.sparql.data.ParsedResult;

public class SparqlException extends RuntimeException {

    private ParsedResult r;

    public SparqlException(ParsedResult r) {
        this.r = r;
    }

    public ParsedResult getResult() {
        return r;
    }
}
