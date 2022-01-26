package io.kontur.userprofile.model.dto;

import io.kontur.userprofile.model.entity.User;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class UserDto {
    private String username;
    private String email;
    private String firstName;
    private String lastName;
    private String language;
    private boolean useMetricUnits;
    private boolean subscribedToKonturUpdates;

    public static UserDto fromEntity(User user) {
        return user == null ? null :
            new UserDto(user.getUsername(),
                user.getEmail(), user.getFirstName(), user.getLastName(),
                user.getLanguage(), user.isUseMetricUnits(), user.isSubscribedToKonturUpdates());
    }
}
