package com.bojken.Projekt.backend.service;

import com.bojken.Projekt.backend.model.CustomUser;
import com.bojken.Projekt.backend.model.UserDTO;

import java.util.List;
import java.util.Optional;

public interface IUserService {
    String saveUser (UserDTO userDTO);

    Optional<CustomUser> findUserByUsername(String username);

    List<CustomUser> getAllUsers();

    void deleteUserById(Long id);
}
