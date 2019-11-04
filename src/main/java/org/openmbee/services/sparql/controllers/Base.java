package org.openmbee.services.sparql.controllers;

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

}
