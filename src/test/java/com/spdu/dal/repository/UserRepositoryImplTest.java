package com.spdu.dal.repository;

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

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
@ContextConfiguration
public class UserRepositoryImplTest {

    @Autowired
    private UserRepositoryImpl userRepository;

    @Autowired
    Flyway flyway;

//    @Before
//    public void init() {
//        flyway.clean();
//        flyway.migrate();
//    }

    @Test
    @AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
    public void testAddingUser() throws SQLException {
        User user = new User(3, "Joe", "Franklin", "testmail@gmail.com", "JoeFranklin", "password", LocalDateTime.of(1999, 12, 10, 1, 23, 22));
        assertEquals(2, userRepository.register(user));
    }
}