package it.pagopa.selfcare.mscore.connector.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.UpdateDefinition;

import java.util.List;

public interface MongoCustomConnector {

    <O> List<O> find(Query query, Class<O> outputType);

    <O> Page<O> find(Query query, Pageable pageable, Class<O> outputType);

    <O> O findAndModify(Query query, UpdateDefinition updateDefinition, FindAndModifyOptions findAndModifyOptions, Class<O> outputType);

}
