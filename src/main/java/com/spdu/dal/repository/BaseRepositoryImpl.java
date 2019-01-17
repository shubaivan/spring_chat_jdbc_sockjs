package com.spdu.dal.repository;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.io.Serializable;
import java.util.Optional;

@Repository
@Transactional
public class BaseRepositoryImpl<T, K extends Serializable> implements BaseRepository<T, K> {
    private final SessionFactory sessionFactory;

    @Autowired
    public BaseRepositoryImpl(SessionFactory sessionFactory){
        this.sessionFactory = sessionFactory;
    }

    private Session getSession() {
        return sessionFactory.getCurrentSession();
    }

    @Override
    public K create(T entity) {
        return (K)getSession().save(entity);
    }

    @Override
    public void update(T entity) {
        getSession().saveOrUpdate(entity);
    }

    @Override
    public void delete(T entity) {
        getSession().delete(entity);
    }

    @Override
    public Optional<T> findById(Class<T> entityType, K identifier) {
        return Optional.of(getSession().get(entityType, identifier));
    }
}
