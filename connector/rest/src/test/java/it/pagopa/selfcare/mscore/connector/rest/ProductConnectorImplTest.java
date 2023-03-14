package it.pagopa.selfcare.mscore.connector.rest;

import it.pagopa.selfcare.mscore.connector.rest.client.ProductsRestClient;
import it.pagopa.selfcare.mscore.model.product.Product;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
class ProductConnectorImplTest {
    @InjectMocks
    private ProductConnectorImpl productConnectorImpl;

    @Mock
    private ProductsRestClient restClient;

    /**
     * Method under test: {@link ProductConnectorImpl#getProductById(String)}
     */
    @Test
    void testGetProductById() {
        when(restClient.getProductById(any(),any())).thenReturn(new Product());
        assertNotNull(productConnectorImpl.getProductById("42"));
    }
}

