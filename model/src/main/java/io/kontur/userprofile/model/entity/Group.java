package io.kontur.userprofile.model.entity;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;
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
