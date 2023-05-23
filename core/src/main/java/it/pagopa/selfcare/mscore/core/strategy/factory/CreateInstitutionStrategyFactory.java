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
import it.pagopa.selfcare.mscore.model.institution.Attributes;
import it.pagopa.selfcare.mscore.model.institution.CategoryProxyInfo;
import it.pagopa.selfcare.mscore.model.institution.Institution;
import it.pagopa.selfcare.mscore.model.institution.InstitutionProxyInfo;
import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;

@Component
public class CreateInstitutionStrategyFactory {

    private final InstitutionConnector institutionConnector;
    private final PartyRegistryProxyConnector partyRegistryProxyConnector;

    private final InstitutionMapper institutionMapper;

    private final Function<CreateInstitutionStrategyInput, String> createExternalId = strategyInput -> Objects.isNull(strategyInput.getSubunitCode())
            ? strategyInput.getTaxCode()
            : String.format("%s#%s", strategyInput.getTaxCode(), strategyInput.getSubunitCode());

    public CreateInstitutionStrategyFactory(InstitutionConnector institutionConnector, PartyRegistryProxyConnector partyRegistryProxyConnector, InstitutionMapper institutionMapper) {
        this.institutionConnector = institutionConnector;
        this.partyRegistryProxyConnector = partyRegistryProxyConnector;
        this.institutionMapper = institutionMapper;
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
            newInstitution.setDigitalAddress( areaOrganizzativaOmogenea.getMail1() );
            //newInstitution.setAddress( areaOrganizzativaOmogenea.getAddress() );
            //newInstitution.setZipCode( areaOrganizzativaOmogenea.getZipCode() );
            newInstitution.setTaxCode( areaOrganizzativaOmogenea.getCodiceFiscaleEnte() );


            newInstitution.setExternalId(createExternalId.apply(strategyInput));
            newInstitution.setOrigin(areaOrganizzativaOmogenea.getOrigin().name());
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
            newInstitution.setDigitalAddress( unitaOrganizzativa.getMail1() );
            //newInstitution.setAddress( unitaOrganizzativa.getAddress() );
            //newInstitution.setZipCode( unitaOrganizzativa.getZipCode() );
            newInstitution.setTaxCode( unitaOrganizzativa.getCodiceFiscaleEnte() );

            newInstitution.setExternalId(createExternalId.apply(strategyInput));
            newInstitution.setOrigin(unitaOrganizzativa.getOrigin().name());
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
