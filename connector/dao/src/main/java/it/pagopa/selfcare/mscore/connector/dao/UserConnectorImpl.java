package it.pagopa.selfcare.mscore.connector.dao;

import it.pagopa.selfcare.mscore.api.UserConnector;
import it.pagopa.selfcare.mscore.connector.dao.model.ProductEntity;
import it.pagopa.selfcare.mscore.connector.dao.model.UserEntity;
import it.pagopa.selfcare.mscore.model.PartyRole;
import it.pagopa.selfcare.mscore.model.Product;
import it.pagopa.selfcare.mscore.model.RelationshipState;
import it.pagopa.selfcare.mscore.model.OnboardedUser;
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

    public List<OnboardedUser> find(OnboardedUser user, List<RelationshipState> validRelationshipStates, String productId) {
        Query query = new Query();
        query.addCriteria(
                new Criteria().andOperator(
                        Criteria.where("institutionId").is(user.getInstitutionId()),
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
    public Optional<OnboardedUser> findById(String id) {
        return repository.findById(new ObjectId(id)).map(this::convertToUser);
    }

    @Override
    public void deleteById(String id) {
        repository.deleteById(new ObjectId(id));
    }

    private OnboardedUser convertToUser(UserEntity entity) {
        OnboardedUser user = new OnboardedUser();
        user.setUser(entity.getId().toString());
        user.setInstitutionId(entity.getInstitutionId());
        user.setProducts(convertToProduct(entity.getProducts()));
        return user;
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
        entity.setInstitutionId(user.getInstitutionId());
        entity.setProducts(convertToProductEntity(user.getProducts()).toArray(ProductEntity[]::new));
        return entity;
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
