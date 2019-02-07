package com.spdu.dal.repository;

import com.spdu.bll.models.constants.UserRole;
import com.spdu.domain_models.entities.User;
import com.spdu.domain_models.entities.relations.UserRoles;
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
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
import java.time.LocalDateTime;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
@ContextConfiguration
@Transactional
public class UserRepositoryImplTest {
private long id;
    @Autowired
    private UserRepositoryImpl userRepository;

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
    public void testAddingUser() throws SQLException {
        User user = new User(3, "Joe", "Franklin", "testmail@gmail.com", "JoeFranklin", "password", LocalDateTime.of(1999, 12, 10, 1, 23, 22));
        assertEquals(3, userRepository.register(user));
    }

    @Test
    @AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
    public void testSettingUserRole() throws SQLException {
        User user = new User(2, "John", "FranklinJr", "testmail2@gmail.com", "JoeFranklin2", "password", LocalDateTime.of(1999, 12, 10, 1, 23, 22));
        id = userRepository.register(user);
        UserRoles userRoles = new UserRoles();
        userRoles.setRoleId(2);
        userRoles.setUserId(2);
        userRepository.setUserRole(userRoles);
        assertEquals(2, id);
    }
}