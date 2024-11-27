package com.bojken.Projekt.backend.config;

import com.bojken.Projekt.backend.model.CustomUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailService implements UserDetailsService {

    private final IUserService userService;

    @Autowired
    public CustomUserDetailService (IUserService userService) {
        this.userService = userService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        CustomUser user = userService.findUserByUsername(username).get();

        return new CustomUserDetails(user);

    }
}