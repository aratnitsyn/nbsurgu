package ru.ras.nbsurgu.telegram.database.service;

import org.hibernate.Session;
import org.hibernate.query.Query;
import org.jetbrains.annotations.NotNull;
import ru.ras.nbsurgu.telegram.database.dao.GenericDAOImpl;
import ru.ras.nbsurgu.telegram.database.entity.CodeEntity;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.Optional;

public class CodeService {

    private static volatile CodeService instance;

    private static volatile GenericDAOImpl<CodeEntity, Long> codeDao = new GenericDAOImpl<>(CodeEntity.class);

    private CodeService() {
    }

    public static CodeService getInstance() {
        if (instance == null) {
            synchronized (CodeService.class) {
                if (instance == null) {
                    instance = new CodeService();
                }
            }
        }

        return instance;
    }

    public void create(final CodeEntity entity) {
        codeDao.openCurrentSessionWithTransaction();
        codeDao.create(entity);
        codeDao.closeCurrentSessionWithTransaction();
    }

    public void update(final CodeEntity entity) {
        codeDao.openCurrentSessionWithTransaction();
        codeDao.update(entity);
        codeDao.closeCurrentSessionWithTransaction();
    }

    public void delete(final CodeEntity entity) {
        codeDao.openCurrentSessionWithTransaction();
        codeDao.delete(entity);
        codeDao.closeCurrentSessionWithTransaction();
    }

    public void delete(final long tgId) {
        final Session session = codeDao.openCurrentSessionWithTransaction();
        final CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        final CriteriaQuery<CodeEntity> criteriaQuery = criteriaBuilder.createQuery(CodeEntity.class);

        final Root<CodeEntity> root = criteriaQuery.from(CodeEntity.class);

        criteriaQuery.select(root);
        criteriaQuery.where(criteriaBuilder.equal(root.get("tgId"), tgId));

        final Query<CodeEntity> query = session.createQuery(criteriaQuery);
        final Optional<CodeEntity> optional = query.uniqueResultOptional();

        optional.ifPresent(codeEntity -> codeDao.delete(codeEntity));

        codeDao.closeCurrentSessionWithTransaction();
    }

    @NotNull
    public Optional<CodeEntity> read(final long tgId) {
        final Session session = codeDao.openCurrentSessionWithTransaction();
        final CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        final CriteriaQuery<CodeEntity> criteriaQuery = criteriaBuilder.createQuery(CodeEntity.class);

        final Root<CodeEntity> root = criteriaQuery.from(CodeEntity.class);

        criteriaQuery.select(root);
        criteriaQuery.where(criteriaBuilder.equal(root.get("tgId"), tgId));

        final Query<CodeEntity> query = session.createQuery(criteriaQuery);
        final Optional<CodeEntity> optional = query.uniqueResultOptional();

        codeDao.closeCurrentSessionWithTransaction();

        return optional;
    }

}