package ru.ras.nbsurgu.telegram.database.service;

import org.hibernate.Session;
import ru.ras.nbsurgu.telegram.database.dao.GenericDAOImpl;
import ru.ras.nbsurgu.telegram.database.entity.OrderBookEntity;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

public class OrderBookService {
    private static volatile OrderBookService instance;

    private static volatile GenericDAOImpl<OrderBookEntity, Long> orderBookDao = new GenericDAOImpl<>(OrderBookEntity.class);

    public static OrderBookService getInstance() {
        if (instance == null) {
            synchronized (OrderBookService.class) {
                if (instance == null) {
                    instance = new OrderBookService();
                }
            }
        }

        return instance;
    }

    public void create(final OrderBookEntity entity) {
        orderBookDao.openCurrentSessionWithTransaction();
        orderBookDao.create(entity);
        orderBookDao.closeCurrentSessionWithTransaction();
    }

    public void update(final OrderBookEntity entity) {
        orderBookDao.openCurrentSessionWithTransaction();
        orderBookDao.update(entity);
        orderBookDao.closeCurrentSessionWithTransaction();
    }

    public List<OrderBookEntity> read(final long userId) {
        final Session session = orderBookDao.openCurrentSessionWithTransaction();
        final CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        final CriteriaQuery<OrderBookEntity> criteriaQuery = criteriaBuilder.createQuery(OrderBookEntity.class);

        final Root<OrderBookEntity> orderBookEntityRoot = criteriaQuery.from(OrderBookEntity.class);

        criteriaQuery.select(orderBookEntityRoot);
        criteriaQuery.where(criteriaBuilder.equal(orderBookEntityRoot.get("userEntity"), userId));

        final List<OrderBookEntity> orderBookEntities = session.createQuery(criteriaQuery).getResultList();

        orderBookDao.closeCurrentSessionWithTransaction();

        return orderBookEntities;
    }

    public List<OrderBookEntity> readAll() {
        orderBookDao.openCurrentSessionWithTransaction();
        final List<OrderBookEntity> departmentEntities = orderBookDao.readAll();
        orderBookDao.closeCurrentSessionWithTransaction();

        return departmentEntities;
    }

}