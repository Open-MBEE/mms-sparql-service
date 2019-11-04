package org.openmbee.services.sparql.unit;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.openmbee.services.sparql.data.ParsedResult;

public class UpdatePassTests extends BaseTest {

    @Test
    void with() {
        String update = "PREFIX foaf:  <http://xmlns.com/foaf/0.1/>\n"
            + "\n"
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
}
