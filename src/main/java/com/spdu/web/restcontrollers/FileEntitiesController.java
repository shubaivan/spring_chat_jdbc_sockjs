package com.spdu.web.restcontrollers;

import com.spdu.bll.custom_exceptions.CustomFileException;
import com.spdu.bll.interfaces.FileEntityService;
import com.spdu.bll.interfaces.UserService;
import com.spdu.bll.models.FileEntityDto;
import com.spdu.bll.models.constants.PathToFiles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.io.*;
import java.net.URL;
import java.util.Properties;

@RestController
@RequestMapping("api/file_entities")
public class FileEntitiesController {
    private final FileEntityService fileEntityService;
    private final UserService userService;

    @Autowired
    public FileEntitiesController(FileEntityService fileService, UserService userService) {
        this.fileEntityService = fileService;
        this.userService = userService;
    }

    @GetMapping("/{id}")
    public ResponseEntity getFile(@PathVariable long id) {
        try {
            FileEntityDto fileEntityDto = fileEntityService.getById(id);

            String pathToFile = PathToFiles.get();

            URL path = new URL("file:///" + pathToFile + fileEntityDto.getPath().trim());
            Resource resource = new UrlResource(path);

            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(fileEntityDto.getContentType()))
                    .header(HttpHeaders.CONTENT_DISPOSITION,
                            "attachment; filename=/" + fileEntityDto.getName() + "/")
                    .body(resource);
        } catch (IOException | CustomFileException e) {
            throw new RuntimeException(e);
        }
    }
}
