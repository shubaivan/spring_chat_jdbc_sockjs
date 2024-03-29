package com.spdu.bll.security;

import com.spdu.bll.services.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {

    private final CustomUserDetailsService customUserDetailsService;
    private final DataSource dataSource;

    @Autowired
    public WebSecurityConfiguration(CustomUserDetailsService customUserDetailsService, DataSource dataSource) {
        this.customUserDetailsService = customUserDetailsService;
        this.dataSource = dataSource;
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    public void configure(WebSecurity web) {
        web.ignoring().antMatchers("/static/**");
        web.ignoring().antMatchers("/resources/**");
        web.ignoring().antMatchers("/css/**");
        web.ignoring().antMatchers("/swagger-resources/**");
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider
                = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(customUserDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Override
    protected void configure(final AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(customUserDetailsService)
                .passwordEncoder(passwordEncoder())
                .and()
                .authenticationProvider(authenticationProvider())
                .jdbcAuthentication()
                .dataSource(dataSource)
                .usersByUsernameQuery("select user_name, password, is_enabled from db_users where email=? or where user_name=?")
                .authoritiesByUsernameQuery("select email, name from roles " +
                        "join user_roles on " +
                        "user_roles.id = roles.id " +
                        "join db_users on " +
                        "db_users.id = user_roles.user_id " +
                        "where db_users.email=? or where db_users.user_name=?");

    }

    @Override
    protected void configure(final HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .authorizeRequests()
                    .antMatchers("/login**").permitAll()
                    .antMatchers("/register*").anonymous()
                    .antMatchers("/confirm-account*").anonymous()
                    .antMatchers("/reset-password*").anonymous()
                    .antMatchers("/newPassword").anonymous()
                    .antMatchers("/check-token").anonymous()
                    .antMatchers("/new-password").anonymous()

                    .antMatchers("/chats").access("hasRole('USER')")
                .anyRequest().hasAnyRole("ADMIN", "USER")

                .and()
                .formLogin().loginPage("/login").loginProcessingUrl("/loginAction").permitAll()

                .and()
                .logout().logoutSuccessUrl("/login").permitAll();
    }
}
