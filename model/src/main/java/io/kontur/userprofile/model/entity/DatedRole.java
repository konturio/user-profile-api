package io.kontur.userprofile.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DatedRole {
    private Long roleId;
    private String roleName;
    private OffsetDateTime expiredAt;
}
