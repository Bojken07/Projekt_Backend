package com.bojken.Projekt.backend.repository;

import com.bojken.Projekt.backend.model.FilmModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

@org.springframework.stereotype.Repository
public interface FilmRepository extends JpaRepository<FilmModel, Integer> {

    Optional<FilmModel> findByTitle (String title);
    Optional<FilmModel> findByTitleIgnoreCase (String title);

}