package it.pagopa.selfcare.mscore.core.strategy;

import it.pagopa.selfcare.mscore.api.InstitutionConnector;
import it.pagopa.selfcare.mscore.api.PartyRegistryProxyConnector;
import it.pagopa.selfcare.mscore.constant.CustomError;
import it.pagopa.selfcare.mscore.constant.Origin;
import it.pagopa.selfcare.mscore.core.mapper.InstitutionMapper;
import it.pagopa.selfcare.mscore.core.strategy.input.CreateInstitutionStrategyInput;
import it.pagopa.selfcare.mscore.core.util.InstitutionPaSubunitType;
import it.pagopa.selfcare.mscore.exception.MsCoreException;
import it.pagopa.selfcare.mscore.exception.ResourceConflictException;
import it.pagopa.selfcare.mscore.model.AreaOrganizzativaOmogenea;
import it.pagopa.selfcare.mscore.model.UnitaOrganizzativa;
import it.pagopa.selfcare.mscore.model.institution.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static it.pagopa.selfcare.mscore.constant.GenericError.CREATE_INSTITUTION_ERROR;

@Slf4j
@Component
public class CreateInstitutionStrategyIpa implements CreateInstitutionStrategy {

    private final PartyRegistryProxyConnector partyRegistryProxyConnector;

    private final InstitutionConnector institutionConnector;

    private final InstitutionMapper institutionMapper;

    public CreateInstitutionStrategyIpa(PartyRegistryProxyConnector partyRegistryProxyConnector,
                                        InstitutionConnector institutionConnector,
                                        InstitutionMapper institutionMapper) {
        this.partyRegistryProxyConnector = partyRegistryProxyConnector;
        this.institutionConnector = institutionConnector;
        this.institutionMapper = institutionMapper;
    }


    @Override
    public Institution createInstitution(CreateInstitutionStrategyInput strategyInput) {

        final InstitutionPaSubunitType subunitType = strategyInput.getSubunitType();

        Institution institutionToReturn;
        Optional<Institution> opt = institutionConnector.findByExternalId(strategyInput.getTaxCode());
        if(opt.isEmpty()){
            try {
                Institution institutionEC = getInstitutionEC(strategyInput.getTaxCode());
                institutionToReturn = institutionConnector.save(institutionEC);
            } catch (Exception e) {
                throw new MsCoreException(CREATE_INSTITUTION_ERROR.getMessage(), CREATE_INSTITUTION_ERROR.getCode());
            }
        } else {
           if(Objects.isNull(subunitType) || InstitutionPaSubunitType.EC.equals(subunitType)){
               throw new ResourceConflictException(String
                       .format(CustomError.CREATE_INSTITUTION_CONFLICT.getMessage(), strategyInput.getTaxCode()),
                       CustomError.CREATE_INSTITUTION_CONFLICT.getCode());
           }
            institutionToReturn = opt.get();
        }

        checkIfAlreadyExistsByTaxCodeAndSubunitCode(strategyInput.getTaxCode(), strategyInput.getSubunitCode());

        Institution institution;
        if (InstitutionPaSubunitType.AOO.equals(subunitType)) {
            institution = mappingToInstitutionIPAAoo(strategyInput, institutionToReturn);
        } else if(InstitutionPaSubunitType.UO.equals(subunitType)) {
            institution = mappingToInstitutionIPAUo(strategyInput, institutionToReturn);
        }else{
            log.info("createInstitution :: unsupported subunitType {}", subunitType);
            return institutionToReturn;
        }

        try {
            return institutionConnector.save(institution);
        } catch (Exception e) {
            throw new MsCoreException(CREATE_INSTITUTION_ERROR.getMessage(), CREATE_INSTITUTION_ERROR.getCode());
        }

    }

    private Institution getInstitutionEC(String taxCode) {

        InstitutionProxyInfo institutionProxyInfo = partyRegistryProxyConnector.getInstitutionById(taxCode);
        CategoryProxyInfo categoryProxyInfo = partyRegistryProxyConnector.getCategory(institutionProxyInfo.getOrigin(), institutionProxyInfo.getCategory());

        Institution newInstitution = institutionMapper.fromInstitutionProxyInfo(institutionProxyInfo);
        newInstitution.setSubunitType(InstitutionPaSubunitType.EC.name());
        newInstitution.setExternalId(taxCode);
        newInstitution.setOrigin(Origin.IPA.getValue());
        newInstitution.setCreatedAt(OffsetDateTime.now());

        Attributes attributes = new Attributes();
        attributes.setOrigin(categoryProxyInfo.getOrigin());
        attributes.setCode(categoryProxyInfo.getCode());
        attributes.setDescription(categoryProxyInfo.getName());
        newInstitution.setAttributes(List.of(attributes));

        return newInstitution;
    }


    private Institution mappingToInstitutionIPAAoo(CreateInstitutionStrategyInput strategyInput, Institution rootParentInstitution) {

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
        newInstitution.setRootParentId(rootParentInstitution.getRootParentId());

        newInstitution.setExternalId(getExternalId(strategyInput));
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
    }

    private Institution mappingToInstitutionIPAUo(CreateInstitutionStrategyInput strategyInput, Institution rootParentInstitution) {

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
        newInstitution.setRootParentId(rootParentInstitution.getRootParentId());

        if(StringUtils.isNotBlank(unitaOrganizzativa.getCodiceUniAoo())) {
            PaAttributes paAttributes = new PaAttributes();
            paAttributes.setAooParentCode(unitaOrganizzativa.getCodiceUniAoo());
            newInstitution.setPaAttributes(paAttributes);
        }

        newInstitution.setExternalId(getExternalId(strategyInput));
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
    }


    private void checkIfAlreadyExistsByTaxCodeAndSubunitCode(String taxCode, String subunitCode) {

        List<Institution> institutions = institutionConnector.findByTaxCodeAndSubunitCode(taxCode, subunitCode);
        if (!institutions.isEmpty())
            throw new ResourceConflictException(String
                    .format(CustomError.CREATE_INSTITUTION_IPA_CONFLICT.getMessage(), taxCode, subunitCode),
                    CustomError.CREATE_INSTITUTION_CONFLICT.getCode());
    }
}
