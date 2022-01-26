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
public class UserSummaryDto {
    private String username;
    private String email;

    public static UserSummaryDto fromEntity(User user) {
        return new UserSummaryDto(user.getUsername(), user.getEmail());
    }
}
