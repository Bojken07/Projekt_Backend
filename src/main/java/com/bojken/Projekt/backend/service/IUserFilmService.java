package com.bojken.Projekt.backend.service;

import com.bojken.Projekt.backend.model.CustomUser;
import com.bojken.Projekt.backend.model.FilmModel;
import com.bojken.Projekt.backend.model.UserFilm;

import java.util.Optional;

public interface IUserFilmService {
    Optional<UserFilm> findByFilmModelAndCustomUser (FilmModel film, CustomUser user);

    void saveUserFilm(UserFilm userFilm);
}
