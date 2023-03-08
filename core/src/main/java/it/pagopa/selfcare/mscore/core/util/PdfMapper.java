package it.pagopa.selfcare.mscore.core.util;

import it.pagopa.selfcare.mscore.exception.InvalidRequestException;
import it.pagopa.selfcare.mscore.model.onboarding.OnboardingRequest;
import it.pagopa.selfcare.mscore.model.user.User;
import it.pagopa.selfcare.mscore.model.institution.GeographicTaxonomies;
import it.pagopa.selfcare.mscore.model.institution.Institution;
import it.pagopa.selfcare.mscore.constant.InstitutionType;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static it.pagopa.selfcare.mscore.constant.GenericError.MANAGER_EMAIL_NOT_FOUND;

@Slf4j
public class PdfMapper {

    private PdfMapper() {
    }

    public static Map<String, Object> setUpCommonData(User validManager, List<User> users, Institution institution, OnboardingRequest request, List<GeographicTaxonomies> geographicTaxonomies) {
        log.info("START - setupCommonData");
        if (validManager.getEmail() != null) {
            Map<String, Object> map = new HashMap<>();
            map.put("institutionName", institution.getDescription());
            map.put("address", institution.getAddress());
            map.put("institutionTaxCode", institution.getTaxCode());
            map.put("zipCode", institution.getZipCode());
            map.put("managerName", validManager.getName());
            map.put("managerSurname", validManager.getFamilyName());
            map.put("originId", institution.getOriginId()!=null ? institution.getOriginId():"");
            map.put("institutionMail", institution.getDigitalAddress());
            map.put("managerTaxCode", validManager.getFiscalCode());
            map.put("managerEmail", validManager.getEmail());
            map.put("delegates", delegatesToText(users));
            map.put("institutionType", decodeInstitutionType(retrieveInstitutionType(institution, request)));
            if (request.getBillingRequest() != null) {
                map.put("institutionVatNumber", request.getBillingRequest().getVatNumber());
            } else {
                map.put("institutionVatNumber", "");
            }
            if (geographicTaxonomies != null && !geographicTaxonomies.isEmpty()) {
                map.put("institutionGeoTaxonomies", geographicTaxonomies.stream().map(GeographicTaxonomies::getDesc).collect(Collectors.toList()));
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
        map.put("managerPEC", validManager.getEmail());
    }

    public static void setupProdIOData(Map<String, Object> map, User validManager, Institution institution, OnboardingRequest request) {
        log.info("START - setupProdIOData");
        map.put("institutionTypeCode", retrieveInstitutionType(institution, request).name());
        map.put("pricingPlan", decodePricingPlan(request.getPricingPlan(), request.getProductId()));
        map.put("originIdLabelValue", InstitutionType.PA == institution.getInstitutionType() ?
                "|<li class=\"c19 c39 li-bullet-0\"><span class=\"c1\">codice di iscrizione all&rsquo;Indice delle Pubbliche Amministrazioni e dei gestori di pubblici servizi (I.P.A.) <span class=\"c3\">${institution.originId}</span> </span><span class=\"c1\"></span></li>|"
                : ""); //solo se è IPA
        if (InstitutionType.PA == institution.getInstitutionType()) {
            map.put("institutionRegisterLabelValue", "<li class=\"c19 c39 li-bullet-0\"><span class=\"c1\">codice di iscrizione all&rsquo;Indice delle Pubbliche Amministrazioni e dei gestori di pubblici servizi (I.P.A.) <span class=\"c3\">$number</span> </span><span class=\"c1\"></span></li>\n");
        } else {
            map.put("institutionRegisterLabelValue", "");
        }

        if (request.getBillingRequest() != null) {
            map.put("institutionRecipientCode", request.getBillingRequest().getRecipientCode());
        }

        String underscore = "_______________";
        map.put("GPSinstitutionName", InstitutionType.GSP == retrieveInstitutionType(institution, request) ? institution.getDescription() : underscore);
        map.put("GPSmanagerName", InstitutionType.GSP == retrieveInstitutionType(institution, request) ? validManager.getName() : underscore);
        map.put("GPSmanagerSurname", InstitutionType.GSP == retrieveInstitutionType(institution, request) ? validManager.getFamilyName() : underscore);
        map.put("GPSmanagerTaxCode", InstitutionType.GSP == retrieveInstitutionType(institution, request) ? validManager.getFiscalCode() : underscore);
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

    private static InstitutionType retrieveInstitutionType(Institution institution, OnboardingRequest request) {
        InstitutionType institutionType = InstitutionType.UNKNOWN;
        if (institution.getInstitutionType() != null) {
            institutionType = institution.getInstitutionType();
        }else if(request.getInstitutionUpdate() != null && request.getInstitutionUpdate().getInstitutionType() != null){
            institutionType = request.getInstitutionUpdate().getInstitutionType();
        }
        return institutionType;
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

    private static String delegatesToText(List<User> users) {
        StringBuilder builder = new StringBuilder();
        users.forEach(user -> builder
                        .append("|<p class=\"c141\"><span class=\"c6\">Nome e Cognome: ")
                        .append(user.getName()).append(" ")
                        .append(user.getFamilyName())
                        .append("&nbsp;</span></p>\n")
                        .append("|<p class=\"c141\"><span class=\"c6\">Codice Fiscale: ")
                        .append(user.getFiscalCode())
                        .append("</span></p>\n")
                        .append("|<p class=\"c141\"><span class=\"c6\">Amm.ne/Ente/Societ&agrave;: </span></p>\n")
                        .append("|<p class=\"c141\"><span class=\"c6\">Qualifica/Posizione: </span></p>\n")
                        .append("|<p class=\"c141\"><span class=\"c6\">e-mail: ")
                        .append(user.getEmail())
                        .append("&nbsp;</span></p>\n")
                        .append("|<p class=\"c141\"><span class=\"c6\">PEC: &nbsp;</span></p>\n")
                        .append("|</br>"));

        return builder.toString();
    }
}
