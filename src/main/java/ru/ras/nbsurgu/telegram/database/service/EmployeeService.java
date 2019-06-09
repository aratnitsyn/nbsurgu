package ru.ras.nbsurgu.telegram.database.service;

import ru.ras.nbsurgu.telegram.database.dao.GenericDAOImpl;
import ru.ras.nbsurgu.telegram.database.entity.EmployeeEntity;

import java.util.List;

public class EmployeeService {

    private static volatile EmployeeService instance;

    private static volatile GenericDAOImpl<EmployeeEntity, Long> employeeDao = new GenericDAOImpl<>(EmployeeEntity.class);

    public static EmployeeService getInstance() {
        if (instance == null) {
            synchronized (EmployeeService.class) {
                if (instance == null) {
                    instance = new EmployeeService();
                }
            }
        }

        return instance;
    }

    public void persist(EmployeeEntity entity) {
        employeeDao.openCurrentSessionWithTransaction();
        employeeDao.persist(entity);
        employeeDao.closeCurrentSessionWithTransaction();
    }

    public void create(EmployeeEntity entity) {
        employeeDao.openCurrentSessionWithTransaction();
        employeeDao.create(entity);
        employeeDao.closeCurrentSessionWithTransaction();
    }

    public void create(List<EmployeeEntity> entities) {
        employeeDao.openCurrentSessionWithTransaction();

        for (EmployeeEntity entity : entities) {
            employeeDao.create(entity);
        }

        employeeDao.closeCurrentSessionWithTransaction();
    }

    public void update(EmployeeEntity entity) {
        employeeDao.openCurrentSessionWithTransaction();
        employeeDao.update(entity);
        employeeDao.closeCurrentSessionWithTransaction();
    }

    public EmployeeEntity read(long userId) {
        employeeDao.openCurrentSessionWithTransaction();
        EmployeeEntity article = employeeDao.read(userId);
        employeeDao.closeCurrentSessionWithTransaction();

        return article;
    }

    public List<EmployeeEntity> readAll() {
        employeeDao.openCurrentSessionWithTransaction();
        List<EmployeeEntity> articleList = employeeDao.readAll();
        employeeDao.closeCurrentSessionWithTransaction();

        return articleList;
    }

    public EmployeeEntity readTg(long tgId) {
        final String query = "from AuthorizationEntity where tg_id = " + tgId;

        employeeDao.openCurrentSessionWithTransaction();
        EmployeeEntity article = (EmployeeEntity) employeeDao.readOnRequest(query);
        employeeDao.closeCurrentSessionWithTransaction();

        return article;
    }

}