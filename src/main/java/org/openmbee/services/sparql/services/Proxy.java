package org.openmbee.services.sparql.services;

import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class Proxy {

    @Value("${sparql.endpoint}")
    private String sparql;

    public void sendQuery(String query, List<String> defaults, List<String> named) {
        //TODO proxy to backend sparql endpoint and return result
    }

    public void sendUpdate(String update, List<String> using, List<String> named) {

    }

}
