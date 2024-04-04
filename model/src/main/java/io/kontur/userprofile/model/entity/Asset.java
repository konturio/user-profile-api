package io.kontur.userprofile.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.NotNull;


import java.time.OffsetDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "assets", uniqueConstraints = {@UniqueConstraint(columnNames = {"app_id", "filename", "language"})})
public class Asset {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(nullable = false, name = "media_type")
    private String mediaType;

    @NotNull
    @Column(nullable = false, name = "media_subtype")
    private String mediaSubtype;

    @NotNull
    @Column(nullable = false)
    private String filename;

    @Column
    private String description;

    @Column(name = "owner_user_id")
    private Long ownerUserId;

    @Column
    private String language;

    @NotNull
    @Column(name = "last_updated", nullable = false)
    private OffsetDateTime lastUpdated;

    @ManyToOne
    @JoinColumn(name = "app_id", insertable = false, updatable = false)
    private App app;

    @ManyToOne
    @JoinColumn(name = "feature_id", insertable = false, updatable = false)
    private Feature feature;

    @NotNull
    @Lob
    @Column(name = "asset", nullable = false)
    private byte[] asset;
}