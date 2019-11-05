package org.openmbee.services.sparql.controllers;

import java.util.List;
import java.util.Optional;
import org.openmbee.services.sparql.data.ParsedResult;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/sparql")
public class Query extends Base {

    @GetMapping
    public ResponseEntity<?> get(
            @RequestParam String query,
            @RequestParam(required = false, name = "default-graph-uri") List<String> defaults,
            @RequestParam(required = false, name = "named-graph-uri") List<String> named,
            @RequestHeader(required = false, name = "Accept") String accept,
            @RequestHeader(required = false, name = "Authorization") Optional<String> auth) {

        ParsedResult result = parser.parseQuery(query, defaults, named);
        permission.checkPermissions(result, auth);
        return proxy.sendQuery(query, defaults, named, accept);
    }

    @PostMapping(consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE, params = "query")
    public ResponseEntity<?> postQueryForm(
            @RequestParam String query,
            @RequestParam(required = false, name = "default-graph-uri") List<String> defaults,
            @RequestParam(required = false, name = "named-graph-uri") List<String> named,
            @RequestHeader(required = false, name = "Accept") String accept,
            @RequestHeader(required = false, name = "Authorization") Optional<String> auth) {

        ParsedResult result = parser.parseQuery(query, defaults, named);
        permission.checkPermissions(result, auth);
        return proxy.sendQuery(query, defaults, named, accept);
    }

    @PostMapping(consumes = "application/sparql-query")
    public ResponseEntity<?> postQuery(
            @RequestBody String query,
            @RequestParam(required = false, name = "default-graph-uri") List<String> defaults,
            @RequestParam(required = false, name = "named-graph-uri") List<String> named,
            @RequestHeader(required = false, name = "Accept") String accept,
            @RequestHeader(required = false, name = "Authorization") Optional<String> auth) {

        ParsedResult result = parser.parseQuery(query, defaults, named);
        permission.checkPermissions(result, auth);
        return proxy.sendQuery(query, defaults, named, accept);
    }
}
