package com.spdu.bll.interfaces;

import com.spdu.bll.custom_exceptions.CustomFileException;
import com.spdu.bll.models.FileEntityDto;
import org.springframework.dao.EmptyResultDataAccessException;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Optional;

public interface FileEntityService {

    FileEntityDto save(FileEntityDto fileEntityDto) throws SQLException, CustomFileException;

    File store(String fileName, String path) throws IOException;

    FileEntityDto getById(long id) throws EmptyResultDataAccessException, CustomFileException;
}
