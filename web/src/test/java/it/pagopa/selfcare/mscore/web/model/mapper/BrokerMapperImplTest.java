package it.pagopa.selfcare.mscore.web.model.mapper;

import it.pagopa.selfcare.mscore.model.institution.Institution;
import it.pagopa.selfcare.mscore.web.model.institution.BrokerResponse;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class BrokerMapperImplTest {

    @Test
    void toBroker() {
        BrokerMapperImpl mapper = new BrokerMapperImpl();
        Institution institution = new Institution();
        institution.setId("id");
        institution.setDescription("description");
        BrokerResponse broker = mapper.toBroker(institution);
        assertNotNull(broker);
        assertEquals(broker.getId(), institution.getId());
        assertEquals(broker.getDescription(), institution.getDescription());
    }

    @Test
    void toBrokers() {
        BrokerMapperImpl mapper = new BrokerMapperImpl();
        Institution institution = new Institution();
        institution.setId("id");
        institution.setDescription("description");
        List<BrokerResponse> brokers = mapper.toBrokers(List.of(institution));
        assertNotNull(brokers);
        assertEquals(1, brokers.size());
        assertNotNull(brokers.get(0));
        assertEquals(brokers.get(0).getId(), institution.getId());
        assertEquals(brokers.get(0).getDescription(), institution.getDescription());
    }
}
