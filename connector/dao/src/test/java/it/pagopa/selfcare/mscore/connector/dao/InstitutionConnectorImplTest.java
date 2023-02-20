package it.pagopa.selfcare.mscore.connector.dao;

import it.pagopa.selfcare.mscore.connector.dao.model.InstitutionEntity;
import it.pagopa.selfcare.mscore.exception.ResourceNotFoundException;
import it.pagopa.selfcare.mscore.model.institution.DataProtectionOfficer;
import it.pagopa.selfcare.mscore.model.institution.Institution;
import it.pagopa.selfcare.mscore.model.institution.Onboarding;
import it.pagopa.selfcare.mscore.model.institution.PaymentServiceProvider;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
class InstitutionConnectorImplTest {

    @InjectMocks
    InstitutionConnectorImpl institutionConnectionImpl;

    @Mock
    InstitutionRepository institutionRepository;

    @Test
    void findById() {
        InstitutionEntity institutionEntity = new InstitutionEntity();
        institutionEntity.setId("507f1f77bcf86cd799439011");
        Optional<Institution> response = institutionConnectionImpl.findByExternalId("id");
        Assertions.assertTrue(response.isEmpty());
    }

    @Test
    void save() {
        Institution institution = new Institution();
        institution.setExternalId("ext");
        institution.setId("507f1f77bcf86cd799439011");
        InstitutionEntity institutionEntity = new InstitutionEntity();
        institutionEntity.setId("507f1f77bcf86cd799439011");
        when(institutionRepository.save(any())).thenReturn(institutionEntity);
        Institution response = institutionConnectionImpl.save(institution);
        Assertions.assertEquals("507f1f77bcf86cd799439011", response.getId());
    }

    @Test
    void save3() {
        Institution institution = new Institution();
        institution.setExternalId("ext");
        InstitutionEntity institutionEntity = new InstitutionEntity();
        institutionEntity.setExternalId("ext");
        when(institutionRepository.save(any())).thenReturn(institutionEntity);
        Institution response = institutionConnectionImpl.save(institution);
        Assertions.assertEquals("ext", response.getExternalId());
    }

    @Test
    void findByExternalIdTest() {
        InstitutionEntity institutionEntity = new InstitutionEntity();
        institutionEntity.setId("507f1f77bcf86cd799439011");
        when(institutionRepository.find(any(), any())).thenReturn(new ArrayList<>());
        Optional<Institution> response = institutionConnectionImpl.findByExternalId("ext");
        Assertions.assertFalse(response.isPresent());
    }

    @Test
    void findByExternalIdNotFoundTest() {
        InstitutionEntity institutionEntity = new InstitutionEntity();
        institutionEntity.setId("507f1f77bcf86cd799439011");
        Optional<Institution> response = institutionConnectionImpl.findByExternalId("ext");
        Assertions.assertTrue(response.isEmpty());
    }

    @Test
    void deleteById() {
        doNothing().when(institutionRepository).deleteById(any());
        Assertions.assertDoesNotThrow(() -> institutionConnectionImpl.deleteById("507f1f77bcf86cd799439011"));
    }

    @Test
    void testFindById() {
        when(institutionRepository.findById(any())).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> institutionConnectionImpl.findById("42"));
    }

    @Test
    void testFindById2() {
        InstitutionEntity institutionEntity = new InstitutionEntity();
        when(institutionRepository.findById(any())).thenReturn(Optional.of(institutionEntity));
        assertNotNull(institutionConnectionImpl.findById("id"));
    }
    @Test
    void testFindAndUpdate() {
        InstitutionEntity institutionEntity = new InstitutionEntity();
        when(institutionRepository.findAndModify(any(),any(),any(),any())).thenReturn(institutionEntity);
        Institution response = institutionConnectionImpl.findAndUpdate("institutionId",new Onboarding(), new ArrayList<>());
        assertNotNull(response);
    }

    @Test
    void testFindWithFilter() {
        when(institutionRepository.find(any(),any())).thenReturn(new ArrayList<>());
        List<Institution> response = institutionConnectionImpl.findWithFilter("externalId","productId",new ArrayList<>());
        assertNotNull(response);
    }

    @Test
    void testFindInstitutionProduct() {
        when(institutionRepository.find(any(),any())).thenReturn(new ArrayList<>());
        assertThrows(ResourceNotFoundException.class, () -> institutionConnectionImpl.findInstitutionProduct("externalId","productId"));
    }

    @Test
    void save2() {
        Institution institution = new Institution();
        institution.setExternalId("ext");
        institution.setId("507f1f77bcf86cd799439011");
        institution.setGeographicTaxonomies(new ArrayList<>());
        institution.setDataProtectionOfficer(new DataProtectionOfficer());
        institution.setPaymentServiceProvider(new PaymentServiceProvider());

        InstitutionEntity institutionEntity = new InstitutionEntity();
        institutionEntity.setId("507f1f77bcf86cd799439011");
        institutionEntity.setGeographicTaxonomies(new ArrayList<>());
        institutionEntity.setDataProtectionOfficer(new DataProtectionOfficer());
        institutionEntity.setPaymentServiceProvider(new PaymentServiceProvider());
        when(institutionRepository.save(any())).thenReturn(institutionEntity);
        Institution response = institutionConnectionImpl.save(institution);
        Assertions.assertEquals("507f1f77bcf86cd799439011", response.getId());
    }
}
