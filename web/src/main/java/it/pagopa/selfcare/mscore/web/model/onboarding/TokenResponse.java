package it.pagopa.selfcare.mscore.web.model.onboarding;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class TokenResponse {
    private String id;
    private String checksum;
    private List<LegalsResponse> legals;

    public TokenResponse(String id) {
        this.id = id;
    }
}
