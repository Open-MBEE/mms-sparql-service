# SPARQL Service

This is a service that wraps any SPARQL endpoint and adds authorization checks to SPARQL queries or updates, based on MMS projectId and refId extracted from graph uris.

This operates on the precondition that for each project/ref combination in the MMS, there's a named graph whose URI includes the projectId and refId.

## Criteria for accepted SPARQL queries

- for all graph uris specified, they must conform to the configured uri pattern where projectId and refId can be extracted, and the user must have corresponding access to the ref (read or write)

### Query

- the default graph (FROM) must be specified as a union of one or more named graphs whose uri includes MMS projectId and refId (pattern is configurable)
- named graphs (FROM NAMED) must be specified if a "GRAPH ?g" is present in the query
- direct reference to graph uri is also accepted (ex. GRAPH <https://mms.openmbee.org/data/projectId/refId)
- default and named graphs can be specified as query params as specified by the SPARQL protocol spec

### Update

- updates to default graph are not allowed, must specify a named graph with "WITH", or "GRAPH <...>"
- WHERE queries must have "USING", "USING NAMED", or "WITH" to indicate which graph(s) to query (similar criteria to Query)
- DROP, CREATE, LOAD, MOVE operations are not supported
- ADD, COPY, CLEAR operations must operate on graph uris, not ALL or DEFAULT or NAMED

## Resources

- [SPARQL 1.1 Protocol](https://www.w3.org/TR/sparql11-protocol/)
- [SPARQL 1.1 Update](https://www.w3.org/TR/2013/REC-sparql11-update-20130321/)
- [SPARQL 1.1 Query](https://www.w3.org/TR/2013/REC-sparql11-query-20130321/)
- [Jena Query Parser](https://jena.apache.org/documentation/javadoc/arq/org/apache/jena/query/QueryFactory.html)
- [Jena Update Parser](https://jena.apache.org/documentation/javadoc/arq/org/apache/jena/update/UpdateFactory.html)
- [Spring Boot](https://spring.io/projects/spring-boot)