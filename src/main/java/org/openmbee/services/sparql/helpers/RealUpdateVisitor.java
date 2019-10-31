package org.openmbee.services.sparql.helpers;

import java.util.List;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.jena.atlas.lib.Sink;
import org.apache.jena.graph.Node_URI;
import org.apache.jena.sparql.core.Quad;
import org.apache.jena.sparql.modify.request.Target;
import org.apache.jena.sparql.modify.request.UpdateAdd;
import org.apache.jena.sparql.modify.request.UpdateBinaryOp;
import org.apache.jena.sparql.modify.request.UpdateClear;
import org.apache.jena.sparql.modify.request.UpdateCopy;
import org.apache.jena.sparql.modify.request.UpdateCreate;
import org.apache.jena.sparql.modify.request.UpdateData;
import org.apache.jena.sparql.modify.request.UpdateDataDelete;
import org.apache.jena.sparql.modify.request.UpdateDataInsert;
import org.apache.jena.sparql.modify.request.UpdateDeleteWhere;
import org.apache.jena.sparql.modify.request.UpdateDrop;
import org.apache.jena.sparql.modify.request.UpdateLoad;
import org.apache.jena.sparql.modify.request.UpdateModify;
import org.apache.jena.sparql.modify.request.UpdateMove;
import org.apache.jena.sparql.modify.request.UpdateVisitor;
import org.openmbee.services.sparql.data.ParsedResult;
import org.openmbee.services.sparql.exceptions.SparqlException;
import org.openmbee.services.sparql.services.Parser;

public class RealUpdateVisitor implements UpdateVisitor {

    private ParsedResult result;

    private Parser parser;

    public RealUpdateVisitor(ParsedResult result, Parser parser) {
        this.result = result;
        this.parser = parser;
    }

    @Override
    public void visit(UpdateDrop update) {
        result.setUnsupported(true);
        throw new SparqlException(result);
    }

    @Override
    public void visit(UpdateClear update) {
        if (!update.isOneGraph()) {
            result.setUnsupported(true);
            throw new SparqlException(result);
        }
        Pair<String, String> dest = parser.parseNamedGraphUri(update.getGraph().getURI());
        if (dest == null) {
            result.addToError(update.getGraph().getURI());
            throw new SparqlException(result);
        }
        result.addToModify(dest);
    }

    @Override
    public void visit(UpdateCreate update) {
        result.setUnsupported(true);
        throw new SparqlException(result);
    }

    @Override
    public void visit(UpdateLoad update) {
        result.setUnsupported(true);
        throw new SparqlException(result);
    }

    @Override
    public void visit(UpdateAdd update) {
        handleBinaryOp(update);
    }

    @Override
    public void visit(UpdateCopy update) {
        handleBinaryOp(update);
    }

    @Override
    public void visit(UpdateMove update) {
        result.setUnsupported(true);
        throw new SparqlException(result);
    }

    @Override
    public void visit(UpdateDataInsert update) {
        handleUpdateData(update);
    }

    @Override
    public void visit(UpdateDataDelete update) {
        handleUpdateData(update);
    }

    @Override
    public void visit(UpdateDeleteWhere update) {
        for (Quad q: update.getQuads()) {
            if (!(q.getGraph() instanceof Node_URI)) {
                result.addToError("no graph specified in delete where");
                throw new SparqlException(result);
            }
            Pair<String, String> uri = parser.parseNamedGraphUri(q.getGraph().getURI());
            if (uri == null) {
                result.addToError(q.getGraph().getURI());
                throw new SparqlException(result);
            }
            result.addToModify(uri);
        }
    }

    @Override
    public void visit(UpdateModify update) {
        ParsedResult whereClauseResult = new ParsedResult();
        boolean hasWith = false;
        if (update.getWithIRI() != null) {
            if (!(update.getWithIRI() instanceof Node_URI)) {
                result.addToError(update.getWithIRI().toString() + " not supported");
                throw new SparqlException(result);
            }
            Pair<String, String> uri = parser.parseNamedGraphUri(update.getWithIRI().getURI());
            if (uri == null) {
                result.addToError(update.getWithIRI().getURI());
                throw new SparqlException(result);
            }
            result.addToModify(uri);
            whereClauseResult.addToRead(uri);
            hasWith = true;
        }
        whereClauseResult.addAllToNamed(result.getNamed());
        whereClauseResult.addAllToRead(result.getToRead());
        parser.handleGraphUriNodes(whereClauseResult, update.getUsing(), false);
        parser.handleGraphUriNodes(whereClauseResult, update.getUsingNamed(), true);
        update.getWherePattern().visit(new QueryElementVisitor(whereClauseResult, parser));
        result.addAllToRead(whereClauseResult.getToRead());
        handleQuads(update.getDeleteQuads(), hasWith);
        handleQuads(update.getInsertQuads(), hasWith);
    }

    private void handleUpdateData(UpdateData op) {
        handleQuads(op.getQuads(), false);
    }

    private void handleQuads(List<Quad> quads, boolean hasWith) {
        if (quads == null) {
            return;
        }
        for (Quad q: quads) {
            if (!(q.getGraph() instanceof Node_URI) && !hasWith) {
                result.addToError("no graph specified in insert or delete");
                throw new SparqlException(result);
            }
            Pair<String, String> uri = parser.parseNamedGraphUri(q.getGraph().getURI());
            if (uri != null) {
                result.addToModify(uri);
                continue;
            }
            if (hasWith && "urn:x-arq:DefaultGraphNode".equals(q.getGraph().getURI())) {
                continue;
            }
            result.addToError(q.getGraph().getURI());
            throw new SparqlException(result);
        }
    }

    private void handleBinaryOp(UpdateBinaryOp op) {
        Target src = op.getSrc();
        Target dest = op.getDest();
        if (!src.isOneNamedGraph() || !dest.isOneNamedGraph()) {
            result.setUnsupported(true);
            throw new SparqlException(result);
        }
        Pair<String, String> srcPair = parser.parseNamedGraphUri(src.getGraph().getURI());
        if (srcPair == null) {
            result.addToError(src.getGraph().getURI());
            throw new SparqlException(result);
        }
        result.addToRead(srcPair);
        Pair<String, String> destPair = parser.parseNamedGraphUri(dest.getGraph().getURI());
        if (destPair == null) {
            result.addToError(dest.getGraph().getURI());
            throw new SparqlException(result);
        }
        result.addToModify(destPair);
    }

    @Override
    public Sink<Quad> createInsertDataSink() {
        return null;
    }

    @Override
    public Sink<Quad> createDeleteDataSink() {
        return null;
    }
}
