package org.openmbee.services.sparql.services;

import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Component
public class Proxy {

    @Value("${sparql.endpoint}")
    private String sparql;

    public ResponseEntity<?> sendQuery(String query, List<String> defaults, List<String> named, String accept) {
        RestTemplate r = new RestTemplate();

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(sparql);
        if (defaults != null && !defaults.isEmpty()) {
            builder.queryParam("default-graph-uri", defaults);
        }
        if (named != null && !named.isEmpty()) {
            builder.queryParam("named-graph-uri", named);
        }
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/sparql-query");
        if (accept != null) {
            headers.set("Accept", accept);
        }
        HttpEntity<String> entity = new HttpEntity<>(query, headers);

        return r.exchange(builder.toUriString(), HttpMethod.POST, entity, String.class);
    }

    public ResponseEntity<?> sendUpdate(String update, List<String> using, List<String> named, String accept) {
        RestTemplate r = new RestTemplate();

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(sparql);
        if (using != null && !using.isEmpty()) {
            builder.queryParam("using-graph-uri", using);
        }
        if (named != null && !named.isEmpty()) {
            builder.queryParam("using-named-graph-uri", named);
        }

        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/sparql-update");
        if (accept != null) {
            headers.set("Accept", accept);
        }
        HttpEntity<String> entity = new HttpEntity<>(update, headers);

        return r.exchange(builder.toUriString(), HttpMethod.POST, entity, String.class);
    }
}
