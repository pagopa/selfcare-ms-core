package it.pagopa.selfcare.mscore.connector.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.UpdateDefinition;

import java.util.ArrayList;
import java.util.List;
public class MongoCustomConnectorImpl implements MongoCustomConnector {

    private final MongoOperations mongoOperations;

    public MongoCustomConnectorImpl(MongoOperations mongoOperations) {
        this.mongoOperations = mongoOperations;
    }


    @Override
    public <O> boolean exists(Query query, Class<O> outputType) {
        return mongoOperations.exists(query, outputType);
    }

    @Override
    public <O> List<O> find(Query query, Class<O> outputType) {
        return mongoOperations.find(query, outputType);
    }

    @Override
    public <O> Page<O> find(Query query, Pageable pageable, Class<O> outputType) {
        long count = mongoOperations.count(query, outputType);
        List<O> list = new ArrayList<>();
        if (count > 0) {
            list = mongoOperations.find(query.with(pageable), outputType);
        }
        return new PageImpl<>(list, pageable, count);
    }

    @Override
    public <O> O findAndModify(Query query, UpdateDefinition updateDefinition, FindAndModifyOptions findAndModifyOptions, Class<O> outputType) {
            return mongoOperations.findAndModify(query, updateDefinition, findAndModifyOptions, outputType);
    }
}
