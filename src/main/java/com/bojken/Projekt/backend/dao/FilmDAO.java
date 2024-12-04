package com.bojken.Projekt.backend.dao;


import com.bojken.Projekt.backend.model.FilmModel;
import com.bojken.Projekt.backend.repository.FilmRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class FilmDAO implements IFilmDAO {

    //@Value("${ApiKey}")
    //private String ApiKey;

    private final FilmRepository filmRepository;

    //private final RateLimiter rateLimiter;

    // private final WebClient webClientConfig;

    //private final IUserService userService;

    @Autowired
    public FilmDAO(FilmRepository filmRepository
                   //,WebClient.Builder webClient,
                   //RateLimiter rateLimiter,
                   // IUserService userService
    ) {
        this.filmRepository = filmRepository;
        //this.userService = userService;
        //this.rateLimiter = rateLimiter;
        //this.webClientConfig = webClient.baseUrl("https://api.themoviedb.org/3/").build();

    }

    @Override
    public FilmModel save(FilmModel filmModel) {
        return filmRepository.save(filmModel);
    }

    @Override
    public List<FilmModel> findAll() {
        return filmRepository.findAll();
    }

    @Override
    public Optional<FilmModel> findByTitle(String filmName) {
        return filmRepository.findByTitle(filmName);
    }

    @Override
    public Optional<FilmModel> findById(Integer filmId) {
        return filmRepository.findById(filmId);
    }

    @Override
    public void deleteById(Integer filmId) {
        filmRepository.deleteById(filmId);
    }

    @Override
    public Optional<FilmModel> findByTitleIgnoreCase(String filmName) {
        return filmRepository.findByTitleIgnoreCase(filmName);
    }
}