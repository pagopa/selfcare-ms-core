package it.pagopa.selfcare.mscore.connector.rest;

import it.pagopa.selfcare.mscore.connector.rest.client.ProductsRestClient;
import it.pagopa.selfcare.mscore.model.product.Product;
import it.pagopa.selfcare.mscore.model.product.ProductStatus;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.*;

@ContextConfiguration(classes = {ProductConnectorImpl.class})
@ExtendWith(SpringExtension.class)
class ProductConnectorImplTest {
    @Autowired
    private ProductConnectorImpl productConnectorImpl;

    @MockBean
    private ProductsRestClient productsRestClient;

    /**
     * Method under test: {@link ProductConnectorImpl#getProductById(String)}
     */
    @Test
    void testGetProductById() {
        Product product = dummyProduct();
        when(productsRestClient.getProductById(any(), any())).thenReturn(product);
        assertSame(product, productConnectorImpl.getProductById("42"));
        verify(productsRestClient).getProductById(any(), any());
    }

    @Test
    void testGetProductValid() {
        Product product = dummyProduct();
        when(productsRestClient.getProductValid(any())).thenReturn(product);
        assertSame(product, productConnectorImpl.getProductValidById("42"));
        verify(productsRestClient).getProductValid(any());
    }

    private Product dummyProduct(){
        Product product = new Product();
        product.setContractTemplatePath("Contract Template Path");
        product.setContractTemplateVersion("1.0.2");
        product.setId("42");
        product.setParentId("42");
        product.setRoleMappings(null);
        product.setStatus(ProductStatus.ACTIVE);
        product.setTitle("Dr");
        return product;
    }
}

