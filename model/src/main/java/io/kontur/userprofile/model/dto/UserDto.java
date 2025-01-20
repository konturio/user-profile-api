package io.kontur.userprofile.model.dto;

import io.kontur.userprofile.model.entity.user.User;
import lombok.*;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class UserDto {
    private String username;
    private String email;
    private String fullName;
    private String language;
    private boolean useMetricUnits;
    private boolean subscribedToKonturUpdates;
    private String bio;
    private String osmEditor;
    private String defaultFeed;
    private String theme;
    private String linkedin;
    private String phone;

    public static UserDto fromEntity(User user) {
        return user == null ? null :
                new UserDto(user.getUsername(), user.getEmail(), user.getFullName(), user.getLanguage(),
                        user.isUseMetricUnits(), user.isSubscribedToKonturUpdates(), user.getBio(),
                        user.getOsmEditor(), user.getDefaultFeed(), user.getTheme(), user.getLinkedin(),
                        user.getPhone());
    }
}
