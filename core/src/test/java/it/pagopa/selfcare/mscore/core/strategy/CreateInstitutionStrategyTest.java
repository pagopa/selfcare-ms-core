package it.pagopa.selfcare.mscore.core.strategy;


import it.pagopa.selfcare.commons.base.utils.InstitutionType;
import it.pagopa.selfcare.mscore.api.InstitutionConnector;
import it.pagopa.selfcare.mscore.api.PartyRegistryProxyConnector;
import it.pagopa.selfcare.mscore.constant.Origin;
import it.pagopa.selfcare.mscore.core.mapper.InstitutionMapper;
import it.pagopa.selfcare.mscore.core.mapper.InstitutionMapperImpl;
import it.pagopa.selfcare.mscore.core.strategy.factory.CreateInstitutionStrategyFactory;
import it.pagopa.selfcare.mscore.core.strategy.input.CreateInstitutionStrategyInput;
import it.pagopa.selfcare.mscore.core.util.InstitutionPaSubunitType;
import it.pagopa.selfcare.mscore.core.util.TestUtils;
import it.pagopa.selfcare.mscore.exception.MsCoreException;
import it.pagopa.selfcare.mscore.exception.ResourceConflictException;
import it.pagopa.selfcare.mscore.exception.ResourceNotFoundException;
import it.pagopa.selfcare.mscore.model.AreaOrganizzativaOmogenea;
import it.pagopa.selfcare.mscore.model.UnitaOrganizzativa;
import it.pagopa.selfcare.mscore.model.institution.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreateInstitutionStrategyTest {

    @InjectMocks
    CreateInstitutionStrategyFactory strategyFactory;

    @Mock
    InstitutionConnector institutionConnector;
    @Mock
    PartyRegistryProxyConnector partyRegistryProxyConnector;
    @Spy
    InstitutionMapper institutionMapper = new InstitutionMapperImpl();


    private static final InstitutionProxyInfo dummyInstitutionProxyInfo;
    private static final CategoryProxyInfo dummyCategoryProxyInfo;

    private static final AreaOrganizzativaOmogenea dummyAreaOrganizzativaOmogenea;

    static {
        dummyInstitutionProxyInfo = new InstitutionProxyInfo();
        dummyInstitutionProxyInfo.setAddress("42 Main St");
        dummyInstitutionProxyInfo.setAoo("Aoo");
        dummyInstitutionProxyInfo.setCategory("Category");
        dummyInstitutionProxyInfo.setDescription("The characteristics of someone or something");
        dummyInstitutionProxyInfo.setDigitalAddress("42 Main St");
        dummyInstitutionProxyInfo.setId("42");
        dummyInstitutionProxyInfo.setO("foo");
        dummyInstitutionProxyInfo.setOrigin("Origin");
        dummyInstitutionProxyInfo.setOriginId("42");
        dummyInstitutionProxyInfo.setOu("Ou");
        dummyInstitutionProxyInfo.setTaxCode("Tax Code");
        dummyInstitutionProxyInfo.setZipCode("21654");

        dummyCategoryProxyInfo = new CategoryProxyInfo();
        dummyCategoryProxyInfo.setCode("Code");
        dummyCategoryProxyInfo.setKind("Kind");
        dummyCategoryProxyInfo.setName("Name");
        dummyCategoryProxyInfo.setOrigin("Origin");

        dummyAreaOrganizzativaOmogenea = new AreaOrganizzativaOmogenea();
        dummyAreaOrganizzativaOmogenea.setOrigin(Origin.IPA);
        dummyAreaOrganizzativaOmogenea.setDenominazioneAoo("Aoo");
        dummyAreaOrganizzativaOmogenea.setIndirizzo("Address");
        dummyAreaOrganizzativaOmogenea.setCAP("12345");
        dummyAreaOrganizzativaOmogenea.setCodiceFiscaleEnte(dummyInstitutionProxyInfo.getTaxCode());
        dummyAreaOrganizzativaOmogenea.setCodAoo("AOO");
    }

    private UnitaOrganizzativa dummyUnitaOrganizzativa() {

        UnitaOrganizzativa dummyUnitaOrganizzativa = new UnitaOrganizzativa();
        dummyUnitaOrganizzativa.setOrigin(Origin.IPA);
        dummyUnitaOrganizzativa.setDescrizioneUo("Uo");
        dummyUnitaOrganizzativa.setIndirizzo("Address");
        dummyUnitaOrganizzativa.setCAP("12345");
        dummyUnitaOrganizzativa.setCodiceFiscaleEnte(dummyInstitutionProxyInfo.getTaxCode());
        dummyUnitaOrganizzativa.setCodiceUniUo("UO");
        dummyUnitaOrganizzativa.setCodiceUniAoo("AOO");
        return dummyUnitaOrganizzativa;
    }

    @Test
    void shouldThrowExceptionOnCreateInstitutionFromIpaIfAlreadyExists() {

        when(institutionConnector.findByTaxCodeSubunitCode(any(), any()))
                .thenReturn(List.of(new Institution()));
        assertThrows(ResourceConflictException.class, () -> strategyFactory.createInstitutionStrategyIpa()
                .createInstitution(CreateInstitutionStrategyInput.builder().subunitType(InstitutionPaSubunitType.AOO)
                        .build()));

    }

    @Test
    void shouldThrowExceptionOnCreateInstitutionIfAlreadyExists() {

        when(institutionConnector.findByTaxCodeSubunitCode(any(), any()))
                .thenReturn(List.of(new Institution()));
        assertThrows(ResourceConflictException.class, () -> strategyFactory.createInstitutionStrategy(new Institution())
                .createInstitution(CreateInstitutionStrategyInput.builder()
                        .build()));
    }

    /**
     * Method under test: {@link CreateInstitutionStrategy#createInstitution(CreateInstitutionStrategyInput)}
     */
    @Test
    void shouldCreateInstitutionFromAnac() {

        Institution institution = TestUtils.dummyInstitutionSa();
        SaResource saResource = new SaResource();
        saResource.setOriginId("originId");
        saResource.setTaxCode("taxCode");
        saResource.setDescription("desc");
        saResource.setDigitalAddress("test@test.it");
        when(partyRegistryProxyConnector.getSAFromAnac(any())).thenReturn(saResource);
        when(institutionConnector.save(any())).thenReturn(institution);
        //When
        Institution actual = strategyFactory.createInstitutionStrategyAnac(institution)
                .createInstitution(CreateInstitutionStrategyInput.builder()
                        .taxCode(institution.getTaxCode())
                        .build());

        //Then
        assertThat(actual.getDescription()).isEqualTo(institution.getDescription());
        assertThat(actual.getDigitalAddress()).isEqualTo(institution.getDigitalAddress());
        assertThat(actual.getAddress()).isEqualTo(institution.getAddress());
        assertThat(actual.getZipCode()).isEqualTo(institution.getZipCode());
        assertThat(actual.getTaxCode()).isEqualTo(institution.getTaxCode());
        assertThat(actual.getSubunitCode()).isNull();
        assertThat(actual.getSubunitType()).isNull();
        assertThat(actual.getInstitutionType()).isEqualTo(InstitutionType.SA);
        assertThat(actual.getSubunitType()).isNull();

        verify(institutionConnector).save(any());
    }

    /**
     * Method under test: {@link CreateInstitutionStrategy#createInstitution(CreateInstitutionStrategyInput)}
     */
    @Test
    void shouldCreateInstitutionFromIvass() {

        Institution institution = TestUtils.dummyInstitutionAs();
        AsResource asResource = new AsResource();
        asResource.setOriginId("originId");
        asResource.setTaxCode("taxCode");
        asResource.setDescription("desc");
        asResource.setDigitalAddress("test@test.it");
        when(partyRegistryProxyConnector.getASFromIvass(any())).thenReturn(asResource);
        when(institutionConnector.save(any())).thenReturn(institution);
        //When
        Institution actual = strategyFactory.createInstitutionStrategyIvass(institution)
                .createInstitution(CreateInstitutionStrategyInput.builder()
                        .taxCode(institution.getTaxCode())
                        .build());

        //Then
        assertThat(actual.getDescription()).isEqualTo(institution.getDescription());
        assertThat(actual.getDigitalAddress()).isEqualTo(institution.getDigitalAddress());
        assertThat(actual.getAddress()).isEqualTo(institution.getAddress());
        assertThat(actual.getZipCode()).isEqualTo(institution.getZipCode());
        assertThat(actual.getTaxCode()).isEqualTo(institution.getTaxCode());
        assertThat(actual.getSubunitCode()).isNull();
        assertThat(actual.getSubunitType()).isNull();
        assertThat(actual.getInstitutionType()).isEqualTo(InstitutionType.AS);
        assertThat(actual.getSubunitType()).isNull();

        verify(institutionConnector).save(any());
    }

    /**
     * Method under test: {@link CreateInstitutionStrategy#createInstitution(CreateInstitutionStrategyInput)}
     */
    @Test
    void shouldCreateInstitution() {
        //Given
        when(institutionConnector.save(any())).thenAnswer(args -> args.getArguments()[0]);
        when(institutionConnector.findByTaxCodeSubunitCode(anyString(), any()))
                .thenReturn(List.of());

        Institution institution = TestUtils.dummyInstitutionGsp();

        //When
        Institution actual = strategyFactory.createInstitutionStrategy(institution)
                .createInstitution(CreateInstitutionStrategyInput.builder()
                        .taxCode(institution.getTaxCode())
                        .build());

        //Then
        assertThat(actual.getDescription()).isEqualTo(institution.getDescription());
        assertThat(actual.getDigitalAddress()).isEqualTo(institution.getDigitalAddress());
        assertThat(actual.getAddress()).isEqualTo(institution.getAddress());
        assertThat(actual.getZipCode()).isEqualTo(institution.getZipCode());
        assertThat(actual.getTaxCode()).isEqualTo(institution.getTaxCode());
        assertThat(actual.getSubunitCode()).isNull();
        assertThat(actual.getSubunitType()).isNull();
        assertThat(actual.getInstitutionType()).isEqualTo(InstitutionType.GSP);
        assertThat(actual.getSubunitType()).isNull();

        verify(institutionConnector).save(any());
        verify(institutionConnector).findByTaxCodeSubunitCode(anyString(), any());
    }

    /**
     * Method under test: {@link CreateInstitutionStrategy#createInstitution(CreateInstitutionStrategyInput)}
     */
    @Test
    void shouldCreateInstitutionFromIpaAoo() {
        //Given
        when(institutionConnector.save(any())).thenAnswer(args -> args.getArguments()[0]);
        when(institutionConnector.findByTaxCodeSubunitCode(anyString(), anyString()))
                .thenReturn(List.of());

        when(partyRegistryProxyConnector.getCategory(any(), any())).thenReturn(dummyCategoryProxyInfo);
        when(partyRegistryProxyConnector.getInstitutionById(any())).thenReturn(dummyInstitutionProxyInfo);
        when(partyRegistryProxyConnector.getAooById(any())).thenReturn(dummyAreaOrganizzativaOmogenea);

        //When
        Institution actual = strategyFactory.createInstitutionStrategyIpa()
                .createInstitution(CreateInstitutionStrategyInput.builder()
                        .taxCode(dummyAreaOrganizzativaOmogenea.getCodiceFiscaleEnte())
                        .subunitType(InstitutionPaSubunitType.AOO)
                        .subunitCode(dummyAreaOrganizzativaOmogenea.getCodAoo())
                        .build());

        //Then
        assertThat(actual.getOriginId()).isEqualTo(dummyAreaOrganizzativaOmogenea.getId());
        assertThat(actual.getDescription()).isEqualTo(dummyAreaOrganizzativaOmogenea.getDenominazioneAoo());
        assertThat(actual.getDigitalAddress()).isEqualTo(dummyInstitutionProxyInfo.getDigitalAddress());
        assertThat(actual.getAddress()).isEqualTo(dummyAreaOrganizzativaOmogenea.getIndirizzo());
        assertThat(actual.getZipCode()).isEqualTo(dummyAreaOrganizzativaOmogenea.getCAP());
        assertThat(actual.getTaxCode()).isEqualTo(dummyAreaOrganizzativaOmogenea.getCodiceFiscaleEnte());
        assertThat(actual.getSubunitCode()).isEqualTo(dummyAreaOrganizzativaOmogenea.getCodAoo());
        assertThat(actual.getSubunitType()).isEqualTo(InstitutionPaSubunitType.AOO.name());
        assertThat(actual.getParentDescription()).isEqualTo(dummyInstitutionProxyInfo.getDescription());

        verify(institutionConnector, times(2)).save(any());
        verify(institutionConnector).findByTaxCodeSubunitCode(anyString(), anyString());
        verify(partyRegistryProxyConnector).getCategory(any(), any());
        verify(partyRegistryProxyConnector).getInstitutionById(any());
    }

    /**
     * Method under test: {@link CreateInstitutionStrategy#createInstitution(CreateInstitutionStrategyInput)}
     */
    @Test
    void shouldCreateInstitutionFromIpaUo() {

        UnitaOrganizzativa dummyUnitaOrganizzativa = dummyUnitaOrganizzativa();

        when(institutionConnector.save(any())).thenAnswer(args -> args.getArguments()[0]);
        when(institutionConnector.findByTaxCodeSubunitCode(anyString(), anyString()))
                .thenReturn(List.of());

        when(partyRegistryProxyConnector.getCategory(any(), any())).thenReturn(dummyCategoryProxyInfo);
        when(partyRegistryProxyConnector.getInstitutionById(any())).thenReturn(dummyInstitutionProxyInfo);
        when(partyRegistryProxyConnector.getUoById(any())).thenReturn(dummyUnitaOrganizzativa);

       Institution actual = strategyFactory.createInstitutionStrategyIpa()
                .createInstitution(CreateInstitutionStrategyInput.builder()
                        .taxCode(dummyUnitaOrganizzativa.getCodiceFiscaleEnte())
                        .subunitType(InstitutionPaSubunitType.UO)
                        .subunitCode(dummyUnitaOrganizzativa.getCodiceUniUo())
                        .build());

        //Then
        assertThat(actual.getOriginId()).isEqualTo(dummyUnitaOrganizzativa.getId());
        assertThat(actual.getDescription()).isEqualTo(dummyUnitaOrganizzativa.getDescrizioneUo());

        assertThat(actual.getDigitalAddress()).isEqualTo(dummyInstitutionProxyInfo.getDigitalAddress());
        assertThat(actual.getAddress()).isEqualTo(dummyUnitaOrganizzativa.getIndirizzo());
        assertThat(actual.getZipCode()).isEqualTo(dummyUnitaOrganizzativa.getCAP());
        assertThat(actual.getTaxCode()).isEqualTo(dummyUnitaOrganizzativa.getCodiceFiscaleEnte());
        assertThat(actual.getSubunitCode()).isEqualTo(dummyUnitaOrganizzativa.getCodiceUniUo());
        assertThat(actual.getSubunitType()).isEqualTo(InstitutionPaSubunitType.UO.name());
        assertThat(actual.getParentDescription()).isEqualTo(dummyInstitutionProxyInfo.getDescription());
        assertThat(actual.getPaAttributes().getAooParentCode()).isEqualTo(dummyUnitaOrganizzativa.getCodiceUniAoo());

        verify(institutionConnector, times(2)).save(any());
        verify(institutionConnector).findByTaxCodeSubunitCode(anyString(), anyString());
        verify(partyRegistryProxyConnector).getCategory(any(), any());
        verify(partyRegistryProxyConnector).getInstitutionById(any());
    }

    /**
     * Method under test: {@link CreateInstitutionStrategy#createInstitution(CreateInstitutionStrategyInput)}
     */
    @Test
    void shouldCreateInstitutionFromIpaUoRetrievingExistingEc() {

        UnitaOrganizzativa dummyUnitaOrganizzativa = dummyUnitaOrganizzativa();

        when(institutionConnector.save(any())).thenAnswer(args -> args.getArguments()[0]);
        when(institutionConnector.findByTaxCodeSubunitCode(anyString(), anyString()))
                .thenReturn(List.of());

        when(partyRegistryProxyConnector.getCategory(any(), any())).thenReturn(dummyCategoryProxyInfo);
        when(partyRegistryProxyConnector.getInstitutionById(any())).thenReturn(dummyInstitutionProxyInfo);
        when(partyRegistryProxyConnector.getUoById(any())).thenReturn(dummyUnitaOrganizzativa);
        when(institutionConnector.findByExternalId(any())).thenReturn(Optional.of(new Institution()));

        Institution actual = strategyFactory.createInstitutionStrategyIpa()
                .createInstitution(CreateInstitutionStrategyInput.builder()
                        .taxCode(dummyUnitaOrganizzativa.getCodiceFiscaleEnte())
                        .subunitType(InstitutionPaSubunitType.UO)
                        .subunitCode(dummyUnitaOrganizzativa.getCodiceUniUo())
                        .build());

        //Then
        assertThat(actual.getOriginId()).isEqualTo(dummyUnitaOrganizzativa.getId());
        assertThat(actual.getDescription()).isEqualTo(dummyUnitaOrganizzativa.getDescrizioneUo());

        assertThat(actual.getDigitalAddress()).isEqualTo(dummyInstitutionProxyInfo.getDigitalAddress());
        assertThat(actual.getAddress()).isEqualTo(dummyUnitaOrganizzativa.getIndirizzo());
        assertThat(actual.getZipCode()).isEqualTo(dummyUnitaOrganizzativa.getCAP());
        assertThat(actual.getTaxCode()).isEqualTo(dummyUnitaOrganizzativa.getCodiceFiscaleEnte());
        assertThat(actual.getSubunitCode()).isEqualTo(dummyUnitaOrganizzativa.getCodiceUniUo());
        assertThat(actual.getSubunitType()).isEqualTo(InstitutionPaSubunitType.UO.name());
        assertThat(actual.getParentDescription()).isEqualTo(dummyInstitutionProxyInfo.getDescription());
        assertThat(actual.getPaAttributes().getAooParentCode()).isEqualTo(dummyUnitaOrganizzativa.getCodiceUniAoo());

        verify(institutionConnector, times(1)).save(any());
        verify(institutionConnector).findByTaxCodeSubunitCode(anyString(), anyString());
        verify(partyRegistryProxyConnector).getCategory(any(), any());
        verify(partyRegistryProxyConnector).getInstitutionById(any());
    }

    /**
     * Method under test: {@link CreateInstitutionStrategy#createInstitution(CreateInstitutionStrategyInput)}
     */
    @Test
    void shouldGetExistingEC() {

        Institution institutionToReturn = new Institution();
        institutionToReturn.setId("id");
        institutionToReturn.setDescription("test");

        //Given
        when(institutionConnector.findByTaxCodeSubunitCode(anyString(), anyString()))
                .thenReturn(List.of());

        when(partyRegistryProxyConnector.getCategory(any(), any())).thenReturn(dummyCategoryProxyInfo);
        when(partyRegistryProxyConnector.getInstitutionById(any())).thenReturn(dummyInstitutionProxyInfo);
        when(institutionConnector.save(any())).thenReturn(institutionToReturn);

        //When
        Institution actual = strategyFactory.createInstitutionStrategyIpa()
                .createInstitution(CreateInstitutionStrategyInput.builder()
                        .taxCode(dummyAreaOrganizzativaOmogenea.getCodiceFiscaleEnte())
                        .subunitCode(dummyAreaOrganizzativaOmogenea.getCodAoo())
                        .build());

        assertThat(actual.getId()).isEqualTo(institutionToReturn.getId());
        assertThat(actual.getDescription()).isEqualTo(institutionToReturn.getDescription());

        verify(institutionConnector).findByTaxCodeSubunitCode(anyString(), anyString());
        verify(partyRegistryProxyConnector).getCategory(any(), any());
        verify(partyRegistryProxyConnector).getInstitutionById(any());
    }


    /**
     * Method under test: {@link CreateInstitutionStrategy#createInstitution(CreateInstitutionStrategyInput)}
     */
    @Test
    void shouldCreateInstitutionFromIpaUoWhenCodAooEmptyAndTipoMail() {

        UnitaOrganizzativa dummyUnitaOrganizzativa = dummyUnitaOrganizzativa();
        dummyUnitaOrganizzativa.setCodiceUniAoo(null);
        dummyUnitaOrganizzativa.setTipoMail1("Pec");
        dummyUnitaOrganizzativa.setMail1("example@pec.it");

        when(institutionConnector.save(any())).thenAnswer(args -> args.getArguments()[0]);
        when(institutionConnector.findByTaxCodeSubunitCode(anyString(), anyString()))
                .thenReturn(List.of());

        when(partyRegistryProxyConnector.getCategory(any(), any())).thenReturn(dummyCategoryProxyInfo);
        when(partyRegistryProxyConnector.getInstitutionById(any())).thenReturn(dummyInstitutionProxyInfo);
        when(partyRegistryProxyConnector.getUoById(any())).thenReturn(dummyUnitaOrganizzativa);

        Institution actual = strategyFactory.createInstitutionStrategyIpa()
                .createInstitution(CreateInstitutionStrategyInput.builder()
                        .taxCode(dummyUnitaOrganizzativa.getCodiceFiscaleEnte())
                        .subunitType(InstitutionPaSubunitType.UO)
                        .subunitCode(dummyUnitaOrganizzativa.getCodiceUniUo())
                        .build());

        //Then
        assertThat(actual.getOriginId()).isEqualTo(dummyUnitaOrganizzativa.getId());
        assertThat(actual.getDescription()).isEqualTo(dummyUnitaOrganizzativa.getDescrizioneUo());

        assertThat(actual.getDigitalAddress()).isEqualTo(dummyUnitaOrganizzativa.getMail1());
        assertThat(actual.getAddress()).isEqualTo(dummyUnitaOrganizzativa.getIndirizzo());
        assertThat(actual.getZipCode()).isEqualTo(dummyUnitaOrganizzativa.getCAP());
        assertThat(actual.getTaxCode()).isEqualTo(dummyUnitaOrganizzativa.getCodiceFiscaleEnte());
        assertThat(actual.getSubunitCode()).isEqualTo(dummyUnitaOrganizzativa.getCodiceUniUo());
        assertThat(actual.getSubunitType()).isEqualTo(InstitutionPaSubunitType.UO.name());
        assertThat(actual.getPaAttributes()).isNull();

        verify(institutionConnector, times(2)).save(any());
        verify(institutionConnector).findByTaxCodeSubunitCode(anyString(), anyString());
        verify(partyRegistryProxyConnector).getCategory(any(), any());
        verify(partyRegistryProxyConnector).getInstitutionById(any());
    }

    /**
     * Method under test: {@link CreateInstitutionStrategy#createInstitution(CreateInstitutionStrategyInput)}
     */
    @Test
    void shouldCreateInstitutionForECFromIpaWithPda() {

        Institution institutionToReturn = new Institution();
        institutionToReturn.setId("id");
        institutionToReturn.setDescription("test");

        //Given
        when(institutionConnector.findByTaxCodeSubunitCode(any(), any()))
                .thenReturn(List.of());

        when(partyRegistryProxyConnector.getCategory(any(), any())).thenReturn(dummyCategoryProxyInfo);
        when(partyRegistryProxyConnector.getInstitutionById(any())).thenReturn(dummyInstitutionProxyInfo);
        when(institutionConnector.save(any())).thenReturn(institutionToReturn);

        //When
        Institution actual = strategyFactory.createInstitutionStrategyPda("EC")
                .createInstitution(CreateInstitutionStrategyInput.builder()
                        .taxCode("test")
                        .build());

        assertThat(actual.getId()).isEqualTo(institutionToReturn.getId());
        assertThat(actual.getDescription()).isEqualTo(institutionToReturn.getDescription());

        verify(institutionConnector).findByTaxCodeSubunitCode(any(), any());
        verify(partyRegistryProxyConnector).getCategory(any(), any());
        verify(partyRegistryProxyConnector).getInstitutionById(any());
    }

    /**
     * Method under test: {@link CreateInstitutionStrategy#createInstitution(CreateInstitutionStrategyInput)}
     */
    @Test
    void shouldCreateInstitutionForECFromInfocamereWithPda() {

        Institution institutionToReturn = new Institution();
        institutionToReturn.setId("id");
        institutionToReturn.setDescription("test");

        NationalRegistriesProfessionalAddress nationalRegistriesProfessionalAddress = new NationalRegistriesProfessionalAddress();
        nationalRegistriesProfessionalAddress.setZipCode("test");
        nationalRegistriesProfessionalAddress.setAddress("test");

        //Given
        when(institutionConnector.findByTaxCodeSubunitCode(any(), any()))
                .thenReturn(List.of());

        when(partyRegistryProxyConnector.getInstitutionById(any())).thenThrow(new MsCoreException("NOT_FOUND", "404"));
        when(partyRegistryProxyConnector.getLegalAddress(any())).thenReturn(nationalRegistriesProfessionalAddress);
        when(institutionConnector.save(any())).thenReturn(institutionToReturn);

        //When
        Institution actual = strategyFactory.createInstitutionStrategyPda("EC")
                .createInstitution(CreateInstitutionStrategyInput.builder()
                        .taxCode("test")
                        .build());

        assertThat(actual.getId()).isEqualTo(institutionToReturn.getId());
        assertThat(actual.getDescription()).isEqualTo(institutionToReturn.getDescription());

        verify(institutionConnector).findByTaxCodeSubunitCode(any(), any());
        verify(partyRegistryProxyConnector).getInstitutionById(any());
        verify(partyRegistryProxyConnector).getLegalAddress(any());
    }

    /**
     * Method under test: {@link CreateInstitutionStrategy#createInstitution(CreateInstitutionStrategyInput)}
     */
    @Test
    void shouldThrowErrorWhenInstitutionDoesNotExistsOnIpaAndInfocamere() {

        Institution institutionToReturn = new Institution();
        institutionToReturn.setId("id");
        institutionToReturn.setDescription("test");

        NationalRegistriesProfessionalAddress nationalRegistriesProfessionalAddress = new NationalRegistriesProfessionalAddress();
        nationalRegistriesProfessionalAddress.setZipCode("test");
        nationalRegistriesProfessionalAddress.setAddress("test");

        //Given
        when(institutionConnector.findByTaxCodeSubunitCode(any(), any()))
                .thenReturn(List.of());

        when(partyRegistryProxyConnector.getInstitutionById(any())).thenThrow(new MsCoreException("NOT_FOUND", "404"));
        when(partyRegistryProxyConnector.getLegalAddress(any())).thenThrow(new MsCoreException("NOT_FOUND", "404"));

        //When
        assertThrows(ResourceNotFoundException.class, () -> strategyFactory.createInstitutionStrategyPda("EC")
                .createInstitution(CreateInstitutionStrategyInput.builder()
                        .taxCode("test")
                        .build()));
        verify(institutionConnector).findByTaxCodeSubunitCode(any(), any());
        verify(partyRegistryProxyConnector).getInstitutionById(any());
        verify(partyRegistryProxyConnector).getLegalAddress(any());
    }
}
