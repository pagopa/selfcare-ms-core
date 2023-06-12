package it.pagopa.selfcare.mscore.connector.dao.model.mapper;

import it.pagopa.selfcare.mscore.connector.dao.model.InstitutionEntity;
import it.pagopa.selfcare.mscore.connector.dao.model.inner.*;
import it.pagopa.selfcare.mscore.constant.Origin;
import it.pagopa.selfcare.mscore.model.institution.*;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.query.Update;

import java.time.OffsetDateTime;
import java.util.*;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.NONE)
public class InstitutionMapperHelper {


    public static void addGeographicTaxonomies(List<InstitutionGeographicTaxonomies> taxonomiesList, Update update) {
        if (taxonomiesList != null && !taxonomiesList.isEmpty()) {
            List<GeoTaxonomyEntity> list = taxonomiesList.stream().map(geographicTaxonomies -> {
                GeoTaxonomyEntity entity = new GeoTaxonomyEntity();
                entity.setCode(geographicTaxonomies.getCode());
                entity.setDesc(geographicTaxonomies.getDesc());
                return entity;
            }).collect(Collectors.toList());
            update.set(InstitutionEntity.Fields.geographicTaxonomies.name(), list);
        }
    }

    public static Map<String, Object> getNotNullField(InstitutionUpdate institutionUpdate) {
        Map<String, Object> response = new HashMap<>();
        if(institutionUpdate.getInstitutionType() != null) {
            response.put(InstitutionUpdate.Fields.institutionType.name(), institutionUpdate.getInstitutionType().name());
        }
        response.put(InstitutionUpdate.Fields.description.name(), institutionUpdate.getDescription());
        response.put(InstitutionUpdate.Fields.digitalAddress.name(), institutionUpdate.getDigitalAddress());
        response.put(InstitutionUpdate.Fields.address.name(), institutionUpdate.getAddress());
        response.put(InstitutionUpdate.Fields.taxCode.name(), institutionUpdate.getTaxCode());
        response.put(InstitutionUpdate.Fields.zipCode.name(), institutionUpdate.getZipCode());
        response.put(InstitutionUpdate.Fields.rea.name(), institutionUpdate.getRea());
        response.put(InstitutionUpdate.Fields.shareCapital.name(), institutionUpdate.getShareCapital());
        response.put(InstitutionUpdate.Fields.businessRegisterPlace.name(), institutionUpdate.getBusinessRegisterPlace());
        response.put(InstitutionUpdate.Fields.supportEmail.name(), institutionUpdate.getSupportEmail());
        response.put(InstitutionUpdate.Fields.supportPhone.name(), institutionUpdate.getSupportPhone());
        response.put(InstitutionUpdate.Fields.imported.name(), institutionUpdate.isImported());

        if(institutionUpdate.getPaymentServiceProvider() != null) {
            response.put(constructPaymentInnerField(PaymentServiceProvider.Fields.abiCode.name()),
                    institutionUpdate.getPaymentServiceProvider().getAbiCode());
            response.put(constructPaymentInnerField(PaymentServiceProvider.Fields.businessRegisterNumber.name()),
                    institutionUpdate.getPaymentServiceProvider().getBusinessRegisterNumber());
            response.put(constructPaymentInnerField(PaymentServiceProvider.Fields.legalRegisterNumber.name()),
                    institutionUpdate.getPaymentServiceProvider().getLegalRegisterNumber());
            response.put(constructPaymentInnerField(PaymentServiceProvider.Fields.legalRegisterName.name()),
                    institutionUpdate.getPaymentServiceProvider().getLegalRegisterName());
            response.put(constructPaymentInnerField(PaymentServiceProvider.Fields.vatNumberGroup.name()),
                    institutionUpdate.getPaymentServiceProvider().isVatNumberGroup());
        }

        if(institutionUpdate.getDataProtectionOfficer() != null){
            response.put(constructProtectionOfficerInnerField(DataProtectionOfficer.Fields.pec.name()),
                    institutionUpdate.getDataProtectionOfficer().getPec());
            response.put(constructProtectionOfficerInnerField(DataProtectionOfficer.Fields.address.name()),
                    institutionUpdate.getDataProtectionOfficer().getAddress());
            response.put(constructProtectionOfficerInnerField(DataProtectionOfficer.Fields.email.name()),
                    institutionUpdate.getDataProtectionOfficer().getEmail());
        }

        response.values().removeIf(Objects::isNull);
        return response;
    }

    private static String constructProtectionOfficerInnerField(String name) {
        return InstitutionUpdate.Fields.dataProtectionOfficer.name() + "." + name;
    }

    private static String constructPaymentInnerField(String name) {
        return InstitutionUpdate.Fields.businessRegisterPlace.name() + "." + name;
    }
}
