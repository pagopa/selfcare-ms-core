package it.pagopa.selfcare.mscore.connector.rest;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import it.pagopa.selfcare.mscore.connector.rest.client.ProductsRestClient;
import it.pagopa.selfcare.mscore.constant.InstitutionType;
import it.pagopa.selfcare.mscore.model.product.Product;
import it.pagopa.selfcare.mscore.model.product.ProductStatus;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

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
        Product product = new Product();
        product.setContractTemplatePath("Contract Template Path");
        product.setContractTemplateVersion("1.0.2");
        product.setId("42");
        product.setParentId("42");
        product.setRoleMappings(null);
        product.setStatus(ProductStatus.ACTIVE);
        product.setTitle("Dr");
        when(productsRestClient.getProductById((String) any(), (InstitutionType) any())).thenReturn(product);
        assertSame(product, productConnectorImpl.getProductById("42"));
        verify(productsRestClient).getProductById((String) any(), (InstitutionType) any());
    }

    /**
     * Method under test: {@link ProductConnectorImpl#getProductById(String)}
     */
    @Test
    @Disabled("TODO: Complete this test")
    void testGetProductById2() {
        // TODO: Complete this test.
        //   Reason: R013 No inputs found that don't throw a trivial exception.
        //   Diffblue Cover tried to run the arrange/act section, but the method under
        //   test threw
        //   java.lang.IllegalArgumentException: A productId is required
        //       at it.pagopa.selfcare.mscore.connector.rest.ProductConnectorImpl.getProductById(ProductConnectorImpl.java:24)
        //   See https://diff.blue/R013 to resolve this issue.

        Product product = new Product();
        product.setContractTemplatePath("Contract Template Path");
        product.setContractTemplateVersion("1.0.2");
        product.setId("42");
        product.setParentId("42");
        product.setRoleMappings(null);
        product.setStatus(ProductStatus.ACTIVE);
        product.setTitle("Dr");
        when(productsRestClient.getProductById((String) any(), (InstitutionType) any())).thenReturn(product);
        productConnectorImpl.getProductById("");
    }
}

