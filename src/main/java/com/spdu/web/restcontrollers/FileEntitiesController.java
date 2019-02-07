package com.spdu.web.restcontrollers;

import com.spdu.bll.interfaces.FileEntityService;
import com.spdu.bll.interfaces.UserService;
import com.spdu.bll.models.FileEntityDto;
import com.spdu.domain_models.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;
import java.sql.SQLException;
import java.util.Optional;
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

    @PostMapping
    @PreAuthorize("hasAuthority(T(com.spdu.bll.models.constants.UserRole).ROLE_USER)")
    public ResponseEntity uploadAvatar(Principal principal, MultipartFile multipartFile) {
        try {
            Optional<User> user = userService.getByEmail(principal.getName());
            if (user.isPresent()) {
                Properties properties = new Properties();
                properties.load(Thread.currentThread().getContextClassLoader().getResourceAsStream("application.properties"));

                String sServerLocation = properties.getProperty("server.upload.docs.path");
                String path = "\\avatar\\" + principal.getName() + "\\";

                multipartFile.transferTo(fileEntityService.store(multipartFile.getOriginalFilename(), sServerLocation + path));

                FileEntityDto fileEntityDto = new FileEntityDto();

                fileEntityDto.setContentType(multipartFile.getContentType());
                fileEntityDto.setName(multipartFile.getOriginalFilename());
                fileEntityDto.setPath(path + multipartFile.getOriginalFilename());
                fileEntityDto.setOwnerId(user.get().getId());
                fileEntityService.save(fileEntityDto);
            }
        } catch (IOException | SQLException e) {
            throw new RuntimeException(e);
        }
        return new ResponseEntity(HttpStatus.OK);
    }
}
