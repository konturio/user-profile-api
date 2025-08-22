package io.kontur.userprofile.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.kontur.userprofile.dao.AccessRecordDao;
import io.kontur.userprofile.model.entity.user.User;
import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor
@Transactional
public class AccessService {
    private final AccessRecordDao accessRecordDao;

    public void newLayerBy(User user, long layerId) {
        accessRecordDao.newLayerBy(user, layerId);
    }
}
