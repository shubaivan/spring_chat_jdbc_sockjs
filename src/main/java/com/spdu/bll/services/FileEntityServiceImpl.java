package com.spdu.bll.services;

import com.spdu.bll.custom_exceptions.CustomFileException;
import com.spdu.bll.interfaces.FileEntityService;
import com.spdu.bll.models.FileEntityDto;
import com.spdu.dal.repositories.FileEntityRepository;
import com.spdu.domain_models.entities.FileEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class FileEntityServiceImpl implements FileEntityService {
    private final FileEntityRepository fileEntityRepository;

    @Autowired
    public FileEntityServiceImpl(FileEntityRepository fileEntityRepository) {
        this.fileEntityRepository = fileEntityRepository;
    }

    @Override
    public FileEntityDto save(FileEntityDto fileEntityDto) throws SQLException, CustomFileException {
        FileEntity fileEntity = new FileEntity();

        fileEntity.setOwnerId(fileEntityDto.getOwnerId());
        fileEntity.setCreatedAt(LocalDateTime.now());
        fileEntity.setContentType(fileEntityDto.getContentType());
        fileEntity.setName(fileEntityDto.getName());
        fileEntity.setPath(fileEntityDto.getPath());

        long newFileId = fileEntityRepository.create(fileEntity);
        Optional<FileEntity> newFile = fileEntityRepository.getById(newFileId);

        if (newFile.isPresent()) {
            return new FileEntityDto(newFile.get());
        } else {
            throw new CustomFileException("Can't save file info!");
        }
    }

    @Override
    public File store(String fileName, String path) throws IOException {
        Path directory = Paths.get(path);
        if (!Files.exists(directory)) {
            Files.createDirectories(directory);
        }

        File file = new File(path + fileName);
        if (!file.exists()) {
            file.createNewFile();
        }
        return file;
    }

    @Override
    public FileEntityDto getById(long id) throws EmptyResultDataAccessException, CustomFileException {
        Optional<FileEntity> fileOptional = fileEntityRepository.getById(id);

        if (fileOptional.isPresent()) {
            return new FileEntityDto(fileOptional.get());
        } else {
            throw new CustomFileException("File not found");
        }
    }
}
