package com.spdu.dal.repositories;

import com.spdu.domain_models.entities.FileEntity;
import com.spdu.web.Application;
import org.flywaydb.core.Flyway;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.sql.SQLException;
import java.time.LocalDateTime;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
@ContextConfiguration
public class FileEntityRepositoryImplTest {

    @Autowired
    private FileEntityRepositoryImpl fileEntityRepository;

    @Autowired
    Flyway flyway;

    @Before
    public void init() {
        flyway.clean();
        flyway.migrate();
    }

    @Test
    public void createTest() throws SQLException {
        createEntities();
        FileEntity fileEntity = new FileEntity();
        fileEntity.setId(3);
        fileEntity.setName("Test3Name");
        fileEntity.setContentType("Content type3");
        fileEntity.setOwnerId(1);
        fileEntity.setCreatedAt(LocalDateTime.now());
        fileEntity.setPath("testPath3");
        assertEquals(3, fileEntityRepository.create(fileEntity));
    }

    @Test
    public void getByIdTest() throws SQLException {
        createEntities();
        assertEquals(1,fileEntityRepository.getById(1).get().getId());
        assertEquals("TestName",fileEntityRepository.getById(1).get().getName());
    }

    private void createEntities() throws SQLException {
        FileEntity fileEntity = new FileEntity();
        fileEntity.setId(1);
        fileEntity.setName("TestName");
        fileEntity.setContentType("Content type");
        fileEntity.setOwnerId(1);
        fileEntity.setCreatedAt(LocalDateTime.now());
        fileEntity.setPath("testPath");
        fileEntityRepository.create(fileEntity);
        FileEntity fileEntity2 = new FileEntity();
        fileEntity2.setId(2);
        fileEntity2.setName("Test2Name");
        fileEntity2.setContentType("Content type2");
        fileEntity2.setOwnerId(1);
        fileEntity2.setCreatedAt(LocalDateTime.now());
        fileEntity2.setPath("testPath2");
        fileEntityRepository.create(fileEntity2);
    }
}