package com.spdu.bll.services;

import org.springframework.security.core.userdetails.User.UserBuilder;
import com.spdu.bll.models.CustomUserDetails;
import com.spdu.dal.repository.UserRepository;
import com.spdu.bll.models.constants.UserRole;
import com.spdu.domain_models.entities.User;
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
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        Optional<User> userOptional = userRepository.getByUserName(userName);
        if (userOptional.isPresent()) {
            User user = userOptional.get();

            UserBuilder builder = null;

            builder = org.springframework.security.core.userdetails.User.withUsername(userName);
            builder.disabled(!user.isEnabled());
            builder.password(user.getPassword());
            String[] authorities = {"ROLE_USER"};

            builder.authorities(authorities);

            return builder.build();

//            UserRole role = userRepository.getUserRole(user.getId());
//            return new CustomUserDetails(user, role);
        } else {
            throw new UsernameNotFoundException(userName);
        }
    }
}
