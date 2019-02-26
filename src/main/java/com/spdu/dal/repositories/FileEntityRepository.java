package com.spdu.dal.repositories;

import com.spdu.domain_models.entities.FileEntity;
import org.springframework.dao.EmptyResultDataAccessException;

import java.sql.SQLException;
import java.util.Optional;

public interface FileEntityRepository {

    long create(FileEntity fileEntity);

    Optional<FileEntity> getById(long id);

    FileEntity getByUserId(long id);
}
