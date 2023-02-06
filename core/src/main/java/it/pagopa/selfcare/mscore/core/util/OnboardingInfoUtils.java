package it.pagopa.selfcare.mscore.core.util;

import it.pagopa.selfcare.mscore.exception.ResourceNotFoundException;
import it.pagopa.selfcare.mscore.model.Product;
import it.pagopa.selfcare.mscore.model.RelationshipState;
import it.pagopa.selfcare.mscore.model.institution.Institution;
import it.pagopa.selfcare.mscore.model.institution.Onboarding;
import lombok.extern.slf4j.Slf4j;

import java.util.*;
import java.util.stream.Collectors;

import static it.pagopa.selfcare.mscore.constant.CustomErrorEnum.ONBOARDING_INFO_INSTITUTION_NOT_FOUND;
import static it.pagopa.selfcare.mscore.core.util.UtilEnumList.onboardingInfoDefaultRelationshipStates;

@Slf4j
public class OnboardingInfoUtils {

    private OnboardingInfoUtils() {
    }

    public static List<RelationshipState> convertStatesToRelationshipsState(String[] states) {
        return Arrays.stream(states)
                .map(RelationshipState::valueOf)
                .collect(Collectors.toList());
    }

    public static Map<String, Map<String, Product>> getUserInstitutionsWithProductStatusIn(Map<String, Map<String, Product>> userInstitutionToBeFiltered, List<RelationshipState> relationshipStateList) {
        Map<String, Map<String, Product>> filteredUserInstitutionMap = new HashMap<>();
        userInstitutionToBeFiltered.forEach((institutionId, productMap) -> {
            Map<String, Product> filteredProductsMap = filterProductsMapByStates(productMap, relationshipStateList);
            if (!filteredProductsMap.isEmpty()) {
                filteredUserInstitutionMap.put(institutionId, filteredProductsMap);
            }
        });
        if (filteredUserInstitutionMap.isEmpty()) {
            //Non ci sono institutions con prodotti aventi status = status di input
            log.info("No onboarding information found for states {}", relationshipStateList);
            throw new ResourceNotFoundException(String.format(ONBOARDING_INFO_INSTITUTION_NOT_FOUND.getMessage(), "states : " + relationshipStateList.toString()), ONBOARDING_INFO_INSTITUTION_NOT_FOUND.getCode());
        }
        return filteredUserInstitutionMap;
    }

    private static Map<String, Product> filterProductsMapByStates(Map<String, Product> productsMap, List<RelationshipState> states) {
        if (productsMap == null)
            return new HashMap<>();

        return productsMap.entrySet()
                .stream()
                .filter(map -> isStatusIn(map.getValue().getStatus(), states))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    public static boolean isStatusIn(RelationshipState status, List<RelationshipState> states) {
        return states.stream().anyMatch(status::equals);
    }

    public static List<Onboarding> findOnboardingLinkedToProductWithStateIn(Map<String, Product> productsMap, Institution onboardedInstitution, List<RelationshipState> relationshipStateList) {
        List<Onboarding> onboardingList = new ArrayList<>();
        productsMap.forEach((productId, product) -> {
            Optional<Onboarding> onboarding = getOnboardingFromInstitutionByProductIdAndState(onboardedInstitution, productId, relationshipStateList);
            onboarding.ifPresent(onboardingList::add);
        });

        return onboardingList;
    }

    private static Optional<Onboarding> getOnboardingFromInstitutionByProductIdAndState(Institution institution, String productId, List<RelationshipState> states) {
        return institution.getOnboarding()
                .stream()
                .filter(onboarding -> onboarding.getProductId().equalsIgnoreCase(productId) && isStatusIn(onboarding.getStatus(), states))
                .findAny();
    }

    public static List<RelationshipState> getRelationShipStateList(String[] states) {
        if (states == null || states.length == 0) {
            return onboardingInfoDefaultRelationshipStates;
        } else {
            return convertStatesToRelationshipsState(states);
        }
    }
}
