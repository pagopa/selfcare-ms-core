package it.pagopa.selfcare.mscore.connector.rest;

import it.pagopa.selfcare.commons.base.logging.LogUtils;
import it.pagopa.selfcare.mscore.api.ProductConnector;
import it.pagopa.selfcare.mscore.connector.rest.client.ProductsRestClient;
import it.pagopa.selfcare.mscore.model.product.Product;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

@Slf4j
@Service
public class ProductConnectorImpl implements ProductConnector {

    private final ProductsRestClient restClient;

    public ProductConnectorImpl(ProductsRestClient productsRestClient) {
        this.restClient = productsRestClient;
    }

    @Override
    public Product getProductById(String productId) {
       /* log.trace("getProductById start");
        log.debug(LogUtils.CONFIDENTIAL_MARKER, "getProductById productId = {}",productId);
        Assert.hasText(productId, "A productId is required");
        Product result = restClient.getProductById(productId, null);
        log.debug(LogUtils.CONFIDENTIAL_MARKER, "getProductById result = {}", result);
        log.trace("getProductById end");
        return result;*/

        Product product = new Product();
        product.setId("prod-interop");
        product.setTitle("prod-interop");
        product.setContractTemplatePath("contracts/template/interop/2.0.0/interop-accordo_di_adesione-v.2.0.0.html");
        return product;
    }
}
