package org.openmbee.services.sparql.services;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.jena.graph.Node;
import org.apache.jena.graph.Node_URI;
import org.apache.jena.query.Query;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.sparql.syntax.Element;
import org.apache.jena.update.Update;
import org.apache.jena.update.UpdateFactory;
import org.apache.jena.update.UpdateRequest;
import org.openmbee.services.sparql.data.ParsedResult;
import org.openmbee.services.sparql.exceptions.SparqlException;
import org.openmbee.services.sparql.helpers.QueryElementVisitor;
import org.openmbee.services.sparql.helpers.RealUpdateVisitor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class Parser {

    private Pattern p;

    public Parser(@Value("${uri.pattern}") String pattern) {
        this.p = Pattern.compile(pattern);
    }

    public ParsedResult parseQuery(String query, List<String> defaults, List<String> named) {
        ParsedResult res = new ParsedResult();
        handleGraphUris(res, defaults, false);
        handleGraphUris(res, named, true);
        Query q = QueryFactory.create(query);
        handleGraphUris(res, q.getGraphURIs(), false);
        handleGraphUris(res, q.getNamedGraphURIs(), true);
        Element e = q.getQueryPattern();
        e.visit(new QueryElementVisitor(res, this));
        if (res.getToRead().isEmpty()) {
            res.addToError("no graph(s) specified");
            throw new SparqlException(res);
        }
        return res;
    }

    public ParsedResult parseUpdate(String update, List<String> using, List<String> usingNamed) {
        ParsedResult res = new ParsedResult();

        ParsedResult holder = new ParsedResult();
        handleGraphUris(holder, using, false);
        handleGraphUris(holder, usingNamed, true);
        if (holder.hasError()) {
            throw new SparqlException(holder);
        }
        UpdateRequest u = UpdateFactory.create(update);
        for (Update op: u.getOperations()) {
            ParsedResult singleUpdate = new ParsedResult();
            singleUpdate.addAllToNamed(holder.getNamed());
            singleUpdate.addAllToDefaults(holder.getDefaults());
            RealUpdateVisitor v = new RealUpdateVisitor(singleUpdate, this);
            op.visit(v);
            res.addAllToRead(singleUpdate.getToRead());
            res.addAllToModify(singleUpdate.getToModify());
        }
        res.addAllToRead(holder.getToRead());
        return res;
    }

    public Pair<String, String> parseNamedGraphUri(String uri) {
        if (uri == null) {
            return null;
        }
        Matcher m = p.matcher(uri);
        if (m.matches()) {
            return Pair.of(m.group("projectId"), m.group("refId"));
        }
        return null;
    }

    public void handleGraphUris(ParsedResult res, List<String> uris, boolean named) {
        if (uris == null) {
            return;
        }
        for (String uri: uris) {
            handleUri(res, uri, named);
        }
    }

    public void handleGraphUriNodes(ParsedResult res, List<Node> uris, boolean named) {
        if (uris == null) {
            return;
        }
        for (Node uri: uris) {
            if (!(uri instanceof Node_URI)) {
                res.addToError(uri.toString());
                throw new SparqlException(res);
            }
            handleUri(res, uri.getURI(), named);
        }
    }

    private void handleUri(ParsedResult res, String uri, boolean named) {
        Pair<String, String> pair = parseNamedGraphUri(uri);
        if (pair == null) {
            res.addToError(uri);
            throw new SparqlException(res);
        }
        res.addToRead(pair);
        if (named) {
            res.addToNamed(pair);
        } else {
            res.addToDefaults(pair);
        }
    }
}
