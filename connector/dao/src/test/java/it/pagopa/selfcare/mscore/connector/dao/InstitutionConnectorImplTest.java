package it.pagopa.selfcare.mscore.connector.dao;

import it.pagopa.selfcare.mscore.connector.dao.model.InstitutionEntity;
import it.pagopa.selfcare.mscore.connector.dao.model.UserEntity;
import it.pagopa.selfcare.mscore.model.OnboardedUser;
import it.pagopa.selfcare.mscore.model.institution.Institution;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Example;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ContextConfiguration(classes = InstitutionConnectorImpl.class)
@ExtendWith(SpringExtension.class)
class InstitutionConnectorImplTest {

    @Autowired
    InstitutionConnectorImpl institutionConnectionImpl;

    @MockBean
    InstitutionRepository institutionRepository;

    @Test
    void findAllTest() {
        Institution institution = new Institution();
        institution.setExternalId("ext");
        InstitutionEntity institutionEntity = new InstitutionEntity();
        institutionEntity.setId(new ObjectId("507f1f77bcf86cd799439011"));
        when(institutionRepository.findAll((Example<InstitutionEntity>) any())).thenReturn(List.of(institutionEntity));
        List<Institution> response = institutionConnectionImpl.findAll(institution);
        Assertions.assertEquals(1, response.size());
        Assertions.assertEquals("507f1f77bcf86cd799439011", response.get(0).getId());
    }

    @Test
    void findByExternalIdTest() {
        InstitutionEntity institutionEntity = new InstitutionEntity();
        institutionEntity.setId(new ObjectId("507f1f77bcf86cd799439011"));
        when(institutionRepository.findAll((Example<InstitutionEntity>) any())).thenReturn(List.of(institutionEntity));
        Optional<Institution> response = institutionConnectionImpl.findByExternalId("ext");
        Assertions.assertTrue(response.isPresent());
        Assertions.assertEquals("507f1f77bcf86cd799439011", response.get().getId());
    }

    @Test
    void findByExternalIdNotFoundTest() {
        InstitutionEntity institutionEntity = new InstitutionEntity();
        institutionEntity.setId(new ObjectId("507f1f77bcf86cd799439011"));
        when(institutionRepository.findAll((Example<InstitutionEntity>) any())).thenReturn(Collections.emptyList());
        Optional<Institution> response = institutionConnectionImpl.findByExternalId("ext");
        Assertions.assertTrue(response.isEmpty());
    }
}
