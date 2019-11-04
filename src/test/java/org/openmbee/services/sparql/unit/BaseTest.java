package org.openmbee.services.sparql.unit;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.openmbee.services.sparql.services.Parser;

@TestInstance(Lifecycle.PER_CLASS)
public abstract class BaseTest {

    static String uriPattern = "http://mms.openmbee.org/data/(?<projectId>[a-zA-Z0-9_-]+)/(?<refId>[a-zA-Z0-9_-]+)";

    static Parser parser;

    @BeforeAll
    static void initParser() {
        parser = new Parser(uriPattern);
    }
}
