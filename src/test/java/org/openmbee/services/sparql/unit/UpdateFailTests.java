package org.openmbee.services.sparql.unit;

import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;
import java.util.List;
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

    @Test
    void insertDataDefault() {
        String update = "PREFIX foaf:  <http://xmlns.com/foaf/0.1/>\n"
            + "INSERT DATA { foaf:hi foaf:givenName 'William' }\n";
        SparqlException e = assertThrows(SparqlException.class, () -> parser.parseUpdate(update, null, null));
    }

    @Test
    void deleteDataDefault() {
        String update = "PREFIX foaf:  <http://xmlns.com/foaf/0.1/>\n"
            + "DELETE DATA { foaf:hi foaf:givenName 'William' }\n";
        SparqlException e = assertThrows(SparqlException.class, () -> parser.parseUpdate(update, null, null));
    }

    @Test
    void deleteWhereDefault() {
        String update = "PREFIX foaf:  <http://xmlns.com/foaf/0.1/>\n"
            + "DELETE WHERE {\n"
            + "   ?person foaf:givenName 'Bill'}\n";
        SparqlException e = assertThrows(SparqlException.class, () -> parser.parseUpdate(update, null, null));
    }

    @Test
    void copyDefault() {
        String update =
            "COPY DEFAULT TO <http://mms.openmbee.org/data/asdf/fdsa>\n";
        SparqlException e = assertThrows(SparqlException.class, () -> parser.parseUpdate(update, null, null));
    }

    @Test
    void addDefault() {
        String update =
            "ADD DEFAULT TO GRAPH <http://mms.openmbee.org/data/asdf/fdsa>\n";
        SparqlException e = assertThrows(SparqlException.class, () -> parser.parseUpdate(update, null, null));
    }

    @Test
    void clearAll() {
        String update = "CLEAR ALL";
        SparqlException e = assertThrows(SparqlException.class, () -> parser.parseUpdate(update, null, null));
    }

    @Test
    void clearNamed() {
        String update = "CLEAR NAMED";
        SparqlException e = assertThrows(SparqlException.class, () -> parser.parseUpdate(update, null, null));
    }

    @Test
    void clearDefault() {
        String update = "CLEAR DEFAULT";
        SparqlException e = assertThrows(SparqlException.class, () -> parser.parseUpdate(update, null, null));
    }

    @Test
    void create() {
        String update = "CREATE GRAPH <http://mms.opembee.org/data/fdsf/asdfs>";
        SparqlException e = assertThrows(SparqlException.class, () -> parser.parseUpdate(update, null, null));
    }

    @Test
    void drop() {
        String update = "DROP GRAPH <http://mms.opembee.org/data/fdsf/asdfs>";
        SparqlException e = assertThrows(SparqlException.class, () -> parser.parseUpdate(update, null, null));
    }

    @Test
    void move() {
        String update = "MOVE GRAPH <http://mms.opembee.org/data/fdsf/asdfs> to <http://mms.opembee.org/data/fdsf/affff>";
        SparqlException e = assertThrows(SparqlException.class, () -> parser.parseUpdate(update, null, null));
    }

    @Test
    void insertDefault() {
        String update = "PREFIX dc:  <http://purl.org/dc/elements/1.1/>\n"
            + "PREFIX dcmitype: <http://purl.org/dc/dcmitype/>\n"
            + "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>\n"
            + "\n"
            + "INSERT\n"
            + "  { ?book ?p ?v }\n"
            + "WHERE\n"
            + "  { GRAPH  <http://mms.openmbee.org/data/fds/sdf>\n"
            + "     { ?book dc:date ?date . \n"
            + "       FILTER ( ?date < \"2000-01-01T00:00:00-02:00\"^^xsd:dateTime )\n"
            + "       ?book ?p ?v\n"
            + "     }\n"
            + "  } ;";
        SparqlException e = assertThrows(SparqlException.class, () -> parser.parseUpdate(update, null, null));
    }

    @Test
    void insertNoUsing() {
        String update = "PREFIX dc:  <http://purl.org/dc/elements/1.1/>\n"
            + "PREFIX dcmitype: <http://purl.org/dc/dcmitype/>\n"
            + "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>\n"
            + "\n"
            + "INSERT\n"
            + "  { GRAPH <http://mms.openmbee.org/data/asdf/fda> { ?book ?p ?v } }\n"
            + "WHERE\n"
            + "  { ?book dc:date ?date . \n"
            + "       FILTER ( ?date < \"2000-01-01T00:00:00-02:00\"^^xsd:dateTime )\n"
            + "       ?book ?p ?v\n"
            + "  } ;";
        SparqlException e = assertThrows(SparqlException.class, () -> parser.parseUpdate(update, null, null));
    }

    @Test
    void deleteUsingInParamNoNamed() {
        List<String> using = new ArrayList<>();
        using.add("http://mms.openmbee.org/data/asdf/fdsa");
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
        SparqlException e = assertThrows(SparqlException.class, () -> parser.parseUpdate(update, using, null));
    }
}
