package org.openmbee.services.sparql.unit;

import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.openmbee.services.sparql.exceptions.SparqlException;

public class QueryFailTests extends BaseTest {

    @Test
    void hasNoDefault() {
        String query = "PREFIX foaf: <http://xmlns.com/foaf/0.1/>\n" //default and named dataset iri populated
            + "PREFIX dc: <http://purl.org/dc/elements/1.1/>\n"
            + "\n"
            + "SELECT ?who ?g ?mbox\n"
            + "WHERE\n"
            + "{\n"
            + "   ?g dc:publisher ?who .\n"
            + "}";
        SparqlException e = assertThrows(SparqlException.class, () -> parser.parseQuery(query, null, null));
    }

    @Test
    void badUri() {
        String query = "PREFIX foaf: <http://xmlns.com/foaf/0.1/>\n" //default and named dataset iri populated
            + "PREFIX dc: <http://purl.org/dc/elements/1.1/>\n"
            + "\n"
            + "SELECT ?who ?g ?mbox\n"
            + "FROM <http://blah>\n"
            + "WHERE\n"
            + "{\n"
            + "   ?g dc:publisher ?who .\n"
            + "}";
        SparqlException e = assertThrows(SparqlException.class, () -> parser.parseQuery(query, null, null));
    }

    @Test
    void graphVarWithoutNamed() {
        String query = "PREFIX foaf: <http://xmlns.com/foaf/0.1/>\n" //default and named dataset iri populated
            + "PREFIX dc: <http://purl.org/dc/elements/1.1/>\n"
            + "\n"
            + "SELECT ?who ?g ?mbox\n"
            + "WHERE\n"
            + "{\n"
            + "   GRAPH ?g { ?x foaf:mbox ?mbox }\n"
            + "}";
        SparqlException e = assertThrows(SparqlException.class, () -> parser.parseQuery(query, null, null));
    }
}
