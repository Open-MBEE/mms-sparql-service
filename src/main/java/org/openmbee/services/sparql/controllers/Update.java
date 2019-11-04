package org.openmbee.services.sparql.controllers;

import java.util.List;
import java.util.Optional;
import org.openmbee.services.sparql.data.ParsedResult;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/sparql")
public class Update extends Base {

    @PostMapping(consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE, params =  "update")
    public ResponseEntity<?> postForm(
            @RequestParam String update,
            @RequestParam(required = false, name = "using-graph-uri") List<String> using,
            @RequestParam(required = false, name = "using-named-graph-uri") List<String> named,
            @RequestHeader(required = false, name = "Authorization") Optional<String> auth) {

        ParsedResult result = parser.parseUpdate(update, using, named);
        permission.checkPermissions(result, auth);
        proxy.sendUpdate(update, using, named);
        return null;
    }

    @PostMapping(consumes = "application/sparql-update")
    public ResponseEntity<?> post(
            @RequestBody String update,
            @RequestParam(required = false, name = "using-graph-uri") List<String> using,
            @RequestParam(required = false, name = "using-named-graph-uri") List<String> named,
            @RequestHeader(required = false, name = "Authorization") Optional<String> auth) {

        ParsedResult result = parser.parseUpdate(update, using, named);
        permission.checkPermissions(result, auth);
        proxy.sendUpdate(update, using, named);
        return null;
    }
}
