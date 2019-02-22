package com.spdu.web.restcontrollers;

import com.spdu.bll.interfaces.FileEntityService;
import com.spdu.bll.interfaces.MessageService;
import com.spdu.bll.models.FileEntityDto;
import com.spdu.bll.models.MessageDto;
import com.spdu.bll.models.MessageReturnDto;
import com.spdu.bll.models.MessagesRequestContentDto;
import com.spdu.domain_models.entities.FileEntity;
import com.spdu.domain_models.entities.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/messages")
public class MessageController {
    private final MessageService messageService;
    private final FileEntityService fileEntityService;

    @Autowired
    public MessageController(
            MessageService messageService,
            FileEntityService fileEntityService
    ) {
        this.messageService = messageService;
        this.fileEntityService = fileEntityService;
    }

    @PostMapping
    @PreAuthorize("hasAuthority(T(com.spdu.bll.models.constants.UserRole).ROLE_USER)")
    public ResponseEntity create(@RequestBody Message message) {
        Optional<Message> newMessage = messageService.create(message);
        if (newMessage.isPresent()) {
            return new ResponseEntity(newMessage.get(), HttpStatus.CREATED);
        } else {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("{id}")
    @PreAuthorize("hasAuthority(T(com.spdu.bll.models.constants.UserRole).ROLE_USER)")
    public ResponseEntity getById(@PathVariable long id) {
        Optional<Message> newMessage = messageService.getById(id);
        if (newMessage.isPresent()) {
            return new ResponseEntity(newMessage.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/chat")
    @PreAuthorize("hasAuthority(T(com.spdu.bll.models.constants.UserRole).ROLE_USER)")
    public ResponseEntity getByChat(
            @RequestBody MessagesRequestContentDto requestContentDTO
    ) {
        List<Message> listMessages = messageService.getMessages(requestContentDTO);
        listMessages.forEach(this::accept);
        return new ResponseEntity(listMessages, HttpStatus.OK);
    }

    private void accept(Message s) {
        Optional<FileEntity> fileOptional = Optional.ofNullable(fileEntityService.getFileEntity(s.getAuthorID()));

        if (fileOptional.isPresent()) {
            FileEntityDto fileEntityDto = new FileEntityDto(fileOptional.get());
            s.setAvatarId(fileEntityDto.getId());
        }
    }

    @PostMapping("/default-chat")
    @PreAuthorize("hasAuthority(T(com.spdu.bll.models.constants.UserRole).ROLE_USER)")
    public ResponseEntity sendMessage(@RequestBody Message message, Principal principal) {
        Optional<MessageReturnDto> newMessage = messageService.send(principal.getName(), message);
        if (newMessage.isPresent()) {
            return new ResponseEntity(newMessage.get(), HttpStatus.CREATED);
        } else {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping
    public ResponseEntity update(@RequestBody MessageDto messageDto, long messageId) {
        try {
            MessageDto result = messageService.update(messageId, messageDto);
            return new ResponseEntity(result, HttpStatus.OK);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
