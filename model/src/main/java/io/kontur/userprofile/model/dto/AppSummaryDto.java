package io.kontur.userprofile.model.dto;

import io.kontur.userprofile.model.entity.App;
import java.util.UUID;
import lombok.Data;

@Data
public class AppSummaryDto {
    private final UUID id;
    private final String name;

    public static AppSummaryDto fromEntity(App app) {
        return new AppSummaryDto(app.getId(), app.getName());
    }
}
