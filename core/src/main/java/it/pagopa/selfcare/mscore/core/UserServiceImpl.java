package it.pagopa.selfcare.mscore.core;

import it.pagopa.selfcare.commons.base.security.PartyRole;
import it.pagopa.selfcare.mscore.api.UserConnector;
import it.pagopa.selfcare.mscore.api.UserRegistryConnector;
import it.pagopa.selfcare.mscore.constant.CustomErrorEnum;
import it.pagopa.selfcare.mscore.exception.InvalidRequestException;
import it.pagopa.selfcare.mscore.exception.ResourceNotFoundException;
import it.pagopa.selfcare.mscore.model.*;
import it.pagopa.selfcare.mscore.model.institution.Institution;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.EnumSet;
import java.util.List;

import static it.pagopa.selfcare.mscore.constant.CustomErrorEnum.*;
import static it.pagopa.selfcare.mscore.constant.CustomErrorEnum.RELATIONSHIP_NOT_SUSPENDABLE;
import static it.pagopa.selfcare.mscore.core.util.UtilEnumList.ADMIN_PARTY_ROLE;
import static it.pagopa.selfcare.mscore.core.util.UtilEnumList.ONBOARDING_INFO_DEFAULT_RELATIONSHIP_STATES;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserConnector userConnector;
    private final OnboardingDao onboardingDao;
    private final InstitutionService institutionService;
    private final UserRegistryConnector userRegistryConnector;

    public UserServiceImpl(UserConnector userConnector, OnboardingDao onboardingDao, InstitutionService institutionService, UserRegistryConnector userRegistryConnector) {
        this.userConnector = userConnector;
        this.onboardingDao = onboardingDao;
        this.institutionService = institutionService;
        this.userRegistryConnector = userRegistryConnector;
    }

    @Override
    public OnboardedUser findOnboardedManager(String institutionId, String productId, List<RelationshipState> active) {
        return userConnector.findOnboardedManager(institutionId, productId, List.of(RelationshipState.ACTIVE));
    }

    @Override
    public OnboardedUser findByUserId(String userId) {
        return userConnector.getById(userId);
    }

    @Override
    public List<OnboardedUser> retrieveAdminUsers(String institutionId, String userId) {
        return userConnector.findWithFilter(institutionId, userId, ADMIN_PARTY_ROLE, ONBOARDING_INFO_DEFAULT_RELATIONSHIP_STATES, null, null);
    }

    @Override
    public List<OnboardedUser> retrieveUsers(String institutionId, String personId, List<PartyRole> roles, List<RelationshipState> states, List<String> products, List<String> productRoles) {
        return userConnector.findWithFilter(institutionId, personId, roles, states, products, productRoles);
    }

    @Override
    public boolean checkIfAdmin(String userId, String institutionId) {
        return !userConnector.findAdminWithFilter(userId, institutionId, ADMIN_PARTY_ROLE, List.of(RelationshipState.ACTIVE)).isEmpty();
    }

    @Override
    public OnboardedUser findByRelationshipId(String relationshipId) {
        return userConnector.findByRelationshipId(relationshipId);
    }

    @Override
    public void verifyUser(String userId) {
        OnboardedUser user = findByUserId(userId);
        if (user == null) {
            throw new ResourceNotFoundException(String.format(CustomErrorEnum.USER_NOT_FOUND_ERROR.getMessage(), userId), CustomErrorEnum.USER_NOT_FOUND_ERROR.getCode());
        }
    }

    @Override
    public void activateRelationship(String relationshipId) {
        OnboardedUser user = findByRelationshipId(relationshipId);
        try {
            onboardingDao.updateUserProductState(user, relationshipId, List.of(RelationshipState.SUSPENDED), RelationshipState.ACTIVE);
        } catch (InvalidRequestException e) {
            throw new InvalidRequestException(String.format(RELATIONSHIP_NOT_ACTIVABLE.getMessage(), relationshipId), RELATIONSHIP_NOT_ACTIVABLE.getCode());
        }
    }

    @Override
    public void suspendRelationship(String relationshipId) {
        OnboardedUser user = findByRelationshipId(relationshipId);
        try {
            onboardingDao.updateUserProductState(user, relationshipId, List.of(RelationshipState.ACTIVE), RelationshipState.SUSPENDED);
        } catch (InvalidRequestException e) {
            throw new InvalidRequestException(String.format(RELATIONSHIP_NOT_SUSPENDABLE.getMessage(), relationshipId), RELATIONSHIP_NOT_SUSPENDABLE.getCode());
        }
    }

    @Override
    public void deleteRelationship(String relationshipId) {
        OnboardedUser user = findByRelationshipId(relationshipId);
        onboardingDao.updateUserProductState(user, relationshipId, List.of(RelationshipState.values()), RelationshipState.DELETED);
    }

    @Override
    public RelationshipInfo retrieveRelationship(String relationshipId) {
        OnboardedUser user = findByRelationshipId(relationshipId);
        for (UserBinding userBinding : user.getBindings()) {
            for (OnboardedProduct product : userBinding.getProducts()) {
                if (relationshipId.equalsIgnoreCase(product.getRelationshipId())) {
                    Institution institution = institutionService.retrieveInstitutionById(userBinding.getInstitutionId());
                    return new RelationshipInfo(institution, user.getId(), product);
                }
            }
        }
        throw new InvalidRequestException(String.format(RELATIONSHIP_ID_NOT_FOUND.getMessage(), relationshipId), RELATIONSHIP_ID_NOT_FOUND.getCode());
    }

    @Override
    public User getUserFromUserRegistry(String userId, EnumSet<User.Fields> fields) {
        return userRegistryConnector.getUserByInternalId(userId, fields);
    }
}
