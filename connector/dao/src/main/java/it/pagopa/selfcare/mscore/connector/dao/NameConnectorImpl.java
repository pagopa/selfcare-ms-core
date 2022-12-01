package it.pagopa.selfcare.mscore.connector.dao;

//import it.pagopa.selfcare.mscore.connector.api.NameConnector;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NameConnectorImpl /*implements NameConnector*/ {//TODO change Name

    private final NameRepository repository;//TODO change Name


    @Autowired
    public NameConnectorImpl(NameRepository repository) {
        this.repository = repository;
    }

    // delegate methods to repository

}
