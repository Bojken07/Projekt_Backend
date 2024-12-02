package com.bojken.Projekt.backend.repository;

import com.bojken.Projekt.backend.authorities.UserRole;
import com.bojken.Projekt.backend.model.CustomUser;
import com.bojken.Projekt.backend.model.UserDTO;
import com.bojken.Projekt.backend.service.IUserService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DBInit {

    @Value("${app.username}")
    private String username;

    @Value("${app.password}")
    private String password;

    private IUserService userService;

    private PasswordEncoder passwordEncoder;

    @Autowired
    public DBInit (IUserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @PostConstruct
    public void createUser () {

        CustomUser user = new CustomUser();

        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        user.setUserRole(UserRole.ADMIN);
        user.setAccountNonExpired(true);
        user.setAccountNonLocked(true);
        user.setCredentialNonExpired(true);
        user.setEnabled(true);

        CustomUser user2 = new CustomUser();

        user2.setUsername("test2");
        user2.setPassword(passwordEncoder.encode(password));
        user2.setUserRole(UserRole.USER);
        user2.setAccountNonExpired(true);
        user2.setAccountNonLocked(true);
        user2.setCredentialNonExpired(true);
        user2.setEnabled(true);

        UserDTO user0 = new UserDTO("test", "test", UserRole.ADMIN);
        UserDTO user1 = new UserDTO("test2", "test", UserRole.USER);



        userService.saveUser(user0);
        userService.saveUser(user1);
    }

}