package com.spdu.dal.repository;

import com.spdu.bll.models.constants.ChatType;
import com.spdu.dal.repositories.ChatRepositoryImpl;
import com.spdu.dal.repositories.UserRepositoryImpl;
import com.spdu.domain_models.entities.Chat;
import com.spdu.domain_models.entities.User;
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
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
@ContextConfiguration
public class ChatRepositoryImplTest {


    @Autowired
    private ChatRepositoryImpl chatRepository;
    @Autowired
    private UserRepositoryImpl userRepositoryImpl;


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
        Chat chat = new Chat();
        chat.setId(2);
        chat.setChatType(ChatType.PUBLIC);
        chat.setName("Chat TEST name");
        chat.setDescription("Chat TEST description");
        chat.setOwnerId(1);
        chat.setTags("Test Tags");
        assertEquals(2, chatRepository.create(chat));
    }

    @Test
    @AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
    public void testGetById() throws SQLException {
        Chat testChat = chatRepository.getById(1).get();
        assertEquals(1, testChat.getId());
        assertEquals(ChatType.DEFAULT, testChat.getChatType());
        assertEquals("Default public chat", testChat.getDescription());
        assertEquals("Default", testChat.getName());
        assertEquals("public, default", testChat.getTags());
        assertEquals(1, testChat.getOwnerId());
    }

    @Test
    @AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
    public void testJoinToChat() throws SQLException {
        Chat chat = new Chat();
        chat.setId(2);
        chat.setChatType(ChatType.PUBLIC);
        chat.setName("Chat2 TEST name");
        chat.setDescription("Chat3 TEST description");
        chat.setOwnerId(1);
        chat.setTags("Test2 Tags");
        chatRepository.create(chat);
        assertEquals(2, chatRepository.joinToChat(1, 2));
    }

    @Test
    @AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
    public void testGetAllOwn() throws SQLException {
        Chat chat = new Chat();
        chat.setId(2);
        chat.setChatType(ChatType.PUBLIC);
        chat.setName("Chat TEST name");
        chat.setDescription("Chat3 TEST description");
        chat.setOwnerId(1);
        chat.setTags("Test Tags");
        chatRepository.create(chat);
        assertEquals(2, chatRepository.getAllOwn(1).size());
        Chat testChat = chatRepository.getAllOwn(1).stream().filter(x -> 1 == x.getId()).findFirst().get();
        assertEquals(1, testChat.getId());
        assertEquals(ChatType.DEFAULT, testChat.getChatType());
        assertEquals("Default public chat", testChat.getDescription());
        assertEquals("Default", testChat.getName());
        assertEquals("public, default", testChat.getTags());
        assertEquals(1, testChat.getOwnerId());
    }

    @Test
    @AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
    public void testUserIsPresentInChat() throws SQLException {
        assertTrue(chatRepository.userIsPresentInChat(1, 1));
    }

    @Test
    @AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
    public void testGetAll() throws SQLException {

        Chat chat = new Chat();
        chat.setId(2);
        chat.setChatType(ChatType.PUBLIC);
        chat.setName("Chat TEST name");
        chat.setDescription("Chat TEST description");
        chat.setOwnerId(1);
        chat.setTags("Test Tags");
        chatRepository.create(chat);
        assertEquals(2, chatRepository.getAll(1).size());
        Chat testChat = chatRepository.getAllOwn(1).stream().filter(x -> 1 == x.getId()).findFirst().get();
        assertEquals(1, testChat.getId());
        assertEquals(ChatType.DEFAULT, testChat.getChatType());
        assertEquals("Default public chat", testChat.getDescription());
        assertEquals("Default", testChat.getName());
        assertEquals("public, default", testChat.getTags());
        assertEquals(1, testChat.getOwnerId());
    }

    @Test
    @AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
    public void testGetPublic() throws SQLException {
        User user = new User(2, "Joe", "Franklin", "testmail@gmail.com", "JoeFranklin", "password", LocalDateTime.of(1999, 12, 10, 1, 23, 22));
        userRepositoryImpl.register(user);
        Chat chat = new Chat();
        chat.setId(2);
        chat.setName("Chat2 TEST name");
        chat.setDescription("Chat2 TEST description");
        chat.setOwnerId(1);
        chat.setChatType(ChatType.PUBLIC);
        chat.setTags("Test2 Tags");
        assertEquals(2, chatRepository.create(chat));
        chatRepository.joinToChat(2, 2);
        assertEquals(1, chatRepository.getPublic(1).size());
        Chat testChat = chatRepository.getPublic(1).stream().filter(x -> 1 == x.getOwnerId()).findFirst().get();
        assertEquals(2, testChat.getId());
        assertEquals(ChatType.PUBLIC, testChat.getChatType());
        assertEquals("Chat2 TEST description", testChat.getDescription());
        assertEquals("Chat2 TEST name", testChat.getName());
        assertEquals("Test2 Tags", testChat.getTags());
        assertEquals(1, testChat.getOwnerId());
    }

}
