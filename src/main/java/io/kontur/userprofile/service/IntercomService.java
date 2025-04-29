package io.kontur.userprofile.service;

import io.kontur.userprofile.client.IntercomClient;
import io.kontur.userprofile.dao.UserCustomRoleDao;
import io.kontur.userprofile.dao.UserDao;
import io.kontur.userprofile.model.dto.intercom.*;
import io.kontur.userprofile.model.entity.UserCustomRole;
import io.kontur.userprofile.model.entity.user.User;
import org.jboss.logging.Logger;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

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
            Optional<String> intercomId = resolveIntercomId(user);

            List<UserCustomRole> roles = userCustomRoleDao.getActiveUserRoles(user);
            IntercomContactDto contactDto = IntercomContactDto.fromUserAndRoles(user, roles);

            IntercomContactResponseDto responseDto = intercomId.isPresent()
                    ? intercomClient.updateContact(contactDto, intercomId.get())
                    : intercomClient.createContact(contactDto);

            user.setIntercomId(responseDto.getId());
            userDao.updateUser(user);

            log.infof("Successfully synced user with intercom: %s", responseDto.toString());
        } catch (Exception e) {
            log.errorf("Failed to sync user with intercom: %s", user.getId(), e);
        }
    }

    private Optional<String> resolveIntercomId(User user) {
        if (user.getIntercomId() != null && !user.getIntercomId().isBlank()) {
            return Optional.of(user.getIntercomId());
        }

        IntercomContactSearchDto<String> contactSearchDto = new IntercomContactSearchDto<>(
                new IntercomContactSearchQueryDto<>("email", "=", user.getEmail()));
        List<IntercomContactResponseDto> contacts = intercomClient.searchContacts(contactSearchDto).getData();

        if (contacts == null || contacts.isEmpty()) {
            return Optional.empty();
        }

        return contacts.stream()
                .max(Comparator.comparing(
                        IntercomContactResponseDto::getSignedUpAt,
                        Comparator.nullsLast(Comparator.naturalOrder())
                ))
                .map(IntercomContactResponseDto::getId);
    }
}
