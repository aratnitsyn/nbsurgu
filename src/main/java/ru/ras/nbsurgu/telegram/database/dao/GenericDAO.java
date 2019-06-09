package ru.ras.nbsurgu.telegram.database.dao;

import java.io.Serializable;
import java.util.List;

public interface GenericDAO <T, Id extends Serializable> {

    void persist(T entity);
    void create(T entity);
    void update(T entity);
    void delete(T entity);
    T load(Id id);
    T read(Id id);
    List<T> readAll();
    Object readOnRequest(String request);

}