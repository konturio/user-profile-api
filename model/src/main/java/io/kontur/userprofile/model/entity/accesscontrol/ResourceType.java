package io.kontur.userprofile.model.entity.accesscontrol;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "enum_resource_types")
public class ResourceType extends EnumBase {
    // no additional fields
}
