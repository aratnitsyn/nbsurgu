package ru.ras.nbsurgu.telegram.database.service;

import ru.ras.nbsurgu.telegram.database.dao.GenericDAOImpl;
import ru.ras.nbsurgu.telegram.database.entity.BookEntity;

import java.util.List;

public class BookService {

    private static volatile BookService instance;

    private static volatile GenericDAOImpl<BookEntity, Long> booksDao = new GenericDAOImpl<>(BookEntity.class);

    private BookService() {}

    public static BookService getInstance() {
        if (instance == null) {
            synchronized (BookService.class) {
                if (instance == null) {
                    instance = new BookService();
                }
            }
        }

        return instance;
    }

    public void create(BookEntity entity) {
        booksDao.openCurrentSessionWithTransaction();
        booksDao.create(entity);
        booksDao.closeCurrentSessionWithTransaction();
    }

    public void create(Iterable<BookEntity> entities) {
        booksDao.openCurrentSessionWithTransaction();

        for (BookEntity entity : entities) {
            booksDao.create(entity);
        }

        booksDao.closeCurrentSessionWithTransaction();
    }

    public BookEntity read(long userId) {
        booksDao.openCurrentSessionWithTransaction();
        BookEntity bookEntity = booksDao.read(userId);
        booksDao.closeCurrentSessionWithTransaction();

        return bookEntity;
    }

    public List<BookEntity> readAll() {
        booksDao.openCurrentSessionWithTransaction();
        List<BookEntity> bookEntities = booksDao.readAll();
        booksDao.closeCurrentSessionWithTransaction();

        return bookEntities;
    }

}