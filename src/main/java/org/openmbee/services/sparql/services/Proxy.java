package org.openmbee.services.sparql.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class Proxy {

    @Value("${sparql.endpoint}")
    private String sparql;

}
