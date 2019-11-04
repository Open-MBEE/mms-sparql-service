package org.openmbee.services.sparql.unit;

import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.openmbee.services.sparql.exceptions.SparqlException;

public class UpdateFailTests extends BaseTest {

    @Test
    void noWith() {
        String update = "PREFIX foaf:  <http://xmlns.com/foaf/0.1/>\n"
            + "\n"
            + "DELETE { ?person foaf:givenName 'Bill' }\n"
            + "INSERT { ?person foaf:givenName 'William' }\n"
            + "WHERE\n"
            + "  { ?person foaf:givenName 'Bill'\n"
            + "  } ";
        SparqlException e = assertThrows(SparqlException.class, () -> parser.parseUpdate(update, null, null));
    }
}
