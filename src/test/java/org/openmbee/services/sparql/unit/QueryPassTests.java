package org.openmbee.services.sparql.unit;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.openmbee.services.sparql.data.ParsedResult;

public class QueryPassTests extends BaseTest {

    @Test
    void hasFromAndNamed() {
        String query = "PREFIX foaf: <http://xmlns.com/foaf/0.1/>\n" //default and named dataset iri populated
            + "PREFIX dc: <http://purl.org/dc/elements/1.1/>\n"
            + "\n"
            + "SELECT ?who ?g ?mbox\n"
            + "FROM <http://mms.openmbee.org/data/asdf/fdsa>\n"
            + "FROM NAMED <http://mms.openmbee.org/data/asdf/fdsa>\n"
            + "FROM NAMED <http://mms.openmbee.org/data/qqqq/wwww>\n"
            + "WHERE\n"
            + "{\n"
            + "   ?g dc:publisher ?who .\n"
            + "   GRAPH ?g { ?x foaf:mbox ?mbox }\n"
            + "}";
        ParsedResult re = parser.parseQuery(query, null, null);
        assertEquals(re.getToRead().size(), 2);
    }
}
