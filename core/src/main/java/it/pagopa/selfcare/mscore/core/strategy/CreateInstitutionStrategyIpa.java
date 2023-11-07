package it.pagopa.selfcare.mscore.core.strategy;

import it.pagopa.selfcare.mscore.api.InstitutionConnector;
import it.pagopa.selfcare.mscore.api.PartyRegistryProxyConnector;
import it.pagopa.selfcare.mscore.constant.Origin;
import it.pagopa.selfcare.mscore.core.mapper.InstitutionMapper;
import it.pagopa.selfcare.mscore.core.strategy.input.CreateInstitutionStrategyInput;
import it.pagopa.selfcare.mscore.core.util.InstitutionPaSubunitType;
import it.pagopa.selfcare.mscore.exception.MsCoreException;
import it.pagopa.selfcare.mscore.model.AreaOrganizzativaOmogenea;
import it.pagopa.selfcare.mscore.model.UnitaOrganizzativa;
import it.pagopa.selfcare.mscore.model.institution.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

import static it.pagopa.selfcare.mscore.constant.GenericError.CREATE_INSTITUTION_ERROR;

@Slf4j
@Component
public class CreateInstitutionStrategyIpa extends CreateInstitutionStrategyCommon implements CreateInstitutionStrategy {

    private final PartyRegistryProxyConnector partyRegistryProxyConnector;

    private final InstitutionMapper institutionMapper;

    public CreateInstitutionStrategyIpa(PartyRegistryProxyConnector partyRegistryProxyConnector,
                                        InstitutionConnector institutionConnector,
                                        InstitutionMapper institutionMapper) {
        super(institutionConnector);
        this.partyRegistryProxyConnector = partyRegistryProxyConnector;
        this.institutionMapper = institutionMapper;
    }


    @Override
    public Institution createInstitution(CreateInstitutionStrategyInput strategyInput) {

        checkIfAlreadyExistsByTaxCodeAndSubunitCode(strategyInput.getTaxCode(), strategyInput.getSubunitCode());

        final InstitutionPaSubunitType subunitType = strategyInput.getSubunitType();
        final InstitutionProxyInfo institutionProxyInfo = partyRegistryProxyConnector.getInstitutionById(strategyInput.getTaxCode());
        final CategoryProxyInfo categoryProxyInfo = partyRegistryProxyConnector.getCategory(institutionProxyInfo.getOrigin(), institutionProxyInfo.getCategory());

        Institution institution;
        if (InstitutionPaSubunitType.AOO.equals(subunitType)) {
            Institution institutionEC = getOrSaveInstitutionEc(strategyInput, institutionProxyInfo, categoryProxyInfo);
            institution = mappingToInstitutionIPAAoo(strategyInput, institutionEC.getId(), institutionProxyInfo, categoryProxyInfo);
        } else if(InstitutionPaSubunitType.UO.equals(subunitType)) {
            Institution institutionEC = getOrSaveInstitutionEc(strategyInput, institutionProxyInfo, categoryProxyInfo);
            institution = mappingToInstitutionIPAUo(strategyInput, institutionEC.getId(), institutionProxyInfo, categoryProxyInfo);
        }else{
            log.info("createInstitution :: unsupported subunitType {}", subunitType);
            institution = getInstitutionEC(strategyInput.getTaxCode(), institutionProxyInfo, categoryProxyInfo);
        }

        try {
            return institutionConnector.save(institution);
        } catch (Exception e) {
            throw new MsCoreException(CREATE_INSTITUTION_ERROR.getMessage(), CREATE_INSTITUTION_ERROR.getCode());
        }
    }

    private Institution getOrSaveInstitutionEc(CreateInstitutionStrategyInput strategyInput,
                                               InstitutionProxyInfo institutionProxyInfo,
                                               CategoryProxyInfo categoryProxyInfo) {
        try {
            Optional<Institution> opt = institutionConnector.findByExternalId(strategyInput.getTaxCode());
            if(opt.isEmpty()) {
                Institution institutionEC = getInstitutionEC(strategyInput.getTaxCode(), institutionProxyInfo, categoryProxyInfo);
                return institutionConnector.save(institutionEC);
            } else {
                return opt.get();
            }
        } catch (Exception e) {
            throw new MsCoreException(CREATE_INSTITUTION_ERROR.getMessage(), CREATE_INSTITUTION_ERROR.getCode());
        }
    }

    private Institution getInstitutionEC(String taxCode, InstitutionProxyInfo institutionProxyInfo, CategoryProxyInfo categoryProxyInfo) {

        Institution newInstitution = institutionMapper.fromInstitutionProxyInfo(institutionProxyInfo);
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


    private Institution mappingToInstitutionIPAAoo(CreateInstitutionStrategyInput strategyInput,
                                                   String rootParentInstitutionId,
                                                   InstitutionProxyInfo institutionProxyInfo,
                                                   CategoryProxyInfo categoryProxyInfo) {

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
        newInstitution.setRootParentId(rootParentInstitutionId);

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

    private Institution mappingToInstitutionIPAUo(CreateInstitutionStrategyInput strategyInput,
                                                  String rootParentInstitutionId,
                                                  InstitutionProxyInfo institutionProxyInfo,
                                                  CategoryProxyInfo categoryProxyInfo) {

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
        newInstitution.setRootParentId(rootParentInstitutionId);

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

}
