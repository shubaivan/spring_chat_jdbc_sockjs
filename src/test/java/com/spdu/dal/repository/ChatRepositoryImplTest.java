package com.spdu.dal.repository;

import com.spdu.bll.models.constants.ChatType;
import com.spdu.domain_models.entities.Chat;
import com.spdu.domain_models.entities.Message;
import com.spdu.web.Application;
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

//    @Autowired
//    Flyway flyway;
//
//    @Before
//    public void init() {
//        flyway.clean();
//        flyway.migrate();
//    }

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
        assertEquals(4,chatRepository.create(chat));
    }

    @Test
    @AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
    public void testGetById() throws SQLException {
        Chat testChat = chatRepository.getById(1).get();
        assertEquals(1,testChat.getId());
        assertEquals(ChatType.DEFAULT,testChat.getChatType());
        assertEquals("Default public chat",testChat.getDescription());
        assertEquals("Default",testChat.getName());
        assertEquals("public, default",testChat.getTags());
        assertEquals(1,testChat.getOwnerId());
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
        assertEquals(2,chatRepository.joinToChat(1,2));
    }

    @Test
    @AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
    public void testGetAllOwn() throws SQLException {
        Chat chat = new Chat();
        chat.setId(3);
        chat.setChatType(ChatType.PUBLIC);
        chat.setName("Chat3 TEST name");
        chat.setDescription("Chat3 TEST description");
        chat.setOwnerId(1);
        chat.setTags("Test4 Tags");
        chatRepository.create(chat);
        assertEquals(3,chatRepository.getAllOwn(1).size());
        Chat testChat = chatRepository.getAllOwn(1).stream().filter(x -> 1 == x.getId()).findFirst().get();
        assertEquals(1,testChat.getId());
        assertEquals(ChatType.DEFAULT,testChat.getChatType());
        assertEquals("Default public chat",testChat.getDescription());
        assertEquals("Default",testChat.getName());
        assertEquals("public, default",testChat.getTags());
        assertEquals(1,testChat.getOwnerId());
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
        chat.setId(4);
        chat.setChatType(ChatType.PUBLIC);
        chat.setName("Chat4 TEST name");
        chat.setDescription("Chat4 TEST description");
        chat.setOwnerId(1);
        chat.setTags("Test4 Tags");
        chatRepository.create(chat);
        assertEquals(2,chatRepository.getAll(1).size());
        Chat testChat = chatRepository.getAllOwn(1).stream().filter(x -> 1 == x.getId()).findFirst().get();
        assertEquals(1,testChat.getId());
        assertEquals(ChatType.DEFAULT,testChat.getChatType());
        assertEquals("Default public chat",testChat.getDescription());
        assertEquals("Default",testChat.getName());
        assertEquals("public, default",testChat.getTags());
        assertEquals(1,testChat.getOwnerId());
    }

//    @Test
//    @AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
//    public void testGetPublic() throws SQLException {
//        Chat chat = new Chat();
//        chat.setId(5);
//        chat.setChatType(ChatType.PUBLIC);
//        chat.setName("Chat4 TEST name");
//        chat.setDescription("Chat4 TEST description");
//        chat.setOwnerId(1);
//        chat.setTags("Test4 Tags");
//        chatRepository.joinToChat(1,5);
//        assertEquals(1,chatRepository.getPublic(1).size());
//        Chat testChat = chatRepository.getPublic(1).stream().filter(x -> 1 == x.getId()).findFirst().get();
//        assertEquals(1,testChat.getId());
//        assertEquals(ChatType.DEFAULT,testChat.getChatType());
//        assertEquals("Default public chat",testChat.getDescription());
//        assertEquals("Default",testChat.getName());
//        assertEquals("public, default",testChat.getTags());
//        assertEquals(1,testChat.getOwnerId());
//    }

}
