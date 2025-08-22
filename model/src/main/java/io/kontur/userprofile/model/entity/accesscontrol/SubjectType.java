package io.kontur.userprofile.model.entity.accesscontrol;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "enum_subject_types")
public class SubjectType extends EnumBase {
    // no additional fields
}
