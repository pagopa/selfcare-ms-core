package it.pagopa.selfcare.mscore.connector.dao;

import it.pagopa.selfcare.mscore.api.UserConnector;
import it.pagopa.selfcare.mscore.connector.dao.model.ProductEntity;
import it.pagopa.selfcare.mscore.connector.dao.model.UserEntity;
import it.pagopa.selfcare.mscore.connector.dao.model.UserInstitutionEntity;
import it.pagopa.selfcare.mscore.exception.ResourceNotFoundException;
import it.pagopa.selfcare.mscore.model.*;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Example;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.UntypedExampleMatcher;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserConnectorImpl implements UserConnector {

    private final UserRepository repository;

    public UserConnectorImpl(UserRepository userRepository) {
        this.repository = userRepository;
    }

    public List<OnboardedUser> findForVerifyOnboardingInfo(String institutionId, List<RelationshipState> validRelationshipStates, String productId) {
        Query query = new Query();
        query.addCriteria(
                new Criteria().andOperator(
                        Criteria.where("institutionId").is(institutionId),
                        Criteria.where("products").exists(true),
                        Criteria.where("products")
                                .elemMatch(Criteria.where("status").in(validRelationshipStates)
                                        .and("productId").is(productId)
                                        .and("roles").is(PartyRole.MANAGER))
                )
        );
        return repository.find(query, UserEntity.class).stream()
                .map(this::convertToUser)
                .collect(Collectors.toList());

    }

    @Override
    public List<OnboardedUser> findForGetOnboardingInfo(String userId, String institutionId, List<RelationshipState> validRelationshipStates) {
        Query query = new Query();
        query.addCriteria(
                new Criteria().andOperator(
                        Criteria.where("id").is(new ObjectId(userId)),
                        Criteria.where("institutions").exists(true),
                        Criteria.where("institutions")
                                .elemMatch(Criteria.where("products").exists(true)
                                        .and("products")
                                        .elemMatch(Criteria.where("status").in(validRelationshipStates))),
                        buildCriteriaForInstitution(institutionId)
                )

        );

        return repository.find(query, UserEntity.class).stream()
                .map(this::convertToUser)
                .collect(Collectors.toList());
    }

    private Criteria buildCriteriaForInstitution(String institutionId) {
        if (institutionId != null) {
            return Criteria.where("institutionId").is(institutionId);
        } else {
            return null;
        }
    }

    @Override
    public OnboardedUser save(OnboardedUser user) {
        final UserEntity entity = convertToUserEntity(user);
        return convertToUser(repository.save(entity));
    }

    @Override
    public List<OnboardedUser> findAll() {
        return repository.findAll().stream()
                .map(this::convertToUser)
                .collect(Collectors.toList());
    }

    @Override
    public List<OnboardedUser> findAll(OnboardedUser user) {
        Example<UserEntity> example = Example.of(convertToUserEntity(user), UntypedExampleMatcher.matching());
        return repository.findAll(example).stream()
                .map(this::convertToUser)
                .collect(Collectors.toList());
    }

    @Override
    public boolean existsById(String id) {
        return repository.existsById(new ObjectId(id));
    }

    @Override
    public OnboardedUser findById(String id) {
        Optional<UserEntity> opt = repository.findById(new ObjectId(id));
        if (opt.isPresent())
            return convertToUser(opt.get());
        else
            throw new ResourceNotFoundException("", "");
    }

    @Override
    public void deleteById(String id) {
        repository.deleteById(new ObjectId(id));
    }

    private OnboardedUser convertToUser(UserEntity entity) {
        OnboardedUser user = new OnboardedUser();
        user.setUser(entity.getId().toString());
        user.setInstitutions(convertToUserInstitution(entity.getInstitutions()));
        return user;
    }

    private List<UserInstitution> convertToUserInstitution(List<UserInstitutionEntity> institutions) {
        List<UserInstitution> userInstitutions = new ArrayList<>();
        for(UserInstitutionEntity u : institutions){
            UserInstitution userInstitution = new UserInstitution();
            userInstitution.setInstitutionId(u.getInstitutionId());
            userInstitution.setProducts(convertToProduct(u.getProducts()));
            userInstitutions.add(userInstitution);
        }
        return userInstitutions;
    }

    private List<Product> convertToProduct(ProductEntity[] products) {
        List<Product> list = new ArrayList<>();
        for (ProductEntity productEntity : products) {
            Product product = new Product();
            product.setProductId(productEntity.getProductId());
            product.setContract(productEntity.getContract());
            product.setRoles(productEntity.getRoles());
            product.setStatus(productEntity.getStatus());
            list.add(product);
        }
        return list;
    }

    private UserEntity convertToUserEntity(OnboardedUser user) {
        UserEntity entity = new UserEntity();
        entity.setId(new ObjectId(user.getUser()));
        entity.setInstitutions(convertToUserInstitutionEntity(user.getInstitutions()));
        return entity;
    }

    private List<UserInstitutionEntity> convertToUserInstitutionEntity(List<UserInstitution> institutions) {
        List<UserInstitutionEntity> list = new ArrayList<>();
        for(UserInstitution u: institutions){
            UserInstitutionEntity userInstitution = new UserInstitutionEntity();
            userInstitution.setInstitutionId(u.getInstitutionId());
            userInstitution.setProducts(convertToProductEntity(u.getProducts()).toArray(ProductEntity[]::new));
            list.add(userInstitution);
        }
        return list;
    }

    private List<ProductEntity> convertToProductEntity(List<Product> products) {
        List<ProductEntity> list = new ArrayList<>();
        for (Product product : products) {
            ProductEntity productEntity = new ProductEntity();
            productEntity.setProductId(product.getProductId());
            productEntity.setContract(product.getContract());
            productEntity.setRoles(product.getRoles());
            productEntity.setStatus(product.getStatus());
            list.add(productEntity);
        }
        return list;
    }
}
