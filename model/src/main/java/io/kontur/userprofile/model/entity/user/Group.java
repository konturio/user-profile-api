package io.kontur.userprofile.model.entity.user;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class Group {
    @NotNull
    @Column(name = "group_id")
    private String id;
    @NotNull
    private String name;
}
