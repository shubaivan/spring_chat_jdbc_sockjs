package com.spdu.dal.repository;

import com.spdu.model.entities.Chat;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository("chatRepository")
public class ChatRepositoryImpl extends BaseRepositoryImpl<Chat, Long> implements ChatRepository {
    private final SessionFactory sessionFactory;

    @Autowired
    public ChatRepositoryImpl(SessionFactory sessionFactory) {
        super(sessionFactory);
        this.sessionFactory = sessionFactory;
    }

    private Session getSession() {
        return sessionFactory.getCurrentSession();
    }
}
