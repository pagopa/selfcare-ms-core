package it.pagopa.selfcare.mscore.connector.dao;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
class MongoCustomConnectorImplTest {
    @InjectMocks
    private MongoCustomConnectorImpl mongoCustomConnector;

    @Mock
    private MongoOperations mongoOperations;

    @Test
    void testFind() {
        List<Object> list = new ArrayList<>();
        when(mongoOperations.find(any(), any())).thenReturn(list);
        Query query = new Query();
        assertNotNull(mongoCustomConnector.find(query, Object.class));
    }

    @Test
    void testFind2() {
        List<Object> list = new ArrayList<>();
        when(mongoOperations.find(any(), any())).thenReturn(list);
        Query query = new Query();
        Pageable page = Pageable.ofSize(3);
        assertNotNull(mongoCustomConnector.find(query, page, Object.class));
    }

    @Test
    void testFind3() {
        Object a = new Object();
        List<Object> list = new ArrayList<>();
        list.add(a);
        when(mongoOperations.count(any(), (Class<?>) any())).thenReturn(1L);
        when(mongoOperations.find(any(), any())).thenReturn(list);
        Query query = new Query();
        Pageable page = Pageable.ofSize(3);
        assertNotNull(mongoCustomConnector.find(query, page, Object.class));
    }

}

