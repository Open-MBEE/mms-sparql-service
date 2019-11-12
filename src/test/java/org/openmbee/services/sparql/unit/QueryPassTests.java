package org.openmbee.services.sparql.unit;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.openmbee.services.sparql.data.ParsedResult;

public class QueryPassTests extends BaseTest {

    @Test
    void hasFromAndNamed() {
        String query = "PREFIX foaf: <http://xmlns.com/foaf/0.1/>\n"
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

    @Test
    void hasDefaultAndNamedAsParams() {
        List<String> defaults = new ArrayList<>();
        List<String> named = new ArrayList<>();
        defaults.add("http://mms.openmbee.org/data/asdf/fdsa");
        named.add("http://mms.openmbee.org/data/asdf/fdsa");
        named.add("http://mms.openmbee.org/data/qqqq/wwww");
        String query = "PREFIX foaf: <http://xmlns.com/foaf/0.1/>\n"
            + "PREFIX dc: <http://purl.org/dc/elements/1.1/>\n"
            + "\n"
            + "SELECT ?who ?g ?mbox\n"
            + "WHERE\n"
            + "{\n"
            + "   ?g dc:publisher ?who .\n"
            + "   GRAPH ?g { ?x foaf:mbox ?mbox }\n"
            + "}";
        ParsedResult re = parser.parseQuery(query, defaults, named);
        assertEquals(re.getToRead().size(), 2);
    }

    @Test
    void namedGraph() {
        String query = "PREFIX foaf: <http://xmlns.com/foaf/0.1/>\n"
            + "PREFIX dc: <http://purl.org/dc/elements/1.1/>\n"
            + "\n"
            + "SELECT ?x ?y ?z\n"
            + "WHERE\n"
            + "{\n"
            + "   GRAPH <http://mms.openmbee.org/data/fds/asdf> { ?x ?y ?z }\n"
            + "}";
        ParsedResult re = parser.parseQuery(query, null, null);
        assertEquals(re.getToRead().size(), 1);
    }
}
