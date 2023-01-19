package it.pagopa.selfcare.mscore.web.model.mapper;

import it.pagopa.selfcare.commons.base.security.PartyRole;
import it.pagopa.selfcare.mscore.model.OnboardedUser;
import it.pagopa.selfcare.mscore.model.Product;
import it.pagopa.selfcare.mscore.model.RelationshipState;
import it.pagopa.selfcare.mscore.model.institution.*;
import it.pagopa.selfcare.mscore.web.model.institution.InstitutionManagerResponse;
import it.pagopa.selfcare.mscore.web.model.institution.*;
import it.pagopa.selfcare.mscore.web.model.institution.InstitutionType;
import it.pagopa.selfcare.mscore.web.model.institution.InstitutionUpdate;
import it.pagopa.selfcare.mscore.web.model.onboarding.ProductInfo;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@NoArgsConstructor(access = AccessLevel.NONE)
public class InstitutionMapper {

    public static InstitutionResponse toInstitutionResponse(Institution institution) {
        InstitutionResponse institutionResponse = new InstitutionResponse();
        institutionResponse.setId(institution.getId());
        institutionResponse.setExternalId(institution.getExternalId());
        institutionResponse.setIpaCode(institution.getIpaCode());
        institutionResponse.setDescription(institution.getDescription());
        institutionResponse.setInstitutionType(institution.getInstitutionType());
        institutionResponse.setDigitalAddress(institution.getDigitalAddress());
        institutionResponse.setAddress(institution.getAddress());
        institutionResponse.setZipCode(institution.getZipCode());
        institutionResponse.setTaxCode(institution.getTaxCode());
        if(institution.getGeographicTaxonomies()!=null)
            institutionResponse.setGeographicTaxonomies(convertToGeoResponse(institution.getGeographicTaxonomies()));
        if(institution.getAttributes()!=null)
            institutionResponse.setAttributes(convertToAttributeResponse(institution.getAttributes()));
        if(institution.getDataProtectionOfficer()!=null)
            institutionResponse.setDataProtectionOfficer(convertToDataProtectionOfficerResponse(institution.getDataProtectionOfficer()));
        if(institution.getPaymentServiceProvider()!=null)
            institutionResponse.setPaymentServiceProviderResponse(convertToPaymentServiceProviderResponse(institution.getPaymentServiceProvider()));
        return institutionResponse;
    }

    public static InstitutionManagerResponse toInstitutionManagerResponse(Institution institution, OnboardedUser manager, String productId, String contractId) {
        InstitutionManagerResponse institutionManagerResponse = new InstitutionManagerResponse();

        institutionManagerResponse.setId(contractId);

        institutionManagerResponse.setFrom(manager.getUser());
        institutionManagerResponse.setTo(institution.getId());
        if(institution.getOnboarding()!=null) {
            institutionManagerResponse.setProduct(convertToProductInfo(institution.getOnboarding(), productId));
            for (Onboarding onboarding : institution.getOnboarding()) {
                if (productId.equalsIgnoreCase(onboarding.getProductId())) {
                    institutionManagerResponse.setPricingPlan(onboarding.getPricingPlan());
                    if (onboarding.getBilling() != null)
                        institutionManagerResponse.setBillingResponse(convertToBillingResponse(onboarding.getBilling()));
                }
            }
        }

        institutionManagerResponse.setInstitutionUpdate(convertToInstitutionUpdate(institution));

        Map<String, Map<String, Product>> map = manager.getBindings();

        if (map.get(institution.getId()) != null) {
            Product product = map.get(institution.getId()).get(productId);
            institutionManagerResponse.setCreatedAt(product.getCreatedAt());
            institutionManagerResponse.setUpdatedAt(product.getUpdatedAt());
            institutionManagerResponse.setState(product.getStatus());
        }

        return institutionManagerResponse;
    }


    public static InstitutionBillingResponse toBillingResponse(Institution institution, String productId) {
        if (institution == null) {
            return null;
        }
        InstitutionBillingResponse response = new InstitutionBillingResponse();

        response.setInstitutionId(institution.getId());
        response.setExternalId(institution.getExternalId());
        response.setIpaCode(institution.getIpaCode());
        response.setDescription(institution.getDescription());
        response.setInstitutionType(InstitutionType.valueOf(institution.getInstitutionType().name()));
        response.setDigitalAddress(institution.getDigitalAddress());
        response.setAddress(institution.getAddress());
        response.setZipCode(institution.getZipCode());
        response.setTaxCode(institution.getTaxCode());

        for (Onboarding onboarding : institution.getOnboarding()) {
            if (productId.equalsIgnoreCase(onboarding.getProductId())) {
                response.setBilling(convertToBillingResponse(onboarding.getBilling()));
                response.setPricingPlan(onboarding.getPricingPlan());
            }
        }

        return response;
    }

    private static PaymentServiceProviderResponse convertToPaymentServiceProviderResponse(PaymentServiceProvider paymentServiceProvider) {
        PaymentServiceProviderResponse response = new PaymentServiceProviderResponse();
        response.setAbiCode(paymentServiceProvider.getAbiCode());
        response.setLegalRegisterName(paymentServiceProvider.getLegalRegisterName());
        response.setBusinessRegisterNumber(paymentServiceProvider.getBusinessRegisterNumber());
        response.setVatNumberGroup(paymentServiceProvider.getVatNumberGroup());
        response.setLegalRegisterNumber(paymentServiceProvider.getLegalRegisterNumber());
        return response;
    }

    private static DataProtectionOfficerResponse convertToDataProtectionOfficerResponse(DataProtectionOfficer dataProtectionOfficer) {
        DataProtectionOfficerResponse response = new DataProtectionOfficerResponse();
        response.setPec(dataProtectionOfficer.getPec());
        response.setEmail(dataProtectionOfficer.getEmail());
        response.setAddress(dataProtectionOfficer.getAddress());
        return response;
    }

    private static List<AttributesResponse> convertToAttributeResponse(List<Attributes> attributes) {
        List<AttributesResponse> list = new ArrayList<>();
        for (Attributes a : attributes) {
            AttributesResponse response = new AttributesResponse();
            response.setCode(a.getCode());
            response.setOrigin(a.getOrigin());
            response.setDescription(a.getDescription());
            list.add(response);
        }
        return list;
    }

    private static List<GeoTaxonomies> convertToGeoResponse(List<GeographicTaxonomies> geographicTaxonomies) {
        List<GeoTaxonomies> list = new ArrayList<>();
        for (GeographicTaxonomies g : geographicTaxonomies) {
            GeoTaxonomies geoTaxonomies = new GeoTaxonomies();
            geoTaxonomies.setCode(g.getCode());
            geoTaxonomies.setDesc(g.getDesc());
            geoTaxonomies.setEnable(g.isEnable());
            list.add(geoTaxonomies);
        }
        return list;
    }

    private static List<String> convertToGeoString(List<GeographicTaxonomies> geographicTaxonomies) {
        List<String> list = new ArrayList<>();
        geographicTaxonomies.forEach(g -> list.add(g.getCode()));
        return list;
    }

    private static BillingResponse convertToBillingResponse(Billing billing) {
        BillingResponse billingResponse = new BillingResponse();
        if(billing!=null) {
            billingResponse.setVatNumber(billing.getVatNumber());
            billingResponse.setRecipientCode(billing.getRecipientCode());
            billingResponse.setPublicServices(billing.isPublicServices());
        }
        return billingResponse;
    }

    private static InstitutionUpdate convertToInstitutionUpdate(Institution institution) {
        InstitutionUpdate institutionUpdate = new InstitutionUpdate();
        institutionUpdate.setInstitutionType(institution.getInstitutionType());
        institutionUpdate.setDescription(institution.getDescription());
        institutionUpdate.setDigitalAddress(institution.getDigitalAddress());
        institutionUpdate.setAddress(institution.getAddress());
        institutionUpdate.setZipCode(institution.getZipCode());
        institutionUpdate.setTaxCode(institution.getTaxCode());
        if(institution.getGeographicTaxonomies()!=null)
            institutionUpdate.setGeographicTaxonomyCodes(convertToGeoString(institution.getGeographicTaxonomies()));

        return institutionUpdate;
    }

    private static ProductInfo convertToProductInfo(List<Onboarding> onboarding, String productId) {
        ProductInfo productInfo = new ProductInfo();
        productInfo.setRole(List.of(PartyRole.MANAGER.name()));
        for (Onboarding o : onboarding) {
            if (productId.equalsIgnoreCase(o.getProductId()) && RelationshipState.ACTIVE.equals(o.getStatus()))
                productInfo.setCreatedAt(o.getCreatedAt());
        }
        productInfo.setId(productId);
        return productInfo;
    }

    public static InstitutionResource toResource(Institution institution) {
        InstitutionResource resource = new InstitutionResource();
        resource.setId(institution.getId());
        resource.setExternalId(institution.getExternalId());
        return resource;
    }

    public static Institution toInstitution(InstitutionRequest request, String externalId) {
        Institution institution = new Institution();
        institution.setCreatedAt(OffsetDateTime.now());
        institution.setUpdatedAt(OffsetDateTime.now());
        institution.setExternalId(externalId);
        institution.setInstitutionType(request.getInstitutionType());
        institution.setDescription(request.getDescription());
        institution.setAddress(request.getAddress());
        institution.setDigitalAddress(request.getDigitalAddress());
        institution.setTaxCode(request.getTaxCode());
        institution.setZipCode(request.getZipCode());
        institution.setGeographicTaxonomies(convertToGeographicTaxonomies(request.getGeographicTaxonomies()));
        institution.setAttributes(convertToAttributes(request.getAttributes()));
        if(request.getPaymentServiceProvider()!=null)
            institution.setPaymentServiceProvider(convertToPaymentServiceProvider(request.getPaymentServiceProvider()));
        if(request.getDataProtectionOfficer()!=null)
            institution.setDataProtectionOfficer(convertToDataProtectionOfficer(request.getDataProtectionOfficer()));
        return institution;
    }

    private static DataProtectionOfficer convertToDataProtectionOfficer(DataProtectionOfficerRequest request) {
        DataProtectionOfficer dataProtectionOfficer = new DataProtectionOfficer();
        dataProtectionOfficer.setAddress(request.getAddress());
        dataProtectionOfficer.setEmail(request.getEmail());
        dataProtectionOfficer.setPec(request.getPec());
        return dataProtectionOfficer;
    }

    private static PaymentServiceProvider convertToPaymentServiceProvider(PaymentServiceProviderRequest request) {
        PaymentServiceProvider paymentServiceProvider = new PaymentServiceProvider();
        paymentServiceProvider.setAbiCode(request.getAbiCode());
        paymentServiceProvider.setVatNumberGroup(request.isVatNumberGroup());
        paymentServiceProvider.setBusinessRegisterNumber(request.getBusinessRegisterNumber());
        paymentServiceProvider.setLegalRegisterNumber(request.getLegalRegisterNumber());
        paymentServiceProvider.setLegalRegisterName(request.getLegalRegisterName());
        return paymentServiceProvider;
    }

    private static List<Attributes> convertToAttributes(List<AttributesRequest> attributes) {
        List<Attributes> response = new ArrayList<>();
        if(attributes!=null) {
            for (AttributesRequest a : attributes) {
                Attributes attribute = new Attributes();
                attribute.setCode(a.getCode());
                attribute.setDescription(a.getDescription());
                attribute.setOrigin(a.getOrigin());
                response.add(attribute);
            }
        }
        return response;
    }

    private static List<GeographicTaxonomies> convertToGeographicTaxonomies(List<GeoTaxonomies> request) {
        List<GeographicTaxonomies> response = new ArrayList<>();
        if(request!=null) {
            for (GeoTaxonomies g : request) {
                GeographicTaxonomies geographicTaxonomies = new GeographicTaxonomies();
                geographicTaxonomies.setCode(g.getCode());
                geographicTaxonomies.setDesc(g.getDesc());
                response.add(geographicTaxonomies);
            }
        }
        return response;
    }
}
