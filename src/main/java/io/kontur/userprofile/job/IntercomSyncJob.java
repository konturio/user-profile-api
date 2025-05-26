package io.kontur.userprofile.job;

import io.kontur.userprofile.dao.UserDao;
import io.kontur.userprofile.service.IntercomService;
import org.jboss.logging.Logger;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@ConditionalOnProperty(value = "intercom.enabled")
public class IntercomSyncJob {

    private static final Logger log = Logger.getLogger(IntercomSyncJob.class);

    private final UserDao userDao;
    private final IntercomService intercomService;

    public IntercomSyncJob(UserDao userDao, IntercomService intercomService) {
        this.userDao = userDao;
        this.intercomService = intercomService;
    }

    @Transactional
    public void syncAllUsersToIntercom() {
        log.info("Started Intercom users sync job");
        userDao.selectUsersForUpdate().forEach(intercomService::syncUser);
        log.info("Finished Intercom users sync job");
    }
}
