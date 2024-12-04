package com.bojken.Projekt.backend.service;

import com.bojken.Projekt.backend.model.CustomUser;
import com.bojken.Projekt.backend.model.FilmModel;
import com.bojken.Projekt.backend.model.UserFilm;
import com.bojken.Projekt.backend.repository.UserFilmRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserFilmService implements IUserFilmService {

    private final UserFilmRepository userFilmRepository;


    public UserFilmService(UserFilmRepository userFilmRepository) {
        this.userFilmRepository = userFilmRepository;
    }

    @Override
    public Optional<UserFilm> findByFilmModelAndCustomUser(FilmModel film, CustomUser user) {
        return userFilmRepository.findByFilmModelAndCustomUser(film, user);
    }

    @Override
    public void saveUserFilm(UserFilm userFilm) {
        userFilmRepository.save(userFilm);
    }

}