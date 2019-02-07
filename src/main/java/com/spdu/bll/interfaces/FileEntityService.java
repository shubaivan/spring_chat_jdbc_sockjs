package com.spdu.bll.interfaces;

import com.spdu.bll.models.FileEntityDto;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

public interface FileEntityService {

    void save(FileEntityDto fileEntityDto) throws SQLException;

    File store(String fileName, String path) throws IOException;
}
