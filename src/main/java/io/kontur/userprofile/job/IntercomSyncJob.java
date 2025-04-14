package io.kontur.userprofile.job;

import io.kontur.userprofile.dao.UserDao;
import io.kontur.userprofile.service.IntercomService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@ConditionalOnProperty(value = "intercom.enabled")
public class IntercomSyncJob {
    private final UserDao userDao;
    private final IntercomService intercomService;

    public IntercomSyncJob(UserDao userDao, IntercomService intercomService) {
        this.userDao = userDao;
        this.intercomService = intercomService;
    }

    @Scheduled(cron = "0 0 2 * * *") // Runs daily at 2 AM
    public void syncAllUsersToIntercom() {
        userDao.getAllUsers().forEach(intercomService::syncUser);
    }
}
