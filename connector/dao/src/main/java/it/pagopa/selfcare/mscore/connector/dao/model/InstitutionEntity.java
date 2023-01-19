package it.pagopa.selfcare.mscore.connector.dao.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import it.pagopa.selfcare.mscore.model.institution.*;
import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.time.OffsetDateTime;
import java.util.List;

@Data
@Document("Institution")
public class InstitutionEntity {
    @MongoId
    private ObjectId id;
    private String externalId;
    private String description;
    private String ipaCode;
    private InstitutionType institutionType;
    private String digitalAddress;
    private String address;
    private String zipCode;
    private String taxCode;
    private BillingEntity billing; //required

    private List<Onboarding> onboarding;

    @JsonProperty("geographicTaxonomyCodes")
    private List<GeographicTaxonomies> geographicTaxonomies;

    private List<Attributes> attributes;

    private PaymentServiceProvider paymentServiceProvider; //optional

    private DataProtectionOfficer dataProtectionOfficer; //optional

    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;

}
