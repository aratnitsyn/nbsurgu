package ru.ras.nbsurgu.telegram.database.service;

import org.hibernate.Session;
import org.hibernate.query.Query;
import ru.ras.nbsurgu.telegram.database.dao.GenericDAOImpl;
import ru.ras.nbsurgu.telegram.database.entity.UserEntity;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.Optional;

public class UserService {

    private static volatile UserService instance;

    private static volatile GenericDAOImpl<UserEntity, Long> userDao = new GenericDAOImpl<>(UserEntity.class);

    public static UserService getInstance() {
        if (instance == null) {
            synchronized (UserService.class) {
                if (instance == null) {
                    instance = new UserService();
                }
            }
        }

        return instance;
    }

    public void update(final UserEntity entity) {
        userDao.openCurrentSessionWithTransaction();
        userDao.update(entity);
        userDao.closeCurrentSessionWithTransaction();
    }

    public Optional<UserEntity> read(final long numberLibraryCard) {
        final Session session = userDao.openCurrentSessionWithTransaction();
        final CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        final CriteriaQuery<UserEntity> criteriaQuery = criteriaBuilder.createQuery(UserEntity.class);

        final Root<UserEntity> userEntityRoot = criteriaQuery.from(UserEntity.class);

        criteriaQuery.select(userEntityRoot);
        criteriaQuery.where(criteriaBuilder.equal(userEntityRoot.get("numberLibraryCard"), numberLibraryCard));

        final Query<UserEntity> userEntityQuery = session.createQuery(criteriaQuery);
        final Optional<UserEntity> userEntityOptional = userEntityQuery.uniqueResultOptional();

        userDao.closeCurrentSessionWithTransaction();

        return userEntityOptional;
    }

    public Optional<UserEntity> read(final String phoneNumber) {
        final Session session = userDao.openCurrentSessionWithTransaction();
        final CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        final CriteriaQuery<UserEntity> criteriaQuery = criteriaBuilder.createQuery(UserEntity.class);

        final Root<UserEntity> userEntityRoot = criteriaQuery.from(UserEntity.class);

        criteriaQuery.select(userEntityRoot);
        criteriaQuery.where(criteriaBuilder.equal(userEntityRoot.get("phoneNumber"), phoneNumber));

        final Query<UserEntity> userEntityQuery = session.createQuery(criteriaQuery);
        final Optional<UserEntity> userEntityOptional = userEntityQuery.uniqueResultOptional();

        userDao.closeCurrentSessionWithTransaction();

        return userEntityOptional;
    }

}