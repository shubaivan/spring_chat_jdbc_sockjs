package com.spdu.dal.repositories;

import com.spdu.bll.models.MessageType;
import com.spdu.domain_models.entities.Message;
import com.spdu.web.Application;
import org.flywaydb.core.Flyway;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.sql.SQLException;
import java.time.LocalDateTime;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
@ContextConfiguration
public class MessageRepositoryImplTest {
    @Autowired
    private MessageRepositoryImpl messageRepository;

    @Autowired
    Flyway flyway;

    @Before
    public void init() {
        flyway.clean();
        flyway.migrate();
    }

    @Test
    @AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
    public void testCreate() throws SQLException {
        createMessages();
        Message message = new Message();
        message.setId(3);
        message.setAuthorID(1);
        message.setChatId(1);
        message.setMessageType(MessageType.CHAT);
        message.setCreatedAt(LocalDateTime.of(2019, 12, 10, 1, 23, 22));
        assertEquals(3, messageRepository.create(message));
    }

    @Test
    @AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
    public void testGetById() throws SQLException {
        createMessages();
        Message testMessage = messageRepository.getById(1).get();
        assertEquals(1, testMessage.getId());
        assertEquals(1, testMessage.getAuthorID());
        assertEquals(1, testMessage.getChatId());
        assertEquals("Text from message", testMessage.getText());
    }

    @Test
    @AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
    public void testGetByChatId() throws SQLException {
        createMessages();
        Message testMessage = messageRepository.getMessages(1, null).stream().filter(x -> 1 == x.getId()).findFirst().get();
        assertEquals(1, testMessage.getId());
        assertEquals(1, testMessage.getAuthorID());
        assertEquals(1, testMessage.getChatId());
        assertEquals("Text from message", testMessage.getText());
        assertEquals(2, messageRepository.getMessages(1, null).size());
    }

    private void createMessages() throws SQLException {
        Message message = new Message();
        message.setId(1);
        message.setAuthorID(1);
        message.setChatId(1);
        message.setText("Text from message");
        message.setMessageType(MessageType.CHAT);
        message.setCreatedAt(LocalDateTime.of(2019, 12, 10, 1, 23, 22));
        messageRepository.create(message);
        Message message2 = new Message();
        message2.setId(2);
        message2.setAuthorID(1);
        message2.setChatId(1);
        message2.setText("Text message");
        message2.setMessageType(MessageType.CHAT);
        message2.setCreatedAt(LocalDateTime.of(2019, 12, 10, 1, 23, 22));
        messageRepository.create(message2);

    }
}
