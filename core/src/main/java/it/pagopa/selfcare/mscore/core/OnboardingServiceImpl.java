package it.pagopa.selfcare.mscore.core;

import it.pagopa.selfcare.commons.base.security.SelfCareUser;
import it.pagopa.selfcare.mscore.api.UserConnector;
import it.pagopa.selfcare.mscore.api.UserRegistryConnector;
import it.pagopa.selfcare.mscore.model.OnboardingData;
import it.pagopa.selfcare.mscore.exception.ResourceNotFoundException;
import it.pagopa.selfcare.mscore.model.*;
import it.pagopa.selfcare.mscore.model.institution.GeographicTaxonomies;
import it.pagopa.selfcare.mscore.model.institution.Institution;
import it.pagopa.selfcare.mscore.model.institution.InstitutionType;
import it.pagopa.selfcare.mscore.model.institution.InstitutionUpdate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;

@Slf4j
@Service
public class OnboardingServiceImpl implements OnboardingService {

    private final ExternalService externalService;
    private final UserConnector userConnector;
    private final UserRegistryConnector userRegistryConnector;

    public OnboardingServiceImpl(ExternalService externalService, UserConnector userConnector, UserRegistryConnector userRegistryConnector) {
        this.externalService = externalService;
        this.userConnector = userConnector;
        this.userRegistryConnector = userRegistryConnector;
    }

    private final List<RelationshipState> validRelationshipStates =
            List.of(RelationshipState.ACTIVE,
                    RelationshipState.DELETED,
                    RelationshipState.SUSPENDED);

    private static User.Fields workContacts;
    private static User.Fields familyName;
    private static User.Fields name;
    private static final EnumSet<User.Fields> USER_FIELD_LIST = EnumSet.of(name, familyName, workContacts);


    @Override
    public void verifyOnboardingInfo(String externalId, String productId) {
        log.info("Verifying onboarding for institution having externalId {} on product {}", externalId, productId);
        Institution institution = externalService.getInstitutionByExternalId(externalId);
        List<OnboardedUser> response = retrieveUser(institution.getId(), productId, validRelationshipStates);
        if (response.isEmpty())
            throw new ResourceNotFoundException(String.format("Institution having externalId %s is not onboarded for product %s",externalId,productId), "");
    }

    @Override
    public void onboard(OnboardingData onboardingData, SelfCareUser selfCareUser) {
        User user = getUserById(selfCareUser.getId());
        Institution institution = externalService.getInstitutionByExternalId(onboardingData.getInstitutionExternalId());
        List<RelationshipState> states = Arrays.asList(RelationshipState.values());
        List<OnboardedUser> onboardedUser = retrieveUser(institution.getId(), null, states);
        OnboardedUser manager = checkIfManagerExists(onboardedUser, onboardingData.getProductId());
        if (onboardingData.getInstitutionUpdate().getInstitutionType().equals(InstitutionType.PA)) {
            performOnboardingWithSignatureForPA(onboardingData, institution, user, manager);
        } else {
            performOnboardingWithSignature(onboardingData, institution, user, manager);
        }
    }

    private void performOnboardingWithSignature(OnboardingData onboardingData, Institution institution, User user, OnboardedUser manager) {

    }

    private OnboardedUser verifyUsersByRoles(List<OnboardedUser> users) {
        //TODO: CHECK IF USERS != NULL E CONTIENE MANAGER O DELEGATO COME RUOLO SUL PRODOTTO
        return new OnboardedUser();
    }

    private boolean validateOverridingData(InstitutionUpdate institutionUpdate, Institution institution) {
        if(institutionUpdate!=null){
            //se è ipa vale questo return altrimenti non serve questo boolean (il controllo viene
            //fatto su origin che non è previsto nel nuovo modello (controllo se PA?)
            return institutionUpdate.getTaxCode().equalsIgnoreCase(institution.getTaxCode())
                    && institutionUpdate.getDigitalAddress().equalsIgnoreCase(institution.getDigitalAddress())
                    && institutionUpdate.getDescription().equalsIgnoreCase(institution.getDescription())
                    && institutionUpdate.getAddress().equalsIgnoreCase(institution.getAddress())
                    && institutionUpdate.getZipCode().equalsIgnoreCase(institution.getZipCode());
        }else{
            return false;
        }
    }

    private void performOnboardingWithSignatureForPA(OnboardingData onboardingData, Institution institution, User user, OnboardedUser manager) {
        if(validateOverridingData(onboardingData.getInstitutionUpdate(), institution)){
            //GET GEO TAXONOMIES
            GeographicTaxonomies geographicTaxonomies = new GeographicTaxonomies();
            OnboardedUser validUser = verifyUsersByRoles(onboardingData.getUsers());
            OnboardedUser validManager = getValidManager(manager,validUser);
            OnboardedUser createdUser = createUser(validUser, onboardingData.getProductId());
            //createUser in DB con il valid user e il productId
            //create or get relationship con il createdUser (rivedere la fase di creazione poichè non ho più
            //l'entità relazione
            OnboardedUser userRelationship = createOrGetRelationShip(createdUser);
            String contractTemplate = getFileAsString(onboardingData.getContractPath());
            String pdf = createContractPdf(contractTemplate, validManager, validUser,
                    institution, onboardingData, geographicTaxonomies);
            String digest = createSignatureDigest(pdf);
            String token = createToken(mapToOnboardingRequest(onboardingData));
            //send email


        }else {
            //TODO: GESTIONE ECCEZIONI
        }
    }

    private String createToken(OnboardingRequest mapToOnboardingRequest) {
        return "";
    }

    private OnboardingRequest mapToOnboardingRequest(OnboardingData onboardingData) {
        return new OnboardingRequest();
    }

    private String createSignatureDigest(String pdf) {
        /*    Try {
      val document: DSSDocument = new FileDocument(file)
      document.getDigest(DigestAlgorithm.SHA256)
    }*/
        return "";
    }

    private String createContractPdf(String contractTemplate, OnboardedUser validManager,
                                     OnboardedUser validUser, Institution institution,
                                     OnboardingData onboardingData, GeographicTaxonomies geographicTaxonomies) {

        return "";
    }

    private String getFileAsString(String contractPath) {
        return "";
    }

    private OnboardedUser createOrGetRelationShip(OnboardedUser createdUser) {
        return new OnboardedUser();
    }

    private OnboardedUser createUser(OnboardedUser validUser, String productId) {
        return new OnboardedUser();
    }

    private OnboardedUser getValidManager(OnboardedUser manager, OnboardedUser validUser) {
        return new OnboardedUser();
    }

    private OnboardedUser checkIfManagerExists(List<OnboardedUser> onboardedUser, String productId) {
        return new OnboardedUser();
        //check if exists product with role manager
        //check if state is one of valid states
        //return active manager
    }

    private User getUserById(String id) {
        return userRegistryConnector.getUserByInternalId(id, USER_FIELD_LIST);
    }

    private List<OnboardedUser> retrieveUser(String to, String productId, List<RelationshipState> states) {
        OnboardedUser user = new OnboardedUser();
        user.setInstitutionId(to);

        if (productId != null) {
            Product product = new Product();
            product.setProductId(productId);
            product.setRoles(List.of(PartyRole.MANAGER));

            user.setProducts(List.of(product));
        }

        return userConnector.find(user, states, productId);
    }
}
