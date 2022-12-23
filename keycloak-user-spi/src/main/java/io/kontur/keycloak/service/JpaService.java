package io.kontur.keycloak.service;

import java.util.Optional;
import java.util.stream.Stream;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import lombok.extern.jbosslog.JBossLog;

@JBossLog
public abstract class JpaService<T> {
    @PersistenceContext(unitName = "io.kontur.userprofile")
    protected EntityManager entityManager;

    public long count(Class<T> entityClazz) {
        return (long) entityManager.createQuery("select count(*) from " + entityClazz.getSimpleName() + " u")
            .getSingleResult();
    }

    public Stream<T> getAll(Class<T> entityClazz) {
        return entityManager.createQuery("from " + entityClazz.getSimpleName() + " u",
            entityClazz).getResultStream();
    }

    protected Optional<T> queryEntityBy(Class<T> entityClazz, String field, String value) {
        if (field == null || field.isBlank() || value == null || value.isBlank()) {
            return Optional.empty();
        }
        return Optional.of(entityManager
            .createQuery("from " + entityClazz.getSimpleName() + " u where u." + field
                + " = ?1", entityClazz)
            .setParameter(1, value).getSingleResult());
    }

    protected void handleLookupException(Class<T> entityClazz, Exception e) {
        if (!(e instanceof NoResultException)) {
            log.warnf("Caught exception on %s lookup: %s", entityClazz.getSimpleName(),
                e.getMessage(), e);
        }
    }
}
