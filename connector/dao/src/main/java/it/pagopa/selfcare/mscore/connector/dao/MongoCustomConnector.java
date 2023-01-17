package it.pagopa.selfcare.mscore.connector.dao;

import org.springframework.data.mongodb.core.query.Query;

import java.util.List;

public interface MongoCustomConnector {

    <O> List<O> find(Query query, Class<O> outputType);

}
