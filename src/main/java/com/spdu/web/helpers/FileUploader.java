package com.spdu.web.helpers;

import com.spdu.bll.custom_exceptions.CustomFileException;
import com.spdu.bll.interfaces.FileEntityService;
import com.spdu.bll.models.FileEntityDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Properties;

@Component
public class FileUploader {
    private final FileEntityService fileEntityService;

    @Autowired
    public FileUploader(FileEntityService fileEntityService) {
        this.fileEntityService = fileEntityService;
    }

    public FileEntityDto uploadFile(MultipartFile file, String detailedPath, long userId) throws IOException, CustomFileException, SQLException {
        Properties properties = new Properties();
        properties.load(Thread.currentThread().getContextClassLoader().getResourceAsStream("application.properties"));
        String sServerLocation = properties.getProperty("server.upload.docs.path");

        file.transferTo(fileEntityService.store(file.getOriginalFilename(), sServerLocation + detailedPath));

        FileEntityDto fileEntityDto = new FileEntityDto();

        fileEntityDto.setContentType(file.getContentType());
        fileEntityDto.setName(file.getOriginalFilename());
        fileEntityDto.setPath(detailedPath + file.getOriginalFilename());
        fileEntityDto.setOwnerId(userId);

        return fileEntityService.save(fileEntityDto);
    }
}
