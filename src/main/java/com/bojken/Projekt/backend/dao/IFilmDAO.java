package com.bojken.Projekt.backend.dao;

import org.apache.catalina.connector.Response;
import org.springframework.http.ResponseEntity;

public interface IFilmDAO {
    FilmModel save(FilmModel filmModel);

    List<FilmModel> findAll();

    Optional<FilmModel> findByTitle(String filmName);

    Optional<FilmModel> findById(Integer filmId);

    void deleteById(Integer filmId);

    Optional<FilmModel> findByTitleIgnoreCase(String filmName);

    ResponseEntity<Response> getFilmById(int id);

    ResponseEntity<Response> saveFilmById(String movie, int id);
}
