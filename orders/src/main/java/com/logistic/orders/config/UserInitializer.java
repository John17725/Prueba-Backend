package com.logistic.orders.config;

import com.logistic.orders.entity.User;
import com.logistic.orders.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Configuration
public class UserInitializer {
    @Value("${spring.security.user.name}")
    private String username;

    @Value("${spring.security.user.password}")
    private String password;

    private static final Logger logger = LoggerFactory.getLogger(UserInitializer.class);

    @Bean
    public CommandLineRunner loadAdminUser(UserRepository userRepository, PasswordEncoder passwordEncoder) {

        return args -> {
            if (userRepository.findByUsername(username).isEmpty()) {
                User user = new User();
                user.setUsername(username);
                user.setPassword(passwordEncoder.encode(password));
                userRepository.save(user);
                logger.info("Usuario precargado desde spring.security.user");
            } else {
                logger.warn("Usuario admin ya existe");
            }
        };
    }
}
