package it.pagopa.selfcare.mscore.connector.dao.config;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.mockito.Mockito.mock;

@ExtendWith(SpringExtension.class)
class DaoConfigTest {
    @InjectMocks
    private DaoConfig daoConfig;

    @Mock
    private MongoDatabaseFactory mongoDatabaseFactory;


    @Test
    void testCustomConversions() {
        daoConfig.customConversions();
    }

    @Test
    void testTransactionManager() {
        MongoDatabaseFactory mongoDatabaseFactory = mock(MongoDatabaseFactory.class);
        daoConfig.transactionManager(mongoDatabaseFactory);
    }

}

