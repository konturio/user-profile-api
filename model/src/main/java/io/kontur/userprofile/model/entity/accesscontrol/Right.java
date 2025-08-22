package io.kontur.userprofile.model.entity.accesscontrol;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "enum_rights")
public class Right extends EnumBase {
    // no additional fields
}
