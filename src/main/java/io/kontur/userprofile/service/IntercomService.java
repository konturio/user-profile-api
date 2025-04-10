package io.kontur.userprofile.service;

import io.kontur.userprofile.client.IntercomClient;
import io.kontur.userprofile.dao.UserCustomRoleDao;
import io.kontur.userprofile.dao.UserDao;
import io.kontur.userprofile.model.dto.intercom.IntercomContactDto;
import io.kontur.userprofile.model.dto.intercom.IntercomContactResponseDto;
import io.kontur.userprofile.model.entity.UserCustomRole;
import io.kontur.userprofile.model.entity.user.User;
import org.jboss.logging.Logger;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class IntercomService {

    private static final Logger log = Logger.getLogger(IntercomService.class);

    private final UserCustomRoleDao userCustomRoleDao;
    private final UserDao userDao;
    private final IntercomClient intercomClient;

    public IntercomService(UserCustomRoleDao userCustomRoleDao, UserDao userDao, IntercomClient intercomClient) {
        this.userCustomRoleDao = userCustomRoleDao;
        this.userDao = userDao;
        this.intercomClient = intercomClient;
    }

    public void syncUser(User user) {
        try {
            List<UserCustomRole> roles = userCustomRoleDao.getActiveUserRoles(user);
            IntercomContactDto intercomContactDto = IntercomContactDto.fromUser(user, roles);

            IntercomContactResponseDto responseDto = intercomClient.syncContact(intercomContactDto, user.getIntercomId());

            user.setIntercomId(responseDto.getId());
            userDao.updateUser(user);

            log.infof("Successfully synced user with intercom: %s", responseDto.toString());
        } catch (Exception e) {
            log.errorf("Failed to sync user with intercom: %s", user.getId(), e);
        }

    }
}
