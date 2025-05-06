package io.kontur.userprofile.model.dto.intercom;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.kontur.userprofile.model.entity.DatedRole;
import io.kontur.userprofile.model.entity.user.User;
import lombok.*;

import java.time.OffsetDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@JsonIgnoreProperties(ignoreUnknown = true)
public class IntercomContactCustomAttributesDto {

    private final static String KONTUR_ATLAS_TRIAL_ROLE_NAME = "kontur_atlas_trial";
    private final static String KONTUR_ATLAS_EDU_ROLE_NAME = "kontur_atlas_edu";
    private final static String KONTUR_ATLAS_PRO_ROLE_NAME = "kontur_atlas_pro";

    private String source;
    private String linkedin;
    private Boolean callConsentGiven;
    private String companyName;
    private String position;
    private String amountOfGis;
    private Boolean konturAtlasTrial;
    private Long konturAtlasTrialEndedAt;
    private Boolean konturAtlasEdu;
    private Long konturAtlasEduEndedAt;
    private Boolean konturAtlasPro;
    private Long konturAtlasProEndedAt;

    public static IntercomContactCustomAttributesDto fromUserAndRoles(User user, List<DatedRole> roles) {
        return IntercomContactCustomAttributesDto.builder()
                .source("user-profile-service")
                .linkedin(user.getLinkedin())
                .callConsentGiven(user.isCallConsentGiven())
                .companyName(user.getCompanyName())
                .position(user.getPosition())
                .amountOfGis(user.getAmountOfGis())
                .konturAtlasTrial(isRoleActive(roles, KONTUR_ATLAS_TRIAL_ROLE_NAME))
                .konturAtlasTrialEndedAt(getRoleEndedAt(roles, KONTUR_ATLAS_TRIAL_ROLE_NAME))
                .konturAtlasEdu(isRoleActive(roles, KONTUR_ATLAS_EDU_ROLE_NAME))
                .konturAtlasEduEndedAt(getRoleEndedAt(roles, KONTUR_ATLAS_EDU_ROLE_NAME))
                .konturAtlasPro(isRoleActive(roles, KONTUR_ATLAS_PRO_ROLE_NAME))
                .konturAtlasProEndedAt(getRoleEndedAt(roles, KONTUR_ATLAS_PRO_ROLE_NAME))
                .build();
    }

    private static boolean isRoleActive(List<DatedRole> roles, String roleName) {
        return roles.stream().anyMatch(r -> roleName.equals(r.getRoleName()));
    }

    private static Long getRoleEndedAt(List<DatedRole> roles, String roleName) {
        List<DatedRole> filteredRoles = roles.stream().filter(r -> roleName.equals(r.getRoleName())).toList();

        if (filteredRoles.stream().anyMatch(r -> r.getExpiredAt() == null)) {
            return null;
        }

        return filteredRoles.stream()
                .map(DatedRole::getExpiredAt)
                .map(OffsetDateTime::toEpochSecond)
                .max(Long::compareTo)
                .orElse(null);
    }
}
