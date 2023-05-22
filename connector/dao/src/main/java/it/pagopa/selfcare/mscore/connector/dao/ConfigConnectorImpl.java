package it.pagopa.selfcare.mscore.connector.dao;

import it.pagopa.selfcare.mscore.api.ConfigConnector;
import it.pagopa.selfcare.mscore.connector.dao.model.ConfigEntity;
import it.pagopa.selfcare.mscore.exception.ResourceNotFoundException;
import it.pagopa.selfcare.mscore.model.Config;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

import static it.pagopa.selfcare.mscore.constant.CustomError.CONFIG_NOT_FOUND;

@Slf4j
@Component
public class ConfigConnectorImpl implements ConfigConnector {

    private final ConfigRepository repository;

    public ConfigConnectorImpl(ConfigRepository repository) {
        this.repository = repository;
    }

    @Override
    public Config findById(String id) {
        return repository.findById(id)
                .map(this::convertToConfig)
                .orElseThrow(() -> new ResourceNotFoundException(String.format(CONFIG_NOT_FOUND.getMessage(), id), CONFIG_NOT_FOUND.getCode()));

    }

    @Override
    public void resetConfiguration(String id) {
        Query query = Query.query(Criteria.where(ConfigEntity.Fields.id.name()).is(id));

        Update update = new Update()
                .set(ConfigEntity.Fields.enableKafkaScheduler.name(), false)
                .set(ConfigEntity.Fields.productFilter.name(), "");

        FindAndModifyOptions findAndModifyOptions = FindAndModifyOptions.options().upsert(false).returnNew(true);
        repository.findAndModify(query, update, findAndModifyOptions, ConfigEntity.class);
    }

    private Config convertToConfig(ConfigEntity entity) {
        Config config = new Config();
        if (entity != null) {
            config.setId(entity.getId());
            config.setProductFilter(entity.getProductFilter());
            config.setEnableKafkaScheduler(entity.isEnableKafkaScheduler());
        }
        return config;
    }


}
