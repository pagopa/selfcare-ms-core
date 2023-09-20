package it.pagopa.selfcare.mscore.model.user;

import it.pagopa.selfcare.commons.base.security.SelfCareAuthority;
import it.pagopa.selfcare.mscore.constant.RelationshipState;
import it.pagopa.selfcare.mscore.model.onboarding.OnboardedProduct;
import lombok.Data;

import java.util.EnumSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Data
public class UserInfo {

    private String id;
    private User user;
    private List<OnboardedProduct> products;

    @Data
    public static class UserInfoFilter {
        private Optional<SelfCareAuthority> role = Optional.empty();
        private Optional<String> productId = Optional.empty();
        private Optional<Set<String>> productRoles = Optional.empty();
        private Optional<String> userId = Optional.empty();
        private Optional<EnumSet<RelationshipState>> allowedStates = Optional.empty();

        public void setRole(Optional<SelfCareAuthority> role) {
            this.role = role == null ? Optional.empty() : role;
        }

        public void setProductId(Optional<String> productId) {
            this.productId = productId == null ? Optional.empty() : productId;
        }

        public void setProductRoles(Optional<Set<String>> productRoles) {
            this.productRoles = productRoles == null ? Optional.empty() : productRoles;
        }

        public void setUserId(Optional<String> userId) {
            this.userId = userId == null ? Optional.empty() : userId;
        }

        public void setAllowedState(Optional<EnumSet<RelationshipState>> allowedStates) {
            this.allowedStates = allowedStates == null ? Optional.empty() : allowedStates;
        }
    }
}
