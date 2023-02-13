package it.pagopa.selfcare.mscore.core;

import it.pagopa.selfcare.mscore.api.InstitutionConnector;
import it.pagopa.selfcare.mscore.model.institution.Institution;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.MissingFormatArgumentException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
class InstitutionServiceImplTest {
    @Mock
    private InstitutionConnector institutionConnector;

    @InjectMocks
    private InstitutionServiceImpl institutionServiceImpl;


    @Test
    void testRetrieveInstitutionById() {
        Optional<Institution> optionalInstitution = Optional.empty();
        when(institutionConnector.findById(any())).thenReturn(optionalInstitution);
        assertThrows(MissingFormatArgumentException.class, () -> institutionServiceImpl.retrieveInstitutionById("42"));
    }

    @Test
    void testRetrieveInstitutionById2() {
        Optional<Institution> optionalInstitution = Optional.of(new Institution());
        when(institutionConnector.findById(any())).thenReturn(optionalInstitution);
        assertNotNull(institutionServiceImpl.retrieveInstitutionById("42"));
    }
}

