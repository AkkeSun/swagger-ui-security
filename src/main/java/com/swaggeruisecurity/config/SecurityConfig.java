package com.swaggeruisecurity.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(auth -> {
                auth
                    .requestMatchers("/login-page").permitAll()
                    .anyRequest().authenticated();
            })
            .formLogin(login ->
                login.loginPage("/login-page")
                    .loginProcessingUrl("/login-process")
                    .usernameParameter("username")
                    .passwordParameter("password")
                    .defaultSuccessUrl("/")
                    .failureUrl("/login-page?loginFail=true")
            );
        return http.build();
    }

    @Bean
    PasswordEncoder passwordEncoder () {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public InMemoryUserDetailsManager userDetailsService(PasswordEncoder passwordEncoder) {
        UserDetails user1 = User.builder()
            .username("od")
            .password(passwordEncoder.encode("1234"))
            .roles("USER")
            .build();
        UserDetails user2 = User.builder()
            .username("admin")
            .password(passwordEncoder.encode("1234"))
            .roles("ADMIN")
            .build();
        return new InMemoryUserDetailsManager(user1, user2);
    }
}
