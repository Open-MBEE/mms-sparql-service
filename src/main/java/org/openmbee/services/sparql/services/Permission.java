package org.openmbee.services.sparql.services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.apache.commons.lang3.tuple.Pair;
import org.openmbee.services.sparql.data.ParsedResult;
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

    public void checkPermissions(ParsedResult result, Optional<String> auth) {
        List<Map> lookups = new ArrayList<>();//TODO create object or use swagger generated lib?
        for (Pair<String, String> pair: result.getToRead()) {
            Map<String, Object> lookup = new HashMap<>();
            lookup.put("projectId", pair.getKey());
            lookup.put("refId", pair.getValue());
            lookup.put("privilege", readPrivilege);
            lookup.put("type", "BRANCH");
            lookup.put("allowAnonIfPublic", true);
            lookups.add(lookup);
        }
        for (Pair<String, String> pair: result.getToModify()) {
            Map<String, Object> lookup = new HashMap<>();
            lookup.put("projectId", pair.getKey());
            lookup.put("refId", pair.getValue());
            lookup.put("privilege", updatePrivilege);
            lookup.put("type", "BRANCH");
            lookup.put("allowAnonIfPublic", false);
            lookups.add(lookup);
        }
        //check perm, throw exception if not all passed
    }
}
