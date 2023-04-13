package it.pagopa.selfcare.mscore.core.util;

import it.pagopa.selfcare.mscore.constant.InstitutionType;
import it.pagopa.selfcare.mscore.constant.Origin;
import it.pagopa.selfcare.mscore.exception.InvalidRequestException;
import it.pagopa.selfcare.mscore.model.institution.Institution;
import it.pagopa.selfcare.mscore.model.institution.InstitutionGeographicTaxonomies;
import it.pagopa.selfcare.mscore.model.onboarding.OnboardingRequest;
import it.pagopa.selfcare.mscore.model.user.User;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

import static it.pagopa.selfcare.mscore.constant.GenericError.MANAGER_EMAIL_NOT_FOUND;

@Slf4j
@NoArgsConstructor(access = AccessLevel.NONE)
public class PdfMapper {

    private static final String[] PLAN_LIST = {"C1", "C2", "C3", "C4", "C5", "C6", "C7"};

    public static Map<String, Object> setUpCommonData(User validManager, List<User> users, Institution institution, OnboardingRequest request, List<InstitutionGeographicTaxonomies> geographicTaxonomies, InstitutionType institutionType) {
        log.info("START - setupCommonData");
        if (validManager.getWorkContacts() != null && validManager.getWorkContacts().containsKey(institution.getId())) {
            Map<String, Object> map = new HashMap<>();
            map.put("institutionName", institution.getDescription());
            map.put("address", institution.getAddress());
            map.put("institutionTaxCode", institution.getTaxCode());
            map.put("zipCode", institution.getZipCode());
            map.put("managerName", validManager.getName());
            map.put("managerSurname", validManager.getFamilyName());
            map.put("originId", institution.getOriginId() != null ? institution.getOriginId() : "");
            map.put("institutionMail", institution.getDigitalAddress());
            map.put("managerTaxCode", validManager.getFiscalCode());
            map.put("managerEmail", validManager.getWorkContacts().get(institution.getId()).getEmail());
            map.put("delegates", delegatesToText(users, institution.getId()));
            map.put("institutionType", decodeInstitutionType(institutionType));
            if (request.getBillingRequest() != null) {
                map.put("institutionVatNumber", request.getBillingRequest().getVatNumber());
            } else {
                map.put("institutionVatNumber", "");
            }
            if (geographicTaxonomies != null && !geographicTaxonomies.isEmpty()) {
                map.put("institutionGeoTaxonomies", geographicTaxonomies.stream().map(InstitutionGeographicTaxonomies::getDesc).collect(Collectors.toList()));
            }
            return map;
        } else {
            throw new InvalidRequestException(MANAGER_EMAIL_NOT_FOUND.getMessage(), MANAGER_EMAIL_NOT_FOUND.getCode());
        }
    }

    public static void setupPSPData(Map<String, Object> map, User validManager, Institution institution) {
        log.info("START - setupPSPData");
        if (institution.getPaymentServiceProvider() != null) {
            map.put("legalRegisterNumber", institution.getPaymentServiceProvider().getLegalRegisterNumber());
            map.put("vatNumberGroup", institution.getPaymentServiceProvider().isVatNumberGroup() ? "partita iva di gruppo" : "");
            map.put("institutionRegister", institution.getPaymentServiceProvider().getBusinessRegisterNumber());
            map.put("institutionAbi", institution.getPaymentServiceProvider().getAbiCode());
        } else if (institution.getDataProtectionOfficer() != null) {
            map.put("dataProtectionOfficerAddress", institution.getDataProtectionOfficer().getAddress());
            map.put("dataProtectionOfficerEmail", institution.getDataProtectionOfficer().getEmail());
            map.put("dataProtectionOfficerPec", institution.getDataProtectionOfficer().getPec());
        }
        if (validManager.getWorkContacts() != null && validManager.getWorkContacts().containsKey(institution.getId())) {
            map.put("managerPEC", validManager.getWorkContacts().get(institution.getId()).getEmail());
        }
    }

    public static void setupProdIOData(Map<String, Object> map, User validManager, Institution institution, OnboardingRequest request, InstitutionType institutionType) {
        log.info("START - setupProdIOData");
        map.put("institutionTypeCode", institutionType);
        map.put("pricingPlan", decodePricingPlan(request.getPricingPlan(), request.getProductId()));
        if (StringUtils.hasText(institution.getOrigin())) {
            map.put("originIdLabelValue", Origin.IPA.getValue().equalsIgnoreCase(institution.getOrigin()) ?
                    "<li class=\"c19 c39 li-bullet-0\"><span class=\"c1\">codice di iscrizione all&rsquo;Indice delle Pubbliche Amministrazioni e dei gestori di pubblici servizi (I.P.A.) <span class=\"c3\">${originId}</span> </span><span class=\"c1\"></span></li>"
                    : "");
        }
        addInstitutionRegisterLabelValue(institution, map);
        if (request.getBillingRequest() != null) {
            map.put("institutionRecipientCode", request.getBillingRequest().getRecipientCode());
        }

        String underscore = "_______________";
        map.put("GPSinstitutionName", InstitutionType.GSP == institutionType ? institution.getDescription() : underscore);
        map.put("GPSmanagerName", InstitutionType.GSP == institutionType ? validManager.getName() : underscore);
        map.put("GPSmanagerSurname", InstitutionType.GSP == institutionType ? validManager.getFamilyName() : underscore);
        map.put("GPSmanagerTaxCode", InstitutionType.GSP == institutionType ? validManager.getFiscalCode() : underscore);

        map.put("institutionREA", Optional.ofNullable(institution.getRea()).orElse(underscore));
        map.put("institutionShareCapital", Optional.ofNullable(institution.getShareCapital()).orElse(underscore));
        map.put("institutionBusinessRegisterPlace", Optional.ofNullable(institution.getBusinessRegisterPlace()).orElse(underscore));

        addPricingPlan(request, map);
    }

    private static void addPricingPlan(OnboardingRequest request, Map<String, Object> map) {
        if (StringUtils.hasText(request.getPricingPlan()) && Arrays.stream(PLAN_LIST).anyMatch(s -> s.equalsIgnoreCase(request.getPricingPlan()))) {
            map.put("pricingPlanPremium", request.getPricingPlan().replace("C", ""));
            map.put("pricingPlanPremiumCheckbox", "X");
        } else {
            map.put("pricingPlanPremium", "");
            map.put("pricingPlanPremiumCheckbox", "");
        }

        map.put("pricingPlanPremiumBase", Optional.ofNullable(request.getPricingPlan()).orElse(""));

        if (StringUtils.hasText(request.getPricingPlan()) && "C0".equalsIgnoreCase(request.getPricingPlan())) {
            map.put("pricingPlanPremiumBaseCheckbox", "X");
        } else {
            map.put("pricingPlanPremiumBaseCheckbox", "");
        }
    }

    private static void addInstitutionRegisterLabelValue(Institution institution, Map<String, Object> map) {
        if (institution.getPaymentServiceProvider() != null
                && StringUtils.hasText(institution.getPaymentServiceProvider().getBusinessRegisterNumber())) {
            map.put("number", institution.getPaymentServiceProvider().getBusinessRegisterNumber());
            map.put("institutionRegisterLabelValue", "<li class=\"c19 c39 li-bullet-0\"><span class=\"c1\">codice di iscrizione all&rsquo;Indice delle Pubbliche Amministrazioni e dei gestori di pubblici servizi (I.P.A.) <span class=\"c3\">${number}</span> </span><span class=\"c1\"></span></li>\n");
        } else {
            map.put("institutionRegisterLabelValue", "");
        }
    }

    private static String decodePricingPlan(String pricingPlan, String productId) {
        if ("FA".equals(pricingPlan)) {
            return "FAST";
        }
        if ("prod-io".equalsIgnoreCase(productId)) {
            return "BASE";
        } else {
            return "PREMIUM";
        }
    }

    private static String decodeInstitutionType(InstitutionType institutionType) {
        switch (institutionType) {
            case PA:
                return "Pubblica Amministrazione";
            case GSP:
                return "Gestore di servizi pubblici";
            case PT:
                return "Partner tecnologico";
            case SCP:
                return "Società a controllo pubblico";
            case PSP:
                return "Prestatori Servizi di Pagamento";
            default:
                return "";

        }
    }

    private static String delegatesToText(List<User> users, String institutionId) {
        StringBuilder builder = new StringBuilder();
        users.forEach(user -> {
            builder
                    .append("</br>")
                    .append("<p class=\"c141\"><span class=\"c6\">Nome e Cognome: ")
                    .append(user.getName()).append(" ")
                    .append(user.getFamilyName())
                    .append("&nbsp;</span></p>\n")
                    .append("<p class=\"c141\"><span class=\"c6\">Codice Fiscale: ")
                    .append(user.getFiscalCode())
                    .append("</span></p>\n")
                    .append("<p class=\"c141\"><span class=\"c6\">Amm.ne/Ente/Societ&agrave;: </span></p>\n")
                    .append("<p class=\"c141\"><span class=\"c6\">Qualifica/Posizione: </span></p>\n")
                    .append("<p class=\"c141\"><span class=\"c6\">e-mail: ");
            if (user.getWorkContacts() != null && user.getWorkContacts().containsKey(institutionId)) {
                builder.append(user.getWorkContacts().get(institutionId).getEmail());
            }
            builder.append("&nbsp;</span></p>\n")
                    .append("<p class=\"c141\"><span class=\"c6\">PEC: &nbsp;</span></p>\n")
                    .append("</br>");
        });
        return builder.toString();
    }
}
