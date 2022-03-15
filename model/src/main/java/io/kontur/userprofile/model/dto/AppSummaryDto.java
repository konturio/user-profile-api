package io.kontur.userprofile.model.dto;

import io.kontur.userprofile.model.entity.App;
import java.util.UUID;
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
@AllArgsConstructor
public class AppSummaryDto {
    private final UUID id;
    private final String name;

    public static AppSummaryDto fromEntity(App app) {
        return new AppSummaryDto(app.getId(), app.getName());
    }
}
