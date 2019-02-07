package com.spdu.bll.services;

import com.spdu.bll.interfaces.FileEntityService;
import com.spdu.bll.models.FileEntityDto;
import com.spdu.dal.repositories.FileEntityRepository;
import com.spdu.domain_models.entities.FileEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;

@Service
public class FileEntityServiceImpl implements FileEntityService {
    private final FileEntityRepository fileEntityRepository;

    @Autowired
    public FileEntityServiceImpl(FileEntityRepository fileEntityRepository) {
        this.fileEntityRepository = fileEntityRepository;
    }

    @Override
    public void save(FileEntityDto fileEntityDto) throws SQLException {
        FileEntity fileEntity = new FileEntity();

        fileEntity.setOwnerId(fileEntityDto.getOwnerId());
        fileEntity.setCreatedAt(LocalDateTime.now());
        fileEntity.setContentType(fileEntityDto.getContentType());
        fileEntity.setName(fileEntityDto.getName());
        fileEntity.setPath(fileEntityDto.getPath());

        fileEntityRepository.create(fileEntity);
    }

    @Override
    public File store(String fileName, String path) throws IOException {
        File directory = new File(path);
        if (!directory.exists()) {
            directory.mkdir();
        }

        File file = new File(path);
        if (!file.exists()) {
            file.createNewFile();
        }
        return file;
    }
}
