package ru.ras.nbsurgu.telegram.database.service;

import ru.ras.nbsurgu.telegram.database.dao.GenericDAOImpl;
import ru.ras.nbsurgu.telegram.database.entity.DepartmentEntity;
import ru.ras.nbsurgu.telegram.database.entity.EmployeeEntity;
import ru.ras.nbsurgu.telegram.database.entity.WorkTimeEntity;

import java.util.List;
import java.util.Set;

public class DepartmentService {

    private static volatile DepartmentService instance;

    private static volatile GenericDAOImpl<DepartmentEntity, Long> departmentDao = new GenericDAOImpl<>(DepartmentEntity.class);

    public static DepartmentService getInstance() {
        if (instance == null) {
            synchronized (DepartmentService.class) {
                if (instance == null) {
                    instance = new DepartmentService();
                }
            }
        }

        return instance;
    }

    public void persist(DepartmentEntity entity) {
        departmentDao.openCurrentSessionWithTransaction();
        departmentDao.persist(entity);
        departmentDao.closeCurrentSessionWithTransaction();
    }

    public void create(DepartmentEntity entity) {
        departmentDao.openCurrentSessionWithTransaction();
        departmentDao.create(entity);
        departmentDao.closeCurrentSessionWithTransaction();
    }

    public void create(List<DepartmentEntity> entities) {
        departmentDao.openCurrentSessionWithTransaction();
        entities.forEach(entity -> departmentDao.create(entity));
        departmentDao.closeCurrentSessionWithTransaction();
    }

    public void update(DepartmentEntity entity) {
        departmentDao.openCurrentSessionWithTransaction();
        departmentDao.update(entity);
        departmentDao.closeCurrentSessionWithTransaction();
    }

    public DepartmentEntity read(long departmentId) {
        departmentDao.openCurrentSessionWithTransaction();
        final DepartmentEntity departmentEntity = departmentDao.read(departmentId);
        departmentDao.closeCurrentSessionWithTransaction();

        return departmentEntity;
    }

    public List<DepartmentEntity> readAll() {
        departmentDao.openCurrentSessionWithTransaction();
        final List<DepartmentEntity> departmentEntities = departmentDao.readAll();
        departmentDao.closeCurrentSessionWithTransaction();

        return departmentEntities;
    }

    public Set<WorkTimeEntity> loadWorkTime(long departmentId) {
        departmentDao.openCurrentSession();
        final DepartmentEntity departmentEntity = departmentDao.load(departmentId);

        departmentDao.getCurrentSession().beginTransaction();
        Set<WorkTimeEntity> workTimeEntities = departmentEntity.getWorkTimes();
        departmentDao.getCurrentSession().getTransaction().commit();

        return workTimeEntities;
    }

    public Set<EmployeeEntity> loadEmployee(long departmentId) {
        departmentDao.openCurrentSession();
        final DepartmentEntity departmentEntity = departmentDao.load(departmentId);

        departmentDao.getCurrentSession().beginTransaction();
        Set<EmployeeEntity> employeeEntities = departmentEntity.getEmployees();
        departmentDao.getCurrentSession().getTransaction().commit();

        return employeeEntities;
    }

}