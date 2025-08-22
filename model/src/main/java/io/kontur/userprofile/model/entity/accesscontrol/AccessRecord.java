package io.kontur.userprofile.model.entity.accesscontrol;

import java.time.OffsetDateTime;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import io.kontur.userprofile.model.entity.user.User;


@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@Entity
@Table(name = "access_records")
public class AccessRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @NotNull
    @Column(name = "allow")
    private Boolean allow;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "right_id")
    private Right right;

    @NotNull
    @Column(name = "resource_id")
    private Long resourceId;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "resource_type")
    private ResourceType resourceType;

    @NotNull
    @Column(name = "subject_id")
    private Long subjectId;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "subject_type")
    private SubjectType subjectType;

    @Builder.Default
    @Column(name = "created_at")
    private OffsetDateTime createdAt = OffsetDateTime.now();
}
