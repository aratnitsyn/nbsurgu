package ru.ras.nbsurgu.telegram.database.service;

import org.hibernate.Session;
import org.jetbrains.annotations.NotNull;

import ru.ras.nbsurgu.telegram.database.dao.GenericDAOImpl;
import ru.ras.nbsurgu.telegram.database.entity.TakenBookEntity;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.Calendar;
import java.util.List;

public class TakenBookService {
    private static volatile TakenBookService instance;

    private static volatile GenericDAOImpl<TakenBookEntity, Long> takenBookDao = new GenericDAOImpl<>(TakenBookEntity.class);

    public static TakenBookService getInstance() {
        if (instance == null) {
            synchronized (TakenBookService.class) {
                if (instance == null) {
                    instance = new TakenBookService();
                }
            }
        }

        return instance;
    }

    public @NotNull List<TakenBookEntity> read(final long userId) {
        final Session session = takenBookDao.openCurrentSessionWithTransaction();
        final CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        final CriteriaQuery<TakenBookEntity> criteriaQuery = criteriaBuilder.createQuery(TakenBookEntity.class);

        final Root<TakenBookEntity> takenBookEntityRoot = criteriaQuery.from(TakenBookEntity.class);

        criteriaQuery.select(takenBookEntityRoot);
        criteriaQuery.where(criteriaBuilder.equal(takenBookEntityRoot.get("userId"), userId));

        final List<TakenBookEntity> takenBookEntities = session.createQuery(criteriaQuery).getResultList();

        takenBookDao.closeCurrentSessionWithTransaction();

        return takenBookEntities;
    }

    public @NotNull List<TakenBookEntity> readOverdue() {
        final Session session = takenBookDao.openCurrentSessionWithTransaction();
        final CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        final CriteriaQuery<TakenBookEntity> criteriaQuery = criteriaBuilder.createQuery(TakenBookEntity.class);

        final Root<TakenBookEntity> takenBookEntityRoot = criteriaQuery.from(TakenBookEntity.class);

        criteriaQuery.select(takenBookEntityRoot);

        final Calendar calendar = Calendar.getInstance();

        calendar.add(Calendar.DATE, 7);

        criteriaQuery.where(
                criteriaBuilder.lessThanOrEqualTo(takenBookEntityRoot.get("dateReturn"), calendar.getTime())
        );

        final List<TakenBookEntity> takenBookEntities = session.createQuery(criteriaQuery).getResultList();

        takenBookDao.closeCurrentSessionWithTransaction();

        return takenBookEntities;
    }

}