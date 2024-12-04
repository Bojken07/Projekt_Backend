package com.bojken.Projekt.backend.dao;

import com.bojken.Projekt.backend.model.FilmModel;
import org.apache.catalina.connector.Response;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;
import java.util.Optional;

public interface IFilmDAO {
    FilmModel save(FilmModel filmModel);

    List<FilmModel> findAll();

    Optional<FilmModel> findByTitle(String filmName);

    Optional<FilmModel> findById(Integer filmId);

    void deleteById(Integer filmId);

    Optional<FilmModel> findByTitleIgnoreCase(String filmName);

    void saveFilm(FilmModel filmModel);
}