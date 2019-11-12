package org.openmbee.services.sparql.unit;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.openmbee.services.sparql.data.ParsedResult;

public class UpdatePassTests extends BaseTest {

    @Test
    void with() {
        String update = "PREFIX foaf:  <http://xmlns.com/foaf/0.1/>\n"
            + "WITH <http://mms.openmbee.org/data/asdf/asdf>\n"
            + "DELETE { ?person foaf:givenName 'Bill' }\n"
            + "INSERT { ?person foaf:givenName 'William' }\n"
            + "WHERE\n"
            + "  { ?person foaf:givenName 'Bill'\n"
            + "  } ";
        ParsedResult re = parser.parseUpdate(update, null, null);
        assertEquals(re.getToRead().size(), 1);
        assertEquals(re.getToModify().size(), 1);
    }

    @Test
    void insertData() {
        String update = "PREFIX foaf:  <http://xmlns.com/foaf/0.1/>\n"
            + "INSERT DATA { GRAPH <http://mms.openmbee.org/data/asdf/fds> { foaf:hi foaf:givenName 'William' }}\n";
        ParsedResult re = parser.parseUpdate(update, null, null);
        assertEquals(re.getToModify().size(), 1);
    }

    @Test
    void deleteData() {
        String update = "PREFIX foaf:  <http://xmlns.com/foaf/0.1/>\n"
            + "DELETE DATA { GRAPH <http://mms.openmbee.org/data/asdf/fds> { foaf:hi foaf:givenName 'William' }}\n";
        ParsedResult re = parser.parseUpdate(update, null, null);
        assertEquals(re.getToModify().size(), 1);
    }

    @Test
    void deleteWhere() {
        String update = "PREFIX foaf:  <http://xmlns.com/foaf/0.1/>\n"
            + "DELETE WHERE {\n"
            + "  GRAPH <http://mms.openmbee.org/data/asdf/fdsa> { ?person foaf:givenName 'Bill'}\n"
            + "  GRAPH <http://mms.openmbee.org/data/aaaa/bbbb> { ?x ?y ?z}}";
        ParsedResult re = parser.parseUpdate(update, null, null);
        assertEquals(re.getToModify().size(), 2);
    }

    @Test
    void copy() {
        String update =
            "COPY <http://mms.openmbee.org/data/asdf/asdf> TO GRAPH <http://mms.openmbee.org/data/asdf/fdsa>\n";
        ParsedResult re = parser.parseUpdate(update, null, null);
        assertEquals(re.getToRead().size(), 1);
        assertEquals(re.getToModify().size(), 1);
    }

    @Test
    void add() {
        String update =
            "ADD <http://mms.openmbee.org/data/asdf/asdf> TO GRAPH <http://mms.openmbee.org/data/asdf/fdsa>\n";
        ParsedResult re = parser.parseUpdate(update, null, null);
        assertEquals(re.getToRead().size(), 1);
        assertEquals(re.getToModify().size(), 1);
    }

    @Test
    void clear() {
        String update =
            "CLEAR GRAPH <http://mms.openmbee.org/data/asdf/fdsa>";
        ParsedResult re = parser.parseUpdate(update, null, null);
        assertEquals(re.getToModify().size(), 1);
    }

    @Test
    void insertWhere() {
        String update = "PREFIX dc:  <http://purl.org/dc/elements/1.1/>\n"
            + "PREFIX dcmitype: <http://purl.org/dc/dcmitype/>\n"
            + "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>\n"
            + "\n"
            + "INSERT\n"
            + "  { GRAPH <http://mms.openmbee.org/data/asdf/fda> { ?book ?p ?v } }\n"
            + "WHERE\n"
            + "  { GRAPH  <http://mms.openmbee.org/data/fds/sdf>\n"
            + "     { ?book dc:date ?date . \n"
            + "       FILTER ( ?date < \"2000-01-01T00:00:00-02:00\"^^xsd:dateTime )\n"
            + "       ?book ?p ?v\n"
            + "     }\n"
            + "  } ;";
        ParsedResult re = parser.parseUpdate(update, null, null);
        assertEquals(re.getToModify().size(), 1);
        assertEquals(re.getToRead().size(), 1);
    }

    @Test
    void insertUsing() {
        String update = "PREFIX dc:  <http://purl.org/dc/elements/1.1/>\n"
            + "PREFIX dcmitype: <http://purl.org/dc/dcmitype/>\n"
            + "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>\n"
            + "\n"
            + "INSERT\n"
            + "  { GRAPH <http://mms.openmbee.org/data/asdf/fda> { ?book ?p ?v } }\n"
            + "USING <http://mms.openmbee.org/data/fds/sdf>"
            + "WHERE\n"
            + "  { ?book dc:date ?date . \n"
            + "       FILTER ( ?date < \"2000-01-01T00:00:00-02:00\"^^xsd:dateTime )\n"
            + "       ?book ?p ?v\n"
            + "  } ;";
        ParsedResult re = parser.parseUpdate(update, null, null);
        assertEquals(re.getToModify().size(), 1);
        assertEquals(re.getToRead().size(), 1);
    }

    @Test
    void deleteUsingInParam() {
        List<String> using = new ArrayList<>();
        List<String> named = new ArrayList<>();
        using.add("http://mms.openmbee.org/data/asdf/fdsa");
        named.add("http://mms.openmbee.org/data/qqqq/wwww");
        String update = "PREFIX dc:  <http://purl.org/dc/elements/1.1/>\n"
            + "PREFIX dcmitype: <http://purl.org/dc/dcmitype/>\n"
            + "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>\n"
            + "\n"
            + "DELETE\n"
            + "  { GRAPH <http://mms.openmbee.org/data/asdf/fda> { ?book ?p ?v } }\n"
            + "WHERE\n"
            + "  { ?book dc:date ?date . \n"
            + "       FILTER ( ?date < \"2000-01-01T00:00:00-02:00\"^^xsd:dateTime )\n"
            + "       ?book ?p ?v\n"
            + "    GRAPH ?g\n"
            + "    { ?book dc:date ?date . \n"
            + "      FILTER ( ?date < \"2000-01-01T00:00:00-02:00\"^^xsd:dateTime )\n"
            + "      ?book ?p ?v }\n"
            + "  } ;";
        ParsedResult re = parser.parseUpdate(update, using, named);
        assertEquals(re.getToModify().size(), 1);
        assertEquals(re.getToRead().size(), 2);
    }
}
