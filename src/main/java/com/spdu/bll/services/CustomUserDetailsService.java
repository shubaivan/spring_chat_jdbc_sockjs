package com.spdu.bll.services;

import com.spdu.bll.returned_model.CustomUserDetails;
import com.spdu.dal.repository.UserRepository;
import com.spdu.model.constants.UserRole;
import com.spdu.model.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

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
            UserRole role = userRepository.getUserRole(user.getId());
            return new CustomUserDetails(user, role);
        } else {
            throw new UsernameNotFoundException(email);
        }
    }
}