package it.pagopa.selfcare.mscore.connector.dao;

import it.pagopa.selfcare.mscore.connector.dao.model.InstitutionEntity;
import it.pagopa.selfcare.mscore.model.institution.DataProtectionOfficer;
import it.pagopa.selfcare.mscore.model.institution.Institution;
import it.pagopa.selfcare.mscore.model.institution.PaymentServiceProvider;
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
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ContextConfiguration(classes = InstitutionConnectorImpl.class)
@ExtendWith(SpringExtension.class)
class InstitutionConnectorImplTest {

    @Autowired
    InstitutionConnectorImpl institutionConnectionImpl;

    @MockBean
    InstitutionRepository institutionRepository;

    @Test
    void findById() {
        InstitutionEntity institutionEntity = new InstitutionEntity();
        institutionEntity.setId(new ObjectId("507f1f77bcf86cd799439011"));
        Optional<Institution> response = institutionConnectionImpl.findByExternalId("id");
        Assertions.assertTrue(response.isEmpty());
    }

    @Test
    void save(){
        Institution institution = new Institution();
        institution.setExternalId("ext");
        institution.setId("507f1f77bcf86cd799439011");
        InstitutionEntity institutionEntity = new InstitutionEntity();
        institutionEntity.setId(new ObjectId("507f1f77bcf86cd799439011"));
        when(institutionRepository.save(any())).thenReturn(institutionEntity);
        Institution response = institutionConnectionImpl.save(institution);
        Assertions.assertEquals("507f1f77bcf86cd799439011", response.getId());
    }

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

    @Test
    void deleteById(){
        doNothing().when(institutionRepository).deleteById(any());
        Assertions.assertDoesNotThrow(() -> institutionConnectionImpl.deleteById("507f1f77bcf86cd799439011"));
    }

    @Test
    void save2(){
        Institution institution = new Institution();
        institution.setExternalId("ext");
        institution.setId("507f1f77bcf86cd799439011");
        institution.setGeographicTaxonomies(new ArrayList<>());
        institution.setDataProtectionOfficer(new DataProtectionOfficer());
        institution.setPaymentServiceProvider(new PaymentServiceProvider());

        InstitutionEntity institutionEntity = new InstitutionEntity();
        institutionEntity.setId(new ObjectId("507f1f77bcf86cd799439011"));
        institutionEntity.setGeographicTaxonomies(new ArrayList<>());
        institutionEntity.setDataProtectionOfficer(new DataProtectionOfficer());
        institutionEntity.setPaymentServiceProvider(new PaymentServiceProvider());
        when(institutionRepository.save(any())).thenReturn(institutionEntity);
        Institution response = institutionConnectionImpl.save(institution);
        Assertions.assertEquals("507f1f77bcf86cd799439011", response.getId());
    }
}
