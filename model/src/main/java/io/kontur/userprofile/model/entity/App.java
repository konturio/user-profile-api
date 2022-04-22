package io.kontur.userprofile.model.entity;

import io.kontur.userprofile.model.converters.GeoJsonUtils;
import io.kontur.userprofile.model.dto.AppDto;
import io.kontur.userprofile.model.entity.user.User;
import java.math.BigDecimal;
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
import org.locationtech.jts.geom.Geometry;

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
    @Column(name = "center_geometry")
    private Geometry centerGeometry;
    private BigDecimal zoom;

    public static App fromDto(AppDto appDto) {
        App app = new App();
        app.setId(appDto.getId());
        app.setName(appDto.getName());
        app.setDescription(appDto.getDescription());
        app.setPublic(appDto.isPublic());
        Geometry entityGeometry = GeoJsonUtils.toEntity(appDto.getCenterGeometry());
        app.setCenterGeometry(entityGeometry);
        app.setZoom(appDto.getZoom());
        return app;
    }

    public boolean isOwnedByUsername(String username) {
        return username != null
            && getOwner() != null
            && username.equals(getOwner().getUsername());
    }
}
