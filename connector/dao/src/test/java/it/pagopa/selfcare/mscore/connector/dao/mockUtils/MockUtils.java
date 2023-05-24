package it.pagopa.selfcare.mscore.connector.dao.mockUtils;

import it.pagopa.selfcare.mscore.connector.dao.model.ConfigEntity;

public class MockUtils {

    public static ConfigEntity createConfig() {
        ConfigEntity configEntity = new ConfigEntity();
        configEntity.setId("KafkaScheduler");
        configEntity.setProductFilter("");
        configEntity.setEnableKafkaScheduler(true);
        return configEntity;
    }
}
