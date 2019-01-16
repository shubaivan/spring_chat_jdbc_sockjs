package com.spdu.dal.repository;

import java.io.Serializable;
import java.util.Optional;

public interface BaseRepository<T, K extends Serializable> {

    K create(T entity);

    void update(T entity);

    void delete(T entity);

    Optional<T> findById(Class<T> entityType, K identifier);
}
