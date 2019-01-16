package com.spdu.dal.repository;

import com.spdu.model.entities.Chat;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository("chatRepository")
public class ChatRepositoryImpl extends BaseRepositoryImpl<Chat, Long> implements ChatRepository {
    @Autowired
    private SessionFactory sessionFactory;

    private Session getSession() {
        return sessionFactory.getCurrentSession();
    }
}
