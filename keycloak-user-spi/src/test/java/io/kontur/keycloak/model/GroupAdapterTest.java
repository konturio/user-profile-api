package io.kontur.keycloak.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import io.kontur.userprofile.model.entity.Group;
import org.junit.jupiter.api.Test;

public class GroupAdapterTest {

    @Test
    public void getIdNameTest() {
        Group entity = new Group();
        entity.setId("some-keycloak-id");
        entity.setName("some-name");
        GroupAdapter adapter = GroupAdapter.fromEntity(entity);

        assertEquals(entity.getId(), adapter.getId());
        assertEquals(entity.getName(), adapter.getName());
    }

    @Test
    public void equalsTest() {
        String id = "some-keycloak-id";

        Group entity1 = new Group();
        entity1.setId(id);
        entity1.setName("name 1");

        Group entity2 = new Group();
        entity2.setId(id);
        entity2.setName("name 2");

        GroupAdapter adapter1 = GroupAdapter.fromEntity(entity1);
        GroupAdapter adapter2 = GroupAdapter.fromEntity(entity2);

        assertEquals(adapter1, adapter2);
    }

}
