package io.kontur.userprofile.model.dto.intercom;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.kontur.userprofile.model.entity.UserCustomRole;
import io.kontur.userprofile.model.entity.user.User;
import lombok.*;

import java.time.OffsetDateTime;
import java.util.List;

@Data
@ToString
@EqualsAndHashCode
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
    private Long konturAtlasTrialStartedAt;
    private Long konturAtlasTrialEndedAt;
    private Boolean konturAtlasEdu;
    private Boolean konturAtlasPro;

    public static IntercomContactCustomAttributesDto fromUser(User user, List<UserCustomRole> roles) {
        return IntercomContactCustomAttributesDto.builder()
                .source("user-profile-service")
                .linkedin(user.getLinkedin())
                .callConsentGiven(user.isCallConsentGiven())
                .companyName(user.getCompanyName())
                .position(user.getPosition())
                .amountOfGis(user.getAmountOfGis())
                .konturAtlasTrial(roles.stream().anyMatch(r -> r.getRole().getName().equals(KONTUR_ATLAS_TRIAL_ROLE_NAME)))
                .konturAtlasTrialStartedAt(roles.stream()
                        .filter(r -> r.getRole().getName().equals(KONTUR_ATLAS_TRIAL_ROLE_NAME))
                        .map(UserCustomRole::getStartedAt)
                        .min(OffsetDateTime::compareTo)
                        .map(OffsetDateTime::toEpochSecond)
                        .orElse(null))
                .konturAtlasTrialEndedAt(roles.stream()
                        .filter(r -> r.getRole().getName().equals(KONTUR_ATLAS_TRIAL_ROLE_NAME))
                        .map(UserCustomRole::getEndedAt)
                        .max(OffsetDateTime::compareTo)
                        .map(OffsetDateTime::toEpochSecond)
                        .orElse(null))
                .konturAtlasEdu(roles.stream().anyMatch(r -> r.getRole().getName().equals(KONTUR_ATLAS_EDU_ROLE_NAME)))
                .konturAtlasPro(roles.stream().anyMatch(r -> r.getRole().getName().equals(KONTUR_ATLAS_PRO_ROLE_NAME)))
                .build();
    }
}
