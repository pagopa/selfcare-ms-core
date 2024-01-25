package it.pagopa.selfcare.mscore.web.model.token;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaginatedTokenResponse {
    private List<ScContractResponse> items;
}
