package com.spdu.bll.services;

import com.spdu.dal.repositories.FileEntityRepositoryImpl;
import com.spdu.domain_models.entities.FileEntity;
import com.spdu.web.Application;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
@ContextConfiguration
public class FileEntityServiceImplTest {
    @Autowired
    private FileEntityRepositoryImpl fileEntityRepository;

    @Test
    public void save() throws SQLException {
        FileEntity fileEntity = new FileEntity();
        fileEntity.setPath("Path");
        fileEntity.setCreatedAt(LocalDateTime.now());
        fileEntity.setOwnerId(1);
        fileEntity.setContentType("Content");
        fileEntity.setName("Name");
        fileEntity.setId(1);
        assertEquals(1, fileEntityRepository.create(fileEntity));
        long newFileId = fileEntityRepository.create(fileEntity);
        Optional<FileEntity> newFile = fileEntityRepository.getById(newFileId);
        assertTrue(newFile.isPresent());
    }
}