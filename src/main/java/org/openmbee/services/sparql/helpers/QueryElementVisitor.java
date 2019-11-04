package org.openmbee.services.sparql.helpers;

import org.apache.commons.lang3.tuple.Pair;
import org.apache.jena.graph.Node;
import org.apache.jena.graph.Node_URI;
import org.apache.jena.query.Query;
import org.apache.jena.sparql.syntax.Element;
import org.apache.jena.sparql.syntax.ElementAssign;
import org.apache.jena.sparql.syntax.ElementBind;
import org.apache.jena.sparql.syntax.ElementData;
import org.apache.jena.sparql.syntax.ElementDataset;
import org.apache.jena.sparql.syntax.ElementExists;
import org.apache.jena.sparql.syntax.ElementFilter;
import org.apache.jena.sparql.syntax.ElementGroup;
import org.apache.jena.sparql.syntax.ElementMinus;
import org.apache.jena.sparql.syntax.ElementNamedGraph;
import org.apache.jena.sparql.syntax.ElementNotExists;
import org.apache.jena.sparql.syntax.ElementOptional;
import org.apache.jena.sparql.syntax.ElementPathBlock;
import org.apache.jena.sparql.syntax.ElementService;
import org.apache.jena.sparql.syntax.ElementSubQuery;
import org.apache.jena.sparql.syntax.ElementTriplesBlock;
import org.apache.jena.sparql.syntax.ElementUnion;
import org.apache.jena.sparql.syntax.ElementVisitor;
import org.openmbee.services.sparql.data.ParsedResult;
import org.openmbee.services.sparql.exceptions.SparqlException;
import org.openmbee.services.sparql.services.Parser;

public class QueryElementVisitor implements ElementVisitor {

    private ParsedResult result;

    private Parser parser;

    private boolean inNamedGraph;

    public QueryElementVisitor(ParsedResult result, Parser parser) {
        this.result = result;
        this.parser = parser;
        inNamedGraph = false;
    }

    @Override
    public void visit(ElementTriplesBlock el) {
    }

    @Override
    public void visit(ElementPathBlock el) {
        if (!result.hasDefaults() && !inNamedGraph) {
            result.addToError("querying default graph not allowed");
            throw new SparqlException(result);
        }
    }

    @Override
    public void visit(ElementFilter el) {
    }

    @Override
    public void visit(ElementAssign el) {
    }

    @Override
    public void visit(ElementBind el) {
    }

    @Override
    public void visit(ElementData el) {
    }

    @Override
    public void visit(ElementUnion el) {
        for (Element e: el.getElements()) {
            e.visit(this);
        }
    }

    @Override
    public void visit(ElementOptional el) {
        el.getOptionalElement().visit(this);
    }

    @Override
    public void visit(ElementGroup el) {
        for (Element e: el.getElements()) {
            e.visit(this);
        }
    }

    @Override
    public void visit(ElementDataset el) {
        el.getElement().visit(this);
    }

    @Override
    public void visit(ElementNamedGraph el) {
        Node n = el.getGraphNameNode();
        if (n instanceof Node_URI) {
            String uri = n.getURI();
            Pair<String, String> pair = parser.parseNamedGraphUri(uri);
            if (pair == null) {
                result.addToError(uri);
                throw new SparqlException(result);
            } else {
                result.addToRead(pair);
            }
        } else if (!result.hasNamed()) {
            //this indicates a graph var but no named graph specified, not allowed
            result.addToError("graph var cannot be used without named graph declared");
            throw new SparqlException(result);
        }
        inNamedGraph = true;
        el.getElement().visit(this);
        inNamedGraph = false;
    }

    @Override
    public void visit(ElementExists el) {
        el.getElement().visit(this);
    }

    @Override
    public void visit(ElementNotExists el) {
        el.getElement().visit(this);
    }

    @Override
    public void visit(ElementMinus el) {
        el.getMinusElement().visit(this);
    }

    @Override
    public void visit(ElementService el) {
        el.getElement().visit(this);
    }

    @Override
    public void visit(ElementSubQuery el) {
        Query q = el.getQuery();

        //TODO ??? make a new visitor instance with new parsed result or reuse this? does current default and named graphs inherit?
    }
}
