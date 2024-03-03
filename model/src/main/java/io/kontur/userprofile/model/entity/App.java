package io.kontur.userprofile.model.entity;

import com.fasterxml.jackson.databind.JsonNode;
import io.kontur.userprofile.model.dto.AppDto;
import io.kontur.userprofile.model.entity.user.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "APP")
public class App {
    @Id
    private UUID id;
    private String name;
    private String description;
    @ManyToOne
    @JoinColumn(name = "owner_user_id")
    private User owner;

    @Column(name = "is_public")
    private boolean isPublic;
    private List<BigDecimal> extent;
    @Column(name = "sidebar_icon_url")
    private String sidebarIconUrl;
    @Column(name = "favicon_url")
    private String faviconUrl;
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "favicon_pack", columnDefinition = "json")
    private JsonNode faviconPack;
    @Column(name = "domains", columnDefinition = "text[]")
    private List<String> domains;

    public static App fromDto(AppDto appDto) {
        App app = new App();
        app.setId(appDto.getId());
        app.setName(appDto.getName());
        app.setDescription(appDto.getDescription());
        app.setPublic(appDto.isPublic());
        app.setExtent(appDto.getExtent());
        app.setSidebarIconUrl(appDto.getSidebarIconUrl());
        app.setFaviconUrl(appDto.getFaviconUrl());
        app.setFaviconPack(appDto.getFaviconPack());
        app.setDomains(appDto.getDomains());
        return app;
    }

    public boolean isOwnedByUsername(String username) {
        return username != null
            && getOwner() != null
            && username.equals(getOwner().getUsername());
    }
}
