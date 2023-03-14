package it.pagopa.selfcare.mscore.core;

import it.pagopa.selfcare.mscore.exception.InvalidRequestException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(SpringExtension.class)
class ClassPathStreamFactoryTest {

    @InjectMocks
    private ClassPathStreamFactory classPathStreamFactory;

    @Test
    void getUrl() {
        assertNotNull(classPathStreamFactory.getUrl("url"));
    }


    @Test
    void getUrl2() {
        assertThrows(InvalidRequestException.class, () -> classPathStreamFactory.getUrl("a b c"));
    }


}

