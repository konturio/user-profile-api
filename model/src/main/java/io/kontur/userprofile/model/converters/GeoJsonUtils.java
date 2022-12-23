package io.kontur.userprofile.model.converters;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.locationtech.jts.geom.Geometry;
import org.wololo.jts2geojson.GeoJSONReader;
import org.wololo.jts2geojson.GeoJSONWriter;

public class GeoJsonUtils {
    private static final GeoJSONReader geoJSONReader = new GeoJSONReader();
    private static final GeoJSONWriter geoJSONWriter = new GeoJSONWriter();
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static Geometry toEntity(org.wololo.geojson.Geometry geometry) {
        return geometry == null ? null : geoJSONReader.read(geometry);
    }

    public static org.wololo.geojson.Geometry fromEntity(Geometry geometry) {
        return geometry == null ? null : geoJSONWriter.write(geometry);
    }

    public static boolean geometriesAreEqual(org.wololo.geojson.Geometry geometry1,
                                             org.wololo.geojson.Geometry geometry2) {
        return objectMapper.valueToTree(geometry1).equals(objectMapper.valueToTree(geometry2));
    }
}
