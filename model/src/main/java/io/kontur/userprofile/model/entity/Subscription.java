package io.kontur.userprofile.model.entity;

import java.util.Objects;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Embeddable
@Data
public class Subscription {
    protected boolean enabled;
    @NotNull
    protected String eventFeed;
    protected String bbox;
    protected String type; //todo enum
    protected String severity; //todo enum
    protected Long people;

    public Subscription(boolean enabled, @NotNull String eventFeed, String bbox, String type,
                        String severity, Long people) {
        this.enabled = enabled;
        this.eventFeed = eventFeed;
        this.bbox = bbox;
        this.type = type;
        this.severity = severity;
        this.people = people;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Subscription that)) {
            return false;
        }
        return enabled == that.enabled && eventFeed.equals(that.eventFeed)
            && Objects.equals(bbox, that.bbox) && Objects.equals(type, that.type)
            && Objects.equals(severity, that.severity)
            && Objects.equals(people, that.people);
    }

    @Override
    public int hashCode() {
        return Objects.hash(enabled, eventFeed, bbox, type, severity, people);
    }

    @Override
    public String toString() {
        return "Subscription{"
            + "enabled=" + enabled
            + ", eventFeed='" + eventFeed
            + ", bbox='" + bbox
            + ", type='" + type
            + ", severity='" + severity
            + ", people=" + people + '}';
    }
}
