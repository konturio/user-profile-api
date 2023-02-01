package io.kontur.userprofile.model.entity;

import io.hypersistence.utils.hibernate.type.array.ListArrayType;
import io.kontur.userprofile.model.dto.AppDto;
import io.kontur.userprofile.model.entity.user.User;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "APP")
@TypeDef(name = "list-array", typeClass = ListArrayType.class)
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

    public static App fromDto(AppDto appDto) {
        App app = new App();
        app.setId(appDto.getId());
        app.setName(appDto.getName());
        app.setDescription(appDto.getDescription());
        app.setPublic(appDto.isPublic());
        app.setExtent(appDto.getExtent());
        app.setSidebarIconUrl(appDto.getSidebarIconUrl());
        app.setFaviconUrl(appDto.getFaviconUrl());
        return app;
    }

    public boolean isOwnedByUsername(String username) {
        return username != null
            && getOwner() != null
            && username.equals(getOwner().getUsername());
    }
}
