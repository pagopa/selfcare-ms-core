package it.pagopa.selfcare.mscore.core;

import it.pagopa.selfcare.mscore.api.UserConnector;
import it.pagopa.selfcare.mscore.exception.ResourceNotFoundException;
import it.pagopa.selfcare.mscore.model.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

import static it.pagopa.selfcare.mscore.constant.CustomErrorEnum.*;

@Service
@Slf4j
public class UserServiceImpl implements UserService{

    private final UserConnector userConnector;

    public UserServiceImpl(UserConnector userConnector) {
        this.userConnector = userConnector;
    }

    @Override
    public OnboardedUser findOnboardedManager(String externalId, String productId, List<RelationshipState> active) {
        List<OnboardedUser> list = userConnector.findOnboardedManager(externalId, productId, List.of(RelationshipState.ACTIVE));
        if (list != null && !list.isEmpty()) {
            log.debug("retrieve first element of tokenList --> id: {}",list.get(0).getId());
            return list.get(0);
        }
        throw new ResourceNotFoundException(String.format(GET_INSTITUTION_MANAGER_NOT_FOUND.getMessage(), externalId, productId),
                GET_INSTITUTION_MANAGER_NOT_FOUND.getCode());
    }

    @Override
    public OnboardedUser findByUserId(String userId){
        return userConnector.getById(userId);
    }
}
