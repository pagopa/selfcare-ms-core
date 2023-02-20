package it.pagopa.selfcare.mscore.web.model.mapper;

import it.pagopa.selfcare.commons.base.security.PartyRole;
import it.pagopa.selfcare.mscore.model.*;
import it.pagopa.selfcare.mscore.model.institution.*;
import it.pagopa.selfcare.mscore.web.model.institution.InstitutionType;
import it.pagopa.selfcare.mscore.web.model.institution.InstitutionUpdate;
import it.pagopa.selfcare.mscore.web.model.institution.*;
import it.pagopa.selfcare.mscore.web.model.onboarding.OnboardedProducts;
import it.pagopa.selfcare.mscore.web.model.onboarding.ProductInfo;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
        institutionResponse.setRea(institution.getRea());
        institutionResponse.setShareCapital(institution.getShareCapital());
        institutionResponse.setBusinessRegisterPlace(institution.getBusinessRegisterPlace());
        institutionResponse.setSupportEmail(institution.getSupportEmail());
        institutionResponse.setSupportPhone(institution.getSupportPhone());
        institutionResponse.setImported(institution.isImported());
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

        institutionManagerResponse.setFrom(manager.getId());
        institutionManagerResponse.setTo(institution.getId());
        if(institution.getOnboarding()!=null) {
            institutionManagerResponse.setProduct(convertToProductInfo(institution.getOnboarding(), productId));
            for (Onboarding onboarding : institution.getOnboarding()) {
                if (productId.equalsIgnoreCase(onboarding.getProductId())) {
                    institutionManagerResponse.setPricingPlan(onboarding.getPricingPlan());
                    if (onboarding.getBilling() != null)
                        institutionManagerResponse.setBillingResponse(convertToBillingResponse(onboarding.getBilling(), institution));
                }
            }
        }

        institutionManagerResponse.setInstitutionUpdate(convertToInstitutionUpdate(institution));
        List<UserBinding> userBindings = manager.getBindings();
        /*for(UserBinding userBinding : userBindings){
            if(institution.getId().equalsIgnoreCase(userBinding.getInstitutionId())
                    && userBinding.getProduct() != null
            && productId.equalsIgnoreCase(userBinding.getProduct().getProductId())){
                institutionManagerResponse.setCreatedAt(userBinding.getProduct().getCreatedAt());
                institutionManagerResponse.setUpdatedAt(userBinding.getProduct().getUpdatedAt());
                institutionManagerResponse.setState(userBinding.getProduct().getStatus());
            }
        }*/

        return institutionManagerResponse;
    }


    public static InstitutionBillingResponse toInstitutionBillingResponse(Institution institution, String productId) {
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
                response.setBilling(convertToBillingResponse(onboarding.getBilling(), institution));
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
        response.setVatNumberGroup(paymentServiceProvider.isVatNumberGroup());
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

    private static BillingResponse convertToBillingResponse(Billing billing, Institution institution) {
        BillingResponse billingResponse = new BillingResponse();
        if(billing!=null) {
            billingResponse.setVatNumber(billing.getVatNumber());
            billingResponse.setRecipientCode(billing.getRecipientCode());
            billingResponse.setPublicServices(billing.isPublicServices());
            return billingResponse;
        }else if(institution.getBilling()!=null){
            billingResponse.setVatNumber(institution.getBilling().getVatNumber());
            billingResponse.setRecipientCode(institution.getBilling().getRecipientCode());
            billingResponse.setPublicServices(institution.getBilling().isPublicServices());
            return billingResponse;
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
            if (productId.equalsIgnoreCase(o.getProductId()) && RelationshipState.ACTIVE == o.getStatus())
                productInfo.setCreatedAt(o.getCreatedAt());
        }
        productInfo.setId(productId);
        return productInfo;
    }

    public static Institution toInstitution(InstitutionRequest request, String externalId) {
        Institution institution = new Institution();
        institution.setExternalId(externalId);
        institution.setInstitutionType(request.getInstitutionType());
        institution.setDescription(request.getDescription());
        institution.setAddress(request.getAddress());
        institution.setDigitalAddress(request.getDigitalAddress());
        institution.setTaxCode(request.getTaxCode());
        institution.setZipCode(request.getZipCode());
        institution.setGeographicTaxonomies(convertToGeographicTaxonomies(request.getGeographicTaxonomies()));
        institution.setAttributes(convertToAttributes(request.getAttributes()));
        institution.setRea(request.getRea());
        institution.setShareCapital(request.getShareCapital());
        institution.setBusinessRegisterPlace(request.getBusinessRegisterPlace());
        institution.setSupportEmail(request.getSupportEmail());
        institution.setSupportPhone(request.getSupportPhone());
        institution.setImported(request.isImported());
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

    public static OnboardedProducts toOnboardedProducts(List<Onboarding> onboardings) {
        OnboardedProducts onboardedProducts = new OnboardedProducts();
        onboardedProducts.setProducts(toInstitutionProduct(onboardings));
        return onboardedProducts;
    }

    public static List<InstitutionProduct> toInstitutionProduct(List<Onboarding> onboardings) {
        return onboardings.stream().map(onboarding -> {
            InstitutionProduct product = new InstitutionProduct();
            product.setId(onboarding.getProductId());
            product.setState(onboarding.getStatus());
            return product;
        }).collect(Collectors.toList());
    }
}
