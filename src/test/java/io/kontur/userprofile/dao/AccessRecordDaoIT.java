package io.kontur.userprofile.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import io.kontur.userprofile.AbstractIT;
import io.kontur.userprofile.model.entity.accesscontrol.AccessRecord;
import io.kontur.userprofile.model.entity.user.User;


@SpringBootTest
@Transactional
public class AccessRecordDaoIT extends AbstractIT {
    @Autowired
    UserDao userDao;

    @Autowired
    AccessRecordDao accessRecordDao;

    // This test should also ensure all the required records are in the DB
    @Test
    public void newLayerByTest() {
        final long layerId = 42;
        final User user = createUser();
        givenUserIsAuthenticated(user);

        accessRecordDao.newLayerBy(user, layerId);

        final List<AccessRecord> records = accessRecordDao.getAllAccessRecords();
        assertTrue(
            records.stream().anyMatch(
                r -> r.getSubjectId() == user.getId() && r.getResourceId() == layerId && r.getRight().getName().equals("owner")
            ));
    }
}
