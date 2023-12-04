package io.kontur.userprofile.model.entity;

import com.fasterxml.jackson.databind.JsonNode;
import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import com.vladmihalcea.hibernate.type.json.JsonStringType;
import io.hypersistence.utils.hibernate.type.array.ListArrayType;
import io.kontur.userprofile.model.dto.AppDto;
import io.kontur.userprofile.model.entity.user.User;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import javax.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "APP")
@TypeDefs({
        @TypeDef(name = "json", typeClass = JsonStringType.class),
        @TypeDef(name = "jsonb", typeClass = JsonBinaryType.class),
        @TypeDef(name = "list-array", typeClass = ListArrayType.class)
})
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
    @Type(type = "list-array")
    private List<BigDecimal> extent;
    @Column(name = "sidebar_icon_url")
    private String sidebarIconUrl;
    @Column(name = "favicon_url")
    private String faviconUrl;
    @Type(type = "jsonb")
    @Column(name = "favicon_pack", columnDefinition = "json")
    private JsonNode faviconPack;
    @Type(type = "list-array")
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
