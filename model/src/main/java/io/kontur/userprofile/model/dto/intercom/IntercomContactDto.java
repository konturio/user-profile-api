package io.kontur.userprofile.model.dto.intercom;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.kontur.userprofile.model.entity.UserCustomRole;
import io.kontur.userprofile.model.entity.user.User;
import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@JsonIgnoreProperties(ignoreUnknown = true)
public class IntercomContactDto {
    private String role;
    private String email;
    private String phone;
    private String name;
    private Long signedUpAt;
    private Boolean unsubscribedFromEmails;
    private IntercomContactCustomAttributesDto customAttributes;

    public static IntercomContactDto fromUserAndRoles(User user, List<UserCustomRole> roles) {
        return IntercomContactDto.builder()
                .role("user")
                .email(user.getEmail())
                .phone(user.getPhone())
                .name(user.getFullName())
                .signedUpAt(user.getCreatedAt().toEpochSecond())
                .unsubscribedFromEmails(!user.isSubscribedToKonturUpdates())
                .customAttributes(IntercomContactCustomAttributesDto.fromUserAndRoles(user, roles))
                .build();
    }
}
