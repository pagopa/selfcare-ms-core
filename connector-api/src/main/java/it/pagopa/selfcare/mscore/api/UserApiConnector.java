package it.pagopa.selfcare.mscore.api;


import java.util.List;

public interface UserApiConnector {

  List<String> getUserEmails(String institutionId, String productId);

}
