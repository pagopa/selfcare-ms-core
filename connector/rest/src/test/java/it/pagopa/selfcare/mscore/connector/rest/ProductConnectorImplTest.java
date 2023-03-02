package it.pagopa.selfcare.mscore.connector.rest;

import it.pagopa.selfcare.mscore.model.product.Product;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
class ProductConnectorImplTest {
    @InjectMocks
    private ProductConnectorImpl productConnectorImpl;

    /**
     * Method under test: {@link ProductConnectorImpl#getProductById(String)}
     */
    @Test
    void testGetProductById() {
        Product actualProductById = productConnectorImpl.getProductById("42");
        assertEquals("contracts/template/interop/2.0.0/interop-accordo_di_adesione-v.2.0.0.html",
                actualProductById.getContractTemplatePath());
        assertEquals("prod-interop", actualProductById.getTitle());
        assertEquals("prod-interop", actualProductById.getId());
    }
}

