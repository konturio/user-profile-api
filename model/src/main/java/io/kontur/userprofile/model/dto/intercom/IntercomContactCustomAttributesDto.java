package io.kontur.userprofile.model.dto.intercom;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.kontur.userprofile.model.entity.UserCustomRole;
import io.kontur.userprofile.model.entity.user.User;
import lombok.*;

import java.time.OffsetDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
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

    public static IntercomContactCustomAttributesDto fromUserAndRoles(User user, List<UserCustomRole> roles) {
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

    private static boolean isRoleActive(List<UserCustomRole> roles, String roleName) {
        return roles.stream().anyMatch(r -> roleName.equals(r.getRole().getName()));
    }

    private static Long getRoleEndedAt(List<UserCustomRole> roles, String roleName) {
        List<UserCustomRole> filteredRoles = roles.stream().filter(r -> roleName.equals(r.getRole().getName())).toList();

        if (filteredRoles.stream().anyMatch(r -> r.getEndedAt() == null)) {
            return null;
        }

        return filteredRoles.stream()
                .map(UserCustomRole::getEndedAt)
                .map(OffsetDateTime::toEpochSecond)
                .max(Long::compareTo)
                .orElse(null);
    }
}
