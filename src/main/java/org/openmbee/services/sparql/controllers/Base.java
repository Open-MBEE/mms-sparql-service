package org.openmbee.services.sparql.controllers;

import java.util.Optional;
import org.apache.commons.lang3.tuple.Pair;
import org.openmbee.services.sparql.data.ParsedResult;
import org.openmbee.services.sparql.services.Parser;
import org.openmbee.services.sparql.services.Permission;
import org.openmbee.services.sparql.services.Proxy;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class Base {

    protected Parser parser;
    protected Permission permission;
    protected Proxy proxy;

    @Autowired
    public void setParser(Parser parser) {
        this.parser = parser;
    }

    @Autowired
    public void setPermission(Permission permission) {
        this.permission = permission;
    }

    @Autowired
    public void setProxy(Proxy proxy) {
        this.proxy = proxy;
    }

    protected void checkPermissions(ParsedResult result, Optional<String> auth){
        for (Pair<String, String> toRead: result.getToRead()) {

        }
        for (Pair<String, String> toModify: result.getToModify()) {

        }
    }
}
