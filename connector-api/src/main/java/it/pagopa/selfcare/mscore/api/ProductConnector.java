package it.pagopa.selfcare.mscore.api;


import it.pagopa.selfcare.mscore.model.product.Product;

public interface ProductConnector {
    Product getProductById(String productId);
}
