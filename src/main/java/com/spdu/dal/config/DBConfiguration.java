package com.spdu.dal.config;

import com.spdu.model.entities.*;
import com.spdu.model.entities.relations.ChatsUsers;
import com.spdu.model.entities.relations.FilesChats;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.io.File;
import java.util.Properties;

@Configuration
@EnableTransactionManagement
public class DBConfiguration {
    @Value("${spring.datasource.driver-class-name}")
    private String DB_DRIVER;

    @Value("${spring.datasource.password}")
    private String DB_PASSWORD;

    @Value("${spring.datasource.url}")
    private String DB_URL;

    @Value("${spring.datasource.username}")
    private String DB_USERNAME;

    @Value("${spring.jpa.properties.hibernate.dialect}")
    private String HIBERNATE_DIALECT;

    @Value("${spring.jpa.show-sql}")
    private String HIBERNATE_SHOW_SQL;

    @Value("${spring.jpa.hibernate.ddl-auto}")
    private String HIBERNATE_HBMDDL_AUTO;

    @Bean
    public DataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(DB_DRIVER);
        dataSource.setUrl(DB_URL);
        dataSource.setUsername(DB_USERNAME);
        dataSource.setPassword(DB_PASSWORD);
        return dataSource;
    }

    @Bean
    public LocalSessionFactoryBean sessionFactory() {
        LocalSessionFactoryBean sessionFactoryBean = new LocalSessionFactoryBean();
        sessionFactoryBean.setDataSource(dataSource());
        Properties hibernateProperties = new Properties();

        hibernateProperties.put("hibernate.dialect", HIBERNATE_DIALECT);
        hibernateProperties.put("hibernate.show_sql", HIBERNATE_SHOW_SQL);
        hibernateProperties.put("hibernate.hbm2ddl.auto", HIBERNATE_HBMDDL_AUTO);

        sessionFactoryBean.setHibernateProperties(hibernateProperties);
        sessionFactoryBean.setAnnotatedClasses(Chat.class, User.class,
                Message.class, FileEntity.class, FavoriteMessage.class,
                FilesChats.class, ChatsUsers.class);
        return sessionFactoryBean;
    }

    @Bean
    public HibernateTransactionManager transactionManager() {
        HibernateTransactionManager transactionManager =
                new HibernateTransactionManager();
        transactionManager.setSessionFactory(sessionFactory().getObject());
        return transactionManager;
    }
}
