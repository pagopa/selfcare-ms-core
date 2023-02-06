package it.pagopa.selfcare.mscore.model;

import lombok.Data;

@Data
public class MessageRequest {

    private MailTemplate mailTemplate;
    private String receiverEmail;

}
