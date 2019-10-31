package org.openmbee.services.sparql.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class Permission {

    @Value("${permission.lookup.endpoint}")
    private String endpoint;

    @Value("${query.privilege}")
    private String readPrivilege;

    @Value("${update.privilege}")
    private String updatePrivilege;
}
