package it.pagopa.selfcare.mscore.connector.dao.model.mapper;

import it.pagopa.selfcare.mscore.connector.dao.model.ConfigEntity;
import it.pagopa.selfcare.mscore.model.Config;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.NONE)
public class ConfigMapper {

    public static Config convertToConfig(ConfigEntity entity) {
        Config config = null;
        if (entity != null) {
            config = new Config();
            config.setId(entity.getId());
            config.setProductFilter(entity.getProductFilter());
            config.setEnableKafkaScheduler(entity.isEnableKafkaScheduler());
        }
        return config;
    }

}
