package com.spdu.dal.repositories;

import com.spdu.domain_models.entities.FileEntity;
import org.springframework.dao.EmptyResultDataAccessException;

import java.sql.SQLException;
import java.util.Optional;

public interface FileEntityRepository {

    long create(FileEntity fileEntity) throws SQLException;

    Optional<FileEntity> getById(long id) throws EmptyResultDataAccessException;

    FileEntity getByUserId(long id) throws EmptyResultDataAccessException;
}
