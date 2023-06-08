package it.pagopa.selfcare.mscore.web.model.mapper;

import it.pagopa.selfcare.mscore.exception.ResourceNotFoundException;
import it.pagopa.selfcare.mscore.model.institution.*;
import it.pagopa.selfcare.mscore.model.onboarding.OnboardedProduct;
import it.pagopa.selfcare.mscore.model.user.ProductManagerInfo;
import it.pagopa.selfcare.mscore.web.model.institution.*;
import it.pagopa.selfcare.mscore.web.model.onboarding.OnboardedProducts;
import it.pagopa.selfcare.mscore.web.model.onboarding.ProductInfo;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.*;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.NONE)
public class InstitutionMapper {

    public static InstitutionResponse toInstitutionResponse(Institution institution) {
        InstitutionResponse institutionResponse = new InstitutionResponse();
        institutionResponse.setId(institution.getId());
        institutionResponse.setExternalId(institution.getExternalId());
        institutionResponse.setOrigin(institutionResponse.getOrigin());
        institutionResponse.setOriginId(institution.getOriginId());
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
        institutionResponse.setSubunitType(institution.getSubunitType());
        institutionResponse.setSubunitCode(institution.getSubunitCode());
        institutionResponse.setAooParentCode(institution.getAooParentCode());
        if (institution.getGeographicTaxonomies() != null)
            institutionResponse.setGeographicTaxonomies(toGeoTaxonomies(institution.getGeographicTaxonomies()));
        if (institution.getAttributes() != null)
            institutionResponse.setAttributes(toAttributeResponse(institution.getAttributes()));
        if (institution.getDataProtectionOfficer() != null)
            institutionResponse.setDataProtectionOfficer(toDataProtectionOfficerResponse(institution.getDataProtectionOfficer()));
        if (institution.getPaymentServiceProvider() != null)
            institutionResponse.setPaymentServiceProvider(toPaymentServiceProviderResponse(institution.getPaymentServiceProvider()));
        return institutionResponse;
    }

    public static InstitutionManagerResponse toInstitutionManagerResponse(ProductManagerInfo manager, String productId) {
        InstitutionManagerResponse institutionManagerResponse = new InstitutionManagerResponse();
        institutionManagerResponse.setFrom(manager.getUserId());
        institutionManagerResponse.setTo(manager.getInstitution().getId());

        addBillingData(institutionManagerResponse, manager, productId);
        addUserManagerData(institutionManagerResponse, manager, productId);
        addInstitutionUpdate(institutionManagerResponse, manager.getInstitution());

        return institutionManagerResponse;
    }

    public static InstitutionBillingResponse toInstitutionBillingResponse(Institution institution, String productId) {
        if (institution == null) {
            return null;
        }
        InstitutionBillingResponse response = new InstitutionBillingResponse();

        response.setInstitutionId(institution.getId());
        response.setExternalId(institution.getExternalId());
        response.setDescription(institution.getDescription());
        response.setInstitutionType(institution.getInstitutionType());
        response.setDigitalAddress(institution.getDigitalAddress());
        response.setAddress(institution.getAddress());
        response.setZipCode(institution.getZipCode());
        response.setTaxCode(institution.getTaxCode());

        response.setSubunitCode(institution.getSubunitCode());
        response.setSubunitType(institution.getSubunitType());
        response.setAooParentCode(institution.getAooParentCode());

        for (Onboarding onboarding : institution.getOnboarding()) {
            if (productId.equalsIgnoreCase(onboarding.getProductId())) {
                response.setBilling(toBillingResponse(onboarding.getBilling(), institution));
                response.setPricingPlan(onboarding.getPricingPlan());
            }
        }

        return response;
    }

    public static InstitutionUpdateResponse toInstitutionUpdateResponse(Institution institution) {
        InstitutionUpdateResponse institutionUpdate = new InstitutionUpdateResponse();
        institutionUpdate.setAddress(institution.getAddress());
        institutionUpdate.setInstitutionType(institution.getInstitutionType());
        institutionUpdate.setDescription(institution.getDescription());
        institutionUpdate.setDigitalAddress(institution.getDigitalAddress());
        institutionUpdate.setTaxCode(institution.getTaxCode());
        institutionUpdate.setZipCode(institution.getZipCode());
        institutionUpdate.setPaymentServiceProvider(toPaymentServiceProviderResponse(institution.getPaymentServiceProvider()));
        institutionUpdate.setDataProtectionOfficer(toDataProtectionOfficerResponse(institution.getDataProtectionOfficer()));
        if (institution.getGeographicTaxonomies() != null) {
            var geoCodes = institution.getGeographicTaxonomies().stream()
                    .map(InstitutionGeographicTaxonomies::getCode)
                    .collect(Collectors.toList());
            institutionUpdate.setGeographicTaxonomyCodes(geoCodes);
        }
        institutionUpdate.setRea(institution.getRea());
        institutionUpdate.setShareCapital(institution.getShareCapital());
        institutionUpdate.setBusinessRegisterPlace(institution.getBusinessRegisterPlace());
        institutionUpdate.setSupportEmail(institution.getSupportEmail());
        institutionUpdate.setSupportPhone(institution.getSupportPhone());
        institutionUpdate.setImported(institution.isImported());

        institutionUpdate.setSubunitCode(institution.getSubunitCode());
        institutionUpdate.setSubunitType(institution.getSubunitType());
        institutionUpdate.setAooParentCode(institution.getAooParentCode());

        return institutionUpdate;
    }

    private static void addUserManagerData(InstitutionManagerResponse institutionManagerResponse, ProductManagerInfo manager, String productId) {
        for (OnboardedProduct product : manager.getProducts()) {
            if (productId.equalsIgnoreCase(product.getProductId())) {
                ProductInfo productInfo = new ProductInfo();
                productInfo.setId(productId);
                productInfo.setCreatedAt(product.getCreatedAt());
                productInfo.setRole(product.getProductRole());
                if (product.getRole() != null) {
                    institutionManagerResponse.setRole(product.getRole().name());
                }
                institutionManagerResponse.setProduct(productInfo);
                institutionManagerResponse.setState(product.getStatus());
                institutionManagerResponse.setCreatedAt(product.getCreatedAt());
                institutionManagerResponse.setUpdatedAt(product.getUpdatedAt());
                institutionManagerResponse.setId(product.getRelationshipId());
            }
        }
    }

    private static void addInstitutionUpdate(InstitutionManagerResponse institutionManagerResponse, Institution institution) {
        InstitutionUpdateResponse institutionUpdate = new InstitutionUpdateResponse();
        institutionUpdate.setInstitutionType(institution.getInstitutionType());
        institutionUpdate.setDescription(institution.getDescription());
        institutionUpdate.setDigitalAddress(institution.getDigitalAddress());
        institutionUpdate.setAddress(institution.getAddress());
        institutionUpdate.setZipCode(institution.getZipCode());
        institutionUpdate.setTaxCode(institution.getTaxCode());
        institutionUpdate.setPaymentServiceProvider(toPaymentServiceProviderResponse(institution.getPaymentServiceProvider()));
        institutionUpdate.setDataProtectionOfficer(toDataProtectionOfficerResponse(institution.getDataProtectionOfficer()));
        institutionUpdate.setRea(institution.getRea());
        institutionUpdate.setShareCapital(institution.getShareCapital());
        institutionUpdate.setBusinessRegisterPlace(institution.getBusinessRegisterPlace());
        institutionUpdate.setSupportEmail(institution.getSupportEmail());
        institutionUpdate.setSupportPhone(institution.getSupportPhone());
        institutionUpdate.setImported(institution.isImported());
        institutionUpdate.setSubunitCode(institution.getSubunitCode());
        institutionUpdate.setSubunitType(institution.getSubunitType());
        institutionUpdate.setAooParentCode(institution.getAooParentCode());
        if (institution.getGeographicTaxonomies() != null) {
            institutionUpdate.setGeographicTaxonomyCodes(convertToGeoString(institution.getGeographicTaxonomies()));
        }
        institutionManagerResponse.setInstitutionUpdate(institutionUpdate);
    }

    public static InstitutionUpdate toInstitutionUpdate(InstitutionPut institutionPut) {
        InstitutionUpdate institutionUpdate = new InstitutionUpdate();
        institutionUpdate.setDescription(institutionPut.getDescription());
        institutionUpdate.setDigitalAddress(institutionPut.getDigitalAddress());
        institutionUpdate.setGeographicTaxonomies(Optional.ofNullable(institutionPut.getGeographicTaxonomyCodes())
                        .map(geoTaxonomiesCodes -> geoTaxonomiesCodes.stream()
                                .map(code -> new InstitutionGeographicTaxonomies(code, null))
                                .collect(Collectors.toList()))
                        .orElse(null)
        );

        return institutionUpdate;
    }

    public static Institution toInstitution(InstitutionRequest request, String externalId) {
        Institution institution = new Institution();
        institution.setExternalId(externalId);
        institution.setOrigin(request.getOrigin());
        institution.setOriginId(request.getOriginId());
        institution.setInstitutionType(request.getInstitutionType());
        institution.setDescription(request.getDescription());
        institution.setAddress(request.getAddress());
        institution.setDigitalAddress(request.getDigitalAddress());
        institution.setTaxCode(request.getTaxCode());
        institution.setZipCode(request.getZipCode());
        institution.setGeographicTaxonomies(toGeographicTaxonomies(request.getGeographicTaxonomies()));
        institution.setAttributes(toAttributes(request.getAttributes()));
        institution.setRea(request.getRea());
        institution.setShareCapital(request.getShareCapital());
        institution.setBusinessRegisterPlace(request.getBusinessRegisterPlace());
        institution.setSupportEmail(request.getSupportEmail());
        institution.setSupportPhone(request.getSupportPhone());
        if (request.getPaymentServiceProvider() != null)
            institution.setPaymentServiceProvider(toPaymentServiceProvider(request.getPaymentServiceProvider()));
        if (request.getDataProtectionOfficer() != null)
            institution.setDataProtectionOfficer(toDataProtectionOfficer(request.getDataProtectionOfficer()));
        return institution;
    }

    private static BillingResponse toBillingResponse(Billing billing, Institution institution) {
        BillingResponse billingResponse = new BillingResponse();
        if (billing != null) {
            billingResponse.setVatNumber(billing.getVatNumber());
            billingResponse.setRecipientCode(billing.getRecipientCode());
            billingResponse.setPublicServices(billing.isPublicServices());
        } else if (institution.getBilling() != null) {
            billingResponse.setVatNumber(institution.getBilling().getVatNumber());
            billingResponse.setRecipientCode(institution.getBilling().getRecipientCode());
            billingResponse.setPublicServices(institution.getBilling().isPublicServices());
        }
        return billingResponse;
    }

    public static BillingResponse toBillingResponse(Onboarding onboarding, Institution institution) {
        BillingResponse billingResponse = new BillingResponse();
        if (onboarding.getBilling() != null) {
            billingResponse.setVatNumber(onboarding.getBilling().getVatNumber());
            billingResponse.setRecipientCode(onboarding.getBilling().getRecipientCode());
            billingResponse.setPublicServices(onboarding.getBilling().isPublicServices());
        } else if (institution.getBilling() != null) {
            billingResponse.setVatNumber(institution.getBilling().getVatNumber());
            billingResponse.setRecipientCode(institution.getBilling().getRecipientCode());
            billingResponse.setPublicServices(institution.getBilling().isPublicServices());
        }
        return billingResponse;
    }

    public static Billing getBillingFromOnboarding(Onboarding onboarding, Institution institution) {
        return onboarding.getBilling() != null ? onboarding.getBilling() : institution.getBilling();
    }

    public static Billing toBilling(BillingRequest billingRequest) {
        Billing billing = new Billing();
        billing.setRecipientCode(billingRequest.getRecipientCode());
        billing.setVatNumber(billingRequest.getVatNumber());
        billing.setPublicServices(billing.isPublicServices());
        return billing;
    }

    private static void addBillingData(InstitutionManagerResponse institutionManagerResponse, ProductManagerInfo manager, String productId) {
        for (Onboarding onboarding : manager.getInstitution().getOnboarding()) {
            if (productId.equalsIgnoreCase(onboarding.getProductId())) {
                institutionManagerResponse.setPricingPlan(onboarding.getPricingPlan());
                if (onboarding.getBilling() != null)
                    institutionManagerResponse.setBilling(toBillingResponse(onboarding.getBilling(), manager.getInstitution()));
            }
        }
    }

    public static DataProtectionOfficer toDataProtectionOfficer(DataProtectionOfficerRequest request) {
        DataProtectionOfficer dataProtectionOfficer = new DataProtectionOfficer();
        if (request != null) {
            dataProtectionOfficer.setAddress(request.getAddress());
            dataProtectionOfficer.setEmail(request.getEmail());
            dataProtectionOfficer.setPec(request.getPec());
        }
        return dataProtectionOfficer;
    }

    public static DataProtectionOfficerResponse toDataProtectionOfficerResponse(DataProtectionOfficer dataProtectionOfficer) {
        DataProtectionOfficerResponse response = null;
        if (dataProtectionOfficer != null) {
            response = new DataProtectionOfficerResponse();
            response.setPec(dataProtectionOfficer.getPec());
            response.setEmail(dataProtectionOfficer.getEmail());
            response.setAddress(dataProtectionOfficer.getAddress());
        }
        return response;
    }

    public static PaymentServiceProvider toPaymentServiceProvider(PaymentServiceProviderRequest request) {
        PaymentServiceProvider paymentServiceProvider = new PaymentServiceProvider();
        if (request != null) {
            paymentServiceProvider.setAbiCode(request.getAbiCode());
            paymentServiceProvider.setVatNumberGroup(request.isVatNumberGroup());
            paymentServiceProvider.setBusinessRegisterNumber(request.getBusinessRegisterNumber());
            paymentServiceProvider.setLegalRegisterNumber(request.getLegalRegisterNumber());
            paymentServiceProvider.setLegalRegisterName(request.getLegalRegisterName());
        }
        return paymentServiceProvider;
    }

    public static PaymentServiceProviderResponse toPaymentServiceProviderResponse(PaymentServiceProvider paymentServiceProvider) {
        PaymentServiceProviderResponse response = null;
        if (paymentServiceProvider != null) {
            response = new PaymentServiceProviderResponse();
            response.setAbiCode(paymentServiceProvider.getAbiCode());
            response.setLegalRegisterName(paymentServiceProvider.getLegalRegisterName());
            response.setBusinessRegisterNumber(paymentServiceProvider.getBusinessRegisterNumber());
            response.setVatNumberGroup(paymentServiceProvider.isVatNumberGroup());
            response.setLegalRegisterNumber(paymentServiceProvider.getLegalRegisterNumber());
        }
        return response;
    }

    public static List<Attributes> toAttributes(List<AttributesRequest> attributes) {
        List<Attributes> response = new ArrayList<>();
        if (attributes != null) {
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

    public static List<AttributesResponse> toAttributeResponse(List<Attributes> attributes) {
        List<AttributesResponse> list = new ArrayList<>();
        if (attributes != null && !attributes.isEmpty()) {
            for (Attributes a : attributes) {
                AttributesResponse response = new AttributesResponse();
                response.setCode(a.getCode());
                response.setOrigin(a.getOrigin());
                response.setDescription(a.getDescription());
                list.add(response);
            }
        }
        return list;
    }


    private static List<InstitutionGeographicTaxonomies> toGeographicTaxonomies(List<GeoTaxonomies> request) {
        List<InstitutionGeographicTaxonomies> response = new ArrayList<>();
        if (request != null && !request.isEmpty()) {
            for (GeoTaxonomies g : request) {
                InstitutionGeographicTaxonomies geographicTaxonomies = new InstitutionGeographicTaxonomies(g.getCode(), g.getDesc());
                response.add(geographicTaxonomies);
            }
        }
        return response;
    }

    public static List<GeoTaxonomies> toGeoTaxonomies(List<InstitutionGeographicTaxonomies> geographicTaxonomies) {
        List<GeoTaxonomies> list = new ArrayList<>();
        if (geographicTaxonomies != null) {
            for (InstitutionGeographicTaxonomies g : geographicTaxonomies) {
                GeoTaxonomies geoTaxonomies = new GeoTaxonomies();
                geoTaxonomies.setCode(g.getCode());
                geoTaxonomies.setDesc(g.getDesc());
                list.add(geoTaxonomies);
            }
        }
        return list;
    }

    private static List<String> convertToGeoString(List<InstitutionGeographicTaxonomies> geographicTaxonomies) {
        List<String> list = new ArrayList<>();
        geographicTaxonomies.forEach(g -> list.add(g.getCode()));
        return list;
    }

    public static OnboardedProducts toOnboardedProducts(List<Onboarding> page) {
        OnboardedProducts onboardedProducts = new OnboardedProducts();
        onboardedProducts.setProducts(toInstitutionProduct(page));
        return onboardedProducts;
    }

    public static List<InstitutionProduct> toInstitutionProduct(List<Onboarding> onboardings) {
        if (onboardings == null) {
            return Collections.emptyList();
        }
        return onboardings.stream().map(onboarding -> {
            InstitutionProduct product = new InstitutionProduct();
            product.setId(onboarding.getProductId());
            product.setState(onboarding.getStatus());
            return product;
        }).collect(Collectors.toList());
    }

    public static InstitutionManagementResponse toInstitutionManagementResponse(Institution institution) {
        InstitutionManagementResponse response = new InstitutionManagementResponse();
        response.setId(institution.getId());
        response.setExternalId(institution.getExternalId());
        response.setOrigin(institution.getOrigin());
        response.setOriginId(institution.getOriginId());
        response.setDescription(institution.getDescription());
        response.setInstitutionType(institution.getInstitutionType());
        response.setDigitalAddress(institution.getDigitalAddress());
        response.setAddress(institution.getAddress());
        response.setZipCode(institution.getZipCode());
        response.setTaxCode(institution.getTaxCode());
        if (institution.getOnboarding() != null) {
            response.setProducts(toProductsMap(institution.getOnboarding(), institution));
        }
        if (institution.getGeographicTaxonomies() != null) {
            response.setGeographicTaxonomies(toGeoTaxonomies(institution.getGeographicTaxonomies()));
        }
        if (institution.getAttributes() != null) {
            response.setAttributes(toAttributeResponse(institution.getAttributes()));
        }
        if (institution.getPaymentServiceProvider() != null) {
            response.setPaymentServiceProvider(toPaymentServiceProviderResponse(institution.getPaymentServiceProvider()));
        }
        if (institution.getDataProtectionOfficer() != null) {
            response.setDataProtectionOfficer(toDataProtectionOfficerResponse(institution.getDataProtectionOfficer()));
        }
        response.setRea(institution.getRea());
        response.setShareCapital(institution.getShareCapital());
        response.setBusinessRegisterPlace(institution.getBusinessRegisterPlace());
        response.setSupportEmail(institution.getSupportEmail());
        response.setSupportPhone(institution.getSupportPhone());
        response.setImported(institution.isImported());
        response.setCreatedAt(institution.getCreatedAt());
        response.setUpdatedAt(institution.getUpdatedAt());

        response.setSubunitCode(institution.getSubunitCode());
        response.setAooParentCode(institution.getAooParentCode());
        response.setSubunitType(institution.getSubunitType());

        return response;
    }

    public static List<InstitutionManagementResponse> toInstitutionListResponse(List<Institution> institutions) {
        List<InstitutionManagementResponse> list = new ArrayList<>();
        for (Institution institution : institutions) {
            InstitutionManagementResponse response = toInstitutionManagementResponse(institution);
            list.add(response);
        }
        return list;
    }

    private static Map<String, ProductsManagement> toProductsMap(List<Onboarding> onboarding, Institution institution) {
        Map<String, ProductsManagement> map = new HashMap<>();
        if (onboarding != null) {
            for (Onboarding o : onboarding) {
                ProductsManagement productsManagement = new ProductsManagement();
                productsManagement.setProduct(o.getProductId());
                productsManagement.setPricingPlan(o.getPricingPlan());
                productsManagement.setBilling(toBillingResponse(o.getBilling(), institution));
                map.put(o.getProductId(), productsManagement);
            }
        }
        return map;
    }

    public static List<InstitutionResponse> toInstitutionResponseList(List<Institution> institutions) {
        List<InstitutionResponse> responses = new ArrayList<>();
        if (!institutions.isEmpty()) {
            for (Institution institution : institutions) {
                responses.add(toInstitutionResponse(institution));
            }
        }
        return responses;
    }

    public static List<AttributesResponse> toInstitutionAttributeResponse(List<Attributes> attributes, String institutionId) {
        List<AttributesResponse> list = toAttributeResponse(attributes);
        if (list.isEmpty()) {
            throw new ResourceNotFoundException(String.format("Attributes for institution %s not found", institutionId), "0000");
        }
        return list;
    }

    public static List<InstitutionToOnboard> toInstitutionToOnboardList(List<ValidInstitution> validInstitutions) {
        return validInstitutions.stream()
                .map(InstitutionMapper::toInstitutionToOnboard)
                .collect(Collectors.toList());
    }

    public static InstitutionToOnboard toInstitutionToOnboard(ValidInstitution validInstitutions) {
        InstitutionToOnboard institution = new InstitutionToOnboard();
        institution.setDescription(validInstitutions.getDescription());
        institution.setId(validInstitutions.getId());
        return institution;
    }

    public static List<ValidInstitution> toValidInstitutions(List<InstitutionToOnboard> institutions) {
        return institutions.stream()
                .map(institutionToOnboard -> new ValidInstitution(institutionToOnboard.getId(), institutionToOnboard.getDescription()))
                .collect(Collectors.toList());
    }
}
