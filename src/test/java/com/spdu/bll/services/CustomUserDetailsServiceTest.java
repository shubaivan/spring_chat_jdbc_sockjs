package com.spdu.bll.services;

import com.spdu.bll.models.CustomUserDetails;
import com.spdu.dal.repositories.UserRepositoryImpl;
import com.spdu.domain_models.entities.User;
import com.spdu.web.Application;
import org.flywaydb.core.Flyway;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
@ContextConfiguration
public class CustomUserDetailsServiceTest {

    @Autowired
    private UserRepositoryImpl userRepositoryImpl;
    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Autowired
    Flyway flyway;

    @Before
    public void init() {
        flyway.clean();
        flyway.migrate();
    }

    @Test
    @AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
    public void testLoadUserByUsername() throws UsernameNotFoundException {
        User user = userRepositoryImpl.getByEmail("ibakum95@gmail.com").get();
        assertEquals(customUserDetailsService.loadUserByUsername("ibakum95@gmail.com"),
                new CustomUserDetails(user, userRepositoryImpl.getUserRole(user.getId())));
    }
}
