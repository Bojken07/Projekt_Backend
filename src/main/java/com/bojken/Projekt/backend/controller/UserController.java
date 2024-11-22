package com.bojken.Projekt.backend.controller;

import ch.qos.logback.core.model.Model;
import com.bojken.Projekt.backend.authorities.UserRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class UserController {

    private final IUserService userService;
    private final PasswordEncoder encoder;

    @Autowired
    public UserController (IUserService userService,
                           PasswordEncoder encoder
    ) {
        this.userService = userService;
        this.encoder = encoder;
    }



    @GetMapping("/login")
    public String loginPage () {

        return "login";
    }

    @GetMapping("/logout")
    public String logout () {

        return "logout";

    }

    @GetMapping("/register")
    public String registerPage (Model model) {


        model.addAttribute("user", new UserDTO("", "", null));
        model.addAttribute("roles", UserRole.values());
        return "register";
    }

    @PostMapping("/register")
    public String registerUser (@Valid @ModelAttribute("user") UserDTO userDTO, BindingResult bindingResult
            ,Model model

                                //,@ModelAttribute("roles") UserRole role
    ) {
        //System.out.println("role: " + role);

//        CustomUser newUser = new CustomUser(userDTO.username(), encoder.encode(userDTO.password()));
//        newUser.setUserRole(userDTO.userRole());
//        newUser.setAccountNonLocked(true);
//        newUser.setEnabled(true);
//
//        newUser.setAccountNonExpired(true);
//        newUser.setCredentialNonExpired(true);

        if (bindingResult.hasErrors()) {

            model.addAttribute("roles" , UserRole.values());
            //model.addAttribute("user" , new UserDTO("", "", null));

            return "register";
        }

        model.addAttribute("status", userService.saveUser(userDTO));
        model.addAttribute("roles" , UserRole.values());
        model.addAttribute("user" , new UserDTO("", "", null));

        return "register";
    }

    @GetMapping("/admin")
    public String adminPage (Model model) {

        List<CustomUser> userList = userService.getAllUsers();

        model.addAttribute("users", userList );

        return "admin-page";
    }

    @PostMapping("/delete")
    public String deleteUser (@ModelAttribute("id") Long id, Model model) {

        if (id == 1) {
            model.addAttribute("error", "kan inte radera användare med id: 1");
            model.addAttribute("users", userService.getAllUsers());
            return "admin-page";
        }

        userService.deleteUserById(id);


        return "redirect:/admin";

    }

}
