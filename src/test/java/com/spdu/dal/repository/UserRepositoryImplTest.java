package com.spdu.dal.repository;

import com.spdu.bll.custom_exceptions.UserException;
import com.spdu.bll.models.constants.UserRole;
import com.spdu.domain_models.entities.User;
import com.spdu.domain_models.entities.relations.UserRoles;
import com.spdu.web.Application;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
@ContextConfiguration
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

    @Test
    @AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
    public void testGetById() throws SQLException {
        User testUser = userRepository.getById(1).get();
        assertEquals(1, testUser.getId());
        assertEquals("Inna", testUser.getFirstName());
        assertEquals("Bakum", testUser.getLastName());
        assertEquals("ibakum95@gmail.com", testUser.getEmail());
        assertEquals("ibakum", testUser.getUserName());
        assertEquals(LocalDate.of(2019, Month.JANUARY, 25), testUser.getDateOfBirth());
    }

    @Test
    @AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
    public void testGetAll() throws SQLException {
        assertEquals(2, userRepository.getAll().size());
        User testUser = userRepository.getAll().stream().filter(x -> 1 == x.getId()).findFirst().get();
        assertEquals(1, testUser.getId());
        assertEquals("Inna", testUser.getFirstName());
        assertEquals("Bakum", testUser.getLastName());
        assertEquals("ibakum95@gmail.com", testUser.getEmail());
        assertEquals("ibakum", testUser.getUserName());
        assertEquals(LocalDate.of(2019, Month.JANUARY, 25), testUser.getDateOfBirth());
    }

    @Test
    @AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
    public void testGetByEmail() throws SQLException {
        User testUser = userRepository.getByEmail("ibakum95@gmail.com").get();
        assertEquals(1, testUser.getId());
        assertEquals("Inna", testUser.getFirstName());
        assertEquals("Bakum", testUser.getLastName());
        assertEquals("ibakum95@gmail.com", testUser.getEmail());
        assertEquals("ibakum", testUser.getUserName());
        assertEquals(LocalDate.of(2019, Month.JANUARY, 25), testUser.getDateOfBirth());
    }

    @Test
    @AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
    public void testGetByUserName() throws SQLException {
        User testUser = userRepository.getByUserName("ibakum").get();
        assertEquals(1, testUser.getId());
        assertEquals("Inna", testUser.getFirstName());
        assertEquals("Bakum", testUser.getLastName());
        assertEquals("ibakum95@gmail.com", testUser.getEmail());
        assertEquals("ibakum", testUser.getUserName());
        assertEquals(LocalDate.of(2019, Month.JANUARY, 25), testUser.getDateOfBirth());
    }

    @Test
    @AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
    public void testGetUserRole() throws SQLException {
        assertEquals(UserRole.ROLE_USER, userRepository.getUserRole(1));
    }

    @Test
    @AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
    public void testUpdate() throws SQLException, UserException {
        User testUser = new User(1, "notInna", "notBakum", "ibakum95@gmail.com", "notibakum", "notpassword", LocalDateTime.of(1999, 12, 10, 1, 23, 22));
        userRepository.update(1, testUser);
        User testUser2 = userRepository.getById(1).get();
        assertEquals(1, testUser2.getId());
        assertEquals("notInna", testUser2.getFirstName());
        assertEquals("notBakum", testUser2.getLastName());
        assertEquals("notibakum", testUser2.getUserName());
    }
}
