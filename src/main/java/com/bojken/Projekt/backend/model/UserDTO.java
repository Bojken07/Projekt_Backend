package com.bojken.Projekt.backend.model;


import com.bojken.Projekt.backend.authorities.UserRole;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.antlr.v4.runtime.misc.NotNull;

public record UserDTO(

        @NotBlank(message = "Du får inte ha den tom")
        @Size(message = "Måste vara minst 3, inte mer än 10", min = 3, max = 10)
        String username,

        @NotBlank(message = "Du får inte ha den tom")
        @Size(message = "Måste vara minst 3, inte mer än 10", min = 3, max = 10)
        String password,

        //@NotNull(message = "ska vara något")
        UserRole userRole

) {
//    public UserDTO(CustomUser customUser) {
//        this(customUser.getUsername(), customUser.getPassword());
//
//    }


}