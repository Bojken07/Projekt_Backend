package com.bojken.Projekt.backend.repository;

import com.bojken.Projekt.backend.model.CustomUser;
import com.bojken.Projekt.backend.model.FilmModel;
import com.bojken.Projekt.backend.model.UserFilm;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserFilmRepository extends JpaRepository<UserFilm, Long> {

    Optional<UserFilm> findByFilmModelAndCustomUser (FilmModel film, CustomUser user);
}
