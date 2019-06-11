package ru.ras.nbsurgu.telegram.database.service;

import org.hibernate.Session;
import org.hibernate.query.Query;
import org.jetbrains.annotations.NotNull;
import ru.ras.nbsurgu.telegram.database.dao.GenericDAOImpl;
import ru.ras.nbsurgu.telegram.database.entity.AuthorizationEntity;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.Optional;

public class AuthorizationService {

    private static volatile AuthorizationService instance;

    private static volatile GenericDAOImpl<AuthorizationEntity, Long> authorizationDao = new GenericDAOImpl<>(AuthorizationEntity.class);

    private AuthorizationService() {}

    public static AuthorizationService getInstance() {
        if (instance == null) {
            synchronized (AuthorizationService.class) {
                if (instance == null) {
                    instance = new AuthorizationService();
                }
            }
        }

        return instance;
    }

    public void create(final AuthorizationEntity entity) {
        authorizationDao.openCurrentSessionWithTransaction();
        authorizationDao.create(entity);
        authorizationDao.closeCurrentSessionWithTransaction();
    }

    public @NotNull Optional<AuthorizationEntity> read(final long tgId) {
        final Session session = authorizationDao.openCurrentSessionWithTransaction();
        final CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        final CriteriaQuery<AuthorizationEntity> criteriaQuery = criteriaBuilder.createQuery(AuthorizationEntity.class);

        final Root<AuthorizationEntity> root = criteriaQuery.from(AuthorizationEntity.class);

        criteriaQuery.select(root);
        criteriaQuery.where(criteriaBuilder.equal(root.get("tgId"), tgId));

        final Query<AuthorizationEntity> query = session.createQuery(criteriaQuery);
        final Optional<AuthorizationEntity> optional = query.uniqueResultOptional();

        authorizationDao.closeCurrentSessionWithTransaction();

        return optional;
    }

    public void delete(final AuthorizationEntity entity) {
        authorizationDao.openCurrentSessionWithTransaction();
        authorizationDao.delete(entity);
        authorizationDao.closeCurrentSessionWithTransaction();
    }

}