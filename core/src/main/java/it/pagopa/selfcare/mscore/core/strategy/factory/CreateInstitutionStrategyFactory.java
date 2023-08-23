package it.pagopa.selfcare.mscore.core.strategy.factory;

import it.pagopa.selfcare.mscore.api.InstitutionConnector;
import it.pagopa.selfcare.mscore.api.PartyRegistryProxyConnector;
import it.pagopa.selfcare.mscore.constant.CustomError;
import it.pagopa.selfcare.mscore.constant.Origin;
import it.pagopa.selfcare.mscore.core.mapper.InstitutionMapper;
import it.pagopa.selfcare.mscore.core.strategy.CreateInstitutionStrategy;
import it.pagopa.selfcare.mscore.core.strategy.input.CreateInstitutionStrategyInput;
import it.pagopa.selfcare.mscore.core.util.InstitutionPaSubunitType;
import it.pagopa.selfcare.mscore.exception.ResourceConflictException;
import it.pagopa.selfcare.mscore.model.AreaOrganizzativaOmogenea;
import it.pagopa.selfcare.mscore.model.UnitaOrganizzativa;
import it.pagopa.selfcare.mscore.model.institution.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

@Component
public class CreateInstitutionStrategyFactory {

    private static final String TYPE_MAIL_PEC = "Pec";

    private final InstitutionConnector institutionConnector;
    private final PartyRegistryProxyConnector partyRegistryProxyConnector;

    private final InstitutionMapper institutionMapper;

    private final Function<CreateInstitutionStrategyInput, String> createExternalId = strategyInput -> Objects.isNull(strategyInput.getSubunitCode())
            ? strategyInput.getTaxCode()
            : String.format("%s_%s", strategyInput.getTaxCode(), strategyInput.getSubunitCode());

    public CreateInstitutionStrategyFactory(InstitutionConnector institutionConnector, PartyRegistryProxyConnector partyRegistryProxyConnector, InstitutionMapper institutionMapper) {
        this.institutionConnector = institutionConnector;
        this.partyRegistryProxyConnector = partyRegistryProxyConnector;
        this.institutionMapper = institutionMapper;
    }


    public CreateInstitutionStrategy createInstitutionStrategy(Institution institution) {

        Consumer<CreateInstitutionStrategyInput> checkIfAlreadyExists = checkIfAlreadyExistsByTaxCodeAndSubunitCode();

        Function<CreateInstitutionStrategyInput, Institution> mappingToInstitution = strategyInput -> {

            institution.setExternalId(createExternalId.apply(strategyInput));
            institution.setOrigin(Origin.SELC.getValue());
            institution.setOriginId("SELC_" + institution.getExternalId());
            institution.setCreatedAt(OffsetDateTime.now());
            return institution;
        };

        return new CreateInstitutionStrategy(institutionConnector, checkIfAlreadyExists, mappingToInstitution);

    }
    public CreateInstitutionStrategy createInstitutionStrategy(InstitutionPaSubunitType subunitType) {

        Consumer<CreateInstitutionStrategyInput> checkIfAlreadyExists = checkIfAlreadyExistsByTaxCodeAndSubunitCode();
        Function<CreateInstitutionStrategyInput, Institution> mappingToInstitution = null;

        if(Objects.isNull(subunitType) || InstitutionPaSubunitType.EC.equals(subunitType)) {
            mappingToInstitution = mappingToInstitutionIPAEc();
        } else if(InstitutionPaSubunitType.AOO.equals(subunitType)) {
            mappingToInstitution = mappingToInstitutionIPAAoo();
        } else {
            mappingToInstitution = mappingToInstitutionIPAUo();
        }

        return new CreateInstitutionStrategy(institutionConnector, checkIfAlreadyExists, mappingToInstitution);

    }

    private Consumer<CreateInstitutionStrategyInput> checkIfAlreadyExistsByTaxCodeAndSubunitCode() {
        return strategyInput -> {

            List<Institution> institutions = institutionConnector.findByTaxCodeAndSubunitCode(strategyInput.getTaxCode(), strategyInput.getSubunitCode());
            if (!institutions.isEmpty())
                throw new ResourceConflictException(String
                        .format(CustomError.CREATE_INSTITUTION_IPA_CONFLICT.getMessage(), strategyInput.getTaxCode(), strategyInput.getSubunitCode()),
                        CustomError.CREATE_INSTITUTION_CONFLICT.getCode());
        };
    }

    private Function<CreateInstitutionStrategyInput, Institution> mappingToInstitutionIPAEc() {
        return strategyInput -> {

            InstitutionProxyInfo institutionProxyInfo = partyRegistryProxyConnector.getInstitutionById(strategyInput.getTaxCode());

            CategoryProxyInfo categoryProxyInfo = partyRegistryProxyConnector.getCategory(institutionProxyInfo.getOrigin(), institutionProxyInfo.getCategory());

            Institution newInstitution = institutionMapper.fromInstitutionProxyInfo(institutionProxyInfo);

            newInstitution.setSubunitType(InstitutionPaSubunitType.EC.name());
            newInstitution.setExternalId(createExternalId.apply(strategyInput));
            newInstitution.setOrigin(Origin.IPA.getValue());
            newInstitution.setCreatedAt(OffsetDateTime.now());

            Attributes attributes = new Attributes();
            attributes.setOrigin(categoryProxyInfo.getOrigin());
            attributes.setCode(categoryProxyInfo.getCode());
            attributes.setDescription(categoryProxyInfo.getName());
            newInstitution.setAttributes(List.of(attributes));

            return newInstitution;
        };
    }

    private Function<CreateInstitutionStrategyInput, Institution> mappingToInstitutionIPAAoo() {
        return strategyInput -> {

            InstitutionProxyInfo institutionProxyInfo = partyRegistryProxyConnector.getInstitutionById(strategyInput.getTaxCode());

            CategoryProxyInfo categoryProxyInfo = partyRegistryProxyConnector.getCategory(institutionProxyInfo.getOrigin(), institutionProxyInfo.getCategory());


            AreaOrganizzativaOmogenea areaOrganizzativaOmogenea = partyRegistryProxyConnector.getAooById(strategyInput.getSubunitCode());

            Institution newInstitution = new Institution();

            newInstitution.setOriginId( areaOrganizzativaOmogenea.getId() );
            newInstitution.setDescription( areaOrganizzativaOmogenea.getDenominazioneAoo() );
            newInstitution.setDigitalAddress( TYPE_MAIL_PEC.equals(areaOrganizzativaOmogenea.getTipoMail1())
                    ? areaOrganizzativaOmogenea.getMail1() : institutionProxyInfo.getDigitalAddress());
            newInstitution.setAddress( areaOrganizzativaOmogenea.getIndirizzo() );
            newInstitution.setZipCode( areaOrganizzativaOmogenea.getCAP() );
            newInstitution.setTaxCode( areaOrganizzativaOmogenea.getCodiceFiscaleEnte() );
            newInstitution.setSubunitCode(strategyInput.getSubunitCode());
            newInstitution.setSubunitType(InstitutionPaSubunitType.AOO.name());
            newInstitution.setParentDescription(institutionProxyInfo.getDescription());
            newInstitution.setRootParentId(institutionProxyInfo.getId());

            newInstitution.setExternalId(createExternalId.apply(strategyInput));
            newInstitution.setOrigin(Optional.ofNullable(areaOrganizzativaOmogenea.getOrigin())
                    .map(Origin::name)
                    .orElse(null));
            newInstitution.setCreatedAt(OffsetDateTime.now());

            Attributes attributes = new Attributes();
            attributes.setOrigin(categoryProxyInfo.getOrigin());
            attributes.setCode(categoryProxyInfo.getCode());
            attributes.setDescription(categoryProxyInfo.getName());
            newInstitution.setAttributes(List.of(attributes));

            return newInstitution;
        };
    }

    private Function<CreateInstitutionStrategyInput, Institution> mappingToInstitutionIPAUo() {
        return strategyInput -> {

            InstitutionProxyInfo institutionProxyInfo = partyRegistryProxyConnector.getInstitutionById(strategyInput.getTaxCode());

            CategoryProxyInfo categoryProxyInfo = partyRegistryProxyConnector.getCategory(institutionProxyInfo.getOrigin(), institutionProxyInfo.getCategory());

            UnitaOrganizzativa unitaOrganizzativa = partyRegistryProxyConnector.getUoById(strategyInput.getSubunitCode());

            Institution newInstitution = new Institution();

            newInstitution.setOriginId( unitaOrganizzativa.getId() );
            newInstitution.setDescription( unitaOrganizzativa.getDescrizioneUo() );
            newInstitution.setDigitalAddress( TYPE_MAIL_PEC.equals(unitaOrganizzativa.getTipoMail1())
                    ? unitaOrganizzativa.getMail1() : institutionProxyInfo.getDigitalAddress() );
            newInstitution.setAddress( unitaOrganizzativa.getIndirizzo() );
            newInstitution.setZipCode( unitaOrganizzativa.getCAP() );
            newInstitution.setTaxCode( unitaOrganizzativa.getCodiceFiscaleEnte() );
            newInstitution.setSubunitCode(strategyInput.getSubunitCode());
            newInstitution.setSubunitType(InstitutionPaSubunitType.UO.name());
            newInstitution.setParentDescription(institutionProxyInfo.getDescription());
            newInstitution.setRootParentId(institutionProxyInfo.getId());

            if(StringUtils.isNotBlank(unitaOrganizzativa.getCodiceUniAoo())) {
                PaAttributes paAttributes = new PaAttributes();
                paAttributes.setAooParentCode(unitaOrganizzativa.getCodiceUniAoo());
                newInstitution.setPaAttributes(paAttributes);
            }

            newInstitution.setExternalId(createExternalId.apply(strategyInput));
            newInstitution.setOrigin(Optional.ofNullable(unitaOrganizzativa.getOrigin())
                    .map(Origin::name)
                    .orElse(null));
            newInstitution.setCreatedAt(OffsetDateTime.now());

            Attributes attributes = new Attributes();
            attributes.setOrigin(categoryProxyInfo.getOrigin());
            attributes.setCode(categoryProxyInfo.getCode());
            attributes.setDescription(categoryProxyInfo.getName());
            newInstitution.setAttributes(List.of(attributes));

            return newInstitution;
        };
    }

}
