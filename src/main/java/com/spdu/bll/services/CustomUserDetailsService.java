package com.spdu.bll.services;

import com.spdu.bll.models.CustomUserDetails;
import com.spdu.dal.repositories.UserRepository;
import com.spdu.bll.models.constants.UserRole;
import com.spdu.domain_models.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.Optional;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    @Autowired
    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<User> userOptional = userRepository.getByEmail(email);
        if (userOptional.isPresent()) {
            User user = userOptional.get();

            try {
                UserRole role = userRepository.getUserRole(user.getId());
                return new CustomUserDetails(user, role);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

        } else {
            throw new UsernameNotFoundException(email);
        }
    }
}
