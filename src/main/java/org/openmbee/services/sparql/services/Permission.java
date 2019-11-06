package org.openmbee.services.sparql.services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.apache.commons.lang3.tuple.Pair;
import org.openmbee.services.sparql.data.ParsedResult;
import org.openmbee.services.sparql.exceptions.PermissionException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Component
public class Permission {

    @Value("${permission.lookup.endpoint}")
    private String endpoint;

    @Value("${query.privilege}")
    private String readPrivilege;

    @Value("${update.privilege}")
    private String updatePrivilege;

    public void checkPermissions(ParsedResult result, Optional<String> auth) {
        List<Map> lookups = new ArrayList<>();//TODO create or import object or use swagger generated lib?
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
        Map<String, Object> body = new HashMap<>();
        body.put("lookups", lookups);

        RestTemplate r = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");
        auth.ifPresent(a -> headers.set("Authorization", a));
        HttpEntity<Map> entity = new HttpEntity<>(body, headers);

        try {
            ResponseEntity<Map> res = r.exchange(endpoint, HttpMethod.POST, entity, Map.class);
            if (Boolean.FALSE.equals(res.getBody().get("allPassed"))) {
                throw new PermissionException(res.getBody());
            }
        } catch (HttpClientErrorException ex) {
            throw new PermissionException(ex.getMessage());
        }
    }
}
