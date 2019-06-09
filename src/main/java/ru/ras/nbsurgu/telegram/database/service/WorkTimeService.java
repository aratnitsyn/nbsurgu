package ru.ras.nbsurgu.telegram.database.service;

import ru.ras.nbsurgu.telegram.database.dao.GenericDAOImpl;
import ru.ras.nbsurgu.telegram.database.entity.WorkTimeEntity;

import java.util.List;

public class WorkTimeService {

    private static volatile WorkTimeService instance;

    private static volatile GenericDAOImpl<WorkTimeEntity, Long> worksTimeDao = new GenericDAOImpl<>(WorkTimeEntity.class);

    public static WorkTimeService getInstance() {
        if (instance == null) {
            synchronized (WorkTimeService.class) {
                if (instance == null) {
                    instance = new WorkTimeService();
                }
            }
        }

        return instance;
    }

    public void create(WorkTimeEntity entity) {
        worksTimeDao.openCurrentSessionWithTransaction();
        worksTimeDao.create(entity);
        worksTimeDao.closeCurrentSessionWithTransaction();
    }

    public void create(List<WorkTimeEntity> entities) {
        worksTimeDao.openCurrentSessionWithTransaction();

        for (WorkTimeEntity entity : entities) {
            worksTimeDao.create(entity);
        }

        worksTimeDao.closeCurrentSessionWithTransaction();
    }

    public WorkTimeEntity read(long id) {
        worksTimeDao.openCurrentSessionWithTransaction();
        WorkTimeEntity workTimeEntity = worksTimeDao.read(id);
        worksTimeDao.closeCurrentSessionWithTransaction();

        return workTimeEntity;
    }

    public List<WorkTimeEntity> readAll() {
        worksTimeDao.openCurrentSessionWithTransaction();
        List<WorkTimeEntity> workTimeEntities = worksTimeDao.readAll();
        worksTimeDao.closeCurrentSessionWithTransaction();

        return workTimeEntities;
    }

    @SuppressWarnings("unchecked")
    public List<WorkTimeEntity> readOnRequest(String request) {
        worksTimeDao.openCurrentSessionWithTransaction();
        List<WorkTimeEntity> workTimeEntities = (List<WorkTimeEntity>) worksTimeDao.readOnRequest(request);
        worksTimeDao.closeCurrentSessionWithTransaction();

        return workTimeEntities;
    }

}