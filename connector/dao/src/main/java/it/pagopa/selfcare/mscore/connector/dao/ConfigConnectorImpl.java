package it.pagopa.selfcare.mscore.connector.dao;

import it.pagopa.selfcare.mscore.api.ConfigConnector;
import it.pagopa.selfcare.mscore.connector.dao.model.ConfigEntity;
import it.pagopa.selfcare.mscore.connector.dao.model.mapper.ConfigMapper;
import it.pagopa.selfcare.mscore.model.Config;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;

@Slf4j
@Component
public class ConfigConnectorImpl implements ConfigConnector {

    private final ConfigRepository repository;

    public ConfigConnectorImpl(ConfigRepository repository) {
        this.repository = repository;
    }

    @Override
    public Config findAndUpdate(String id) {
        Query query = Query.query(Criteria.where(ConfigEntity.Fields.id.name()).is(id)
                .and(ConfigEntity.Fields.enableKafkaScheduler.name()).is(true));

        Update update = new Update()
                .set(ConfigEntity.Fields.enableKafkaScheduler.name(), false)
                .set(ConfigEntity.Fields.productFilter.name(), "")
                .set(ConfigEntity.Fields.lastRequestDate.name(), OffsetDateTime.now());

        FindAndModifyOptions findAndModifyOptions = FindAndModifyOptions.options().upsert(false).returnNew(true);
        ConfigEntity result = repository.findAndModify(query, update, findAndModifyOptions, ConfigEntity.class);
        return ConfigMapper.convertToConfig(result);
    }

}
