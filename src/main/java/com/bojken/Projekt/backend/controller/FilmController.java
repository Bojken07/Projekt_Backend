package com.bojken.Projekt.backend.controller;

import ch.qos.logback.core.model.Model;
import com.bojken.Projekt.backend.model.CustomUser;
import com.bojken.Projekt.backend.model.FilmDTO;
import com.bojken.Projekt.backend.model.FilmModel;
import com.bojken.Projekt.backend.service.IFilmService;
import com.bojken.Projekt.backend.service.IUserService;
import org.apache.catalina.connector.Response;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

@Controller
public class FilmController {

    @Value("${app.username}")
    private String username;

    @Value("${app.password}")
    private String password;

    private final IFilmService filmService;
    private final IUserService userService;
    private final WebClient webClient;

    public FilmController (IFilmService filmService, IUserService userService, WebClient.Builder webClientBuilder) {
        this.filmService = filmService;
        this.userService = userService;
        this.webClient = webClientBuilder
                .baseUrl("https://localhost:8443/films/")
                .filter((request, next) -> {
                    System.out.println("Request Headers: " + request.headers());
                    return next.exchange(request);
                })
                .build();
    }

    @GetMapping("/")
    public String indexPage (Model model) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        String username = authentication.getName();

        CustomUser user = userService.findUserByUsername(username).get();

        List<FilmModel> filmList = user.getFilmList();

        //TODO - Showing films saved by logged in user
        //model.addAttribute("films", filmService.findAll());
        model.addAttribute("films", filmList);
        model.addAttribute("username", username);

        return "index";
    }

    @GetMapping("/movies/savedfilms")
    public String getSavedFilms (Model model) {

        List<FilmModel> films = filmService.findAll();

        /*for (FilmModel film : films) {

            String base64Image = Base64.getEncoder().encodeToString(film.getImage());

            film.setBase64Image(base64Image);

        }*/

        model.addAttribute("films", films);

        return "saved-films";
    }

    @GetMapping("/movies/search")
    public String searchMoviesPage (Model model) {

        model.addAttribute("filmName", "");
        model.addAttribute("film", new FilmDTO());
        model.addAttribute("error", "");

        return "search-page";
    }

    @PostMapping("/movies/search")
    public String searchMovies (@RequestParam String filmName, Model model) {

//        ResponseEntity<Response> response = filmService.searchFilmByName(filmName);
//
//        if (response.getStatusCode().is2xxSuccessful()) {
//            FilmDTO film = (FilmDTO) response.getBody();
//
//            model.addAttribute("film", film);
//            model.addAttribute("filmName", filmName);
//
//        } else {
//
//            model.addAttribute("error", "ingen sån film");
//        }

        if (filmService.findByTitleIgnoreCase(filmName).isPresent()) {
            //FilmModel response1 = filmService.findByTitle(filmName).get();
            FilmModel response = filmService.findByTitleIgnoreCase(filmName.trim().toLowerCase()).get();

            FilmDTO film = new FilmDTO();

            film.setTitle(response.getTitle());
            film.setId((long) response.getFilmid());
            model.addAttribute("film", film);
            model.addAttribute("filmName", filmName);
        } else {
            model.addAttribute("error", "ingen sån film");
        }


        return "search-page";
    }

    @GetMapping("/movies/searchid")
    public String searchIdPage () {

        return "searchid-page";
    }


    @PostMapping("/movies/searchid")
    public String search (@RequestParam("filmId") String filmId, Model model) {

        System.out.println("in postMapping for searchid");
        System.out.println("username: " + username);
        System.out.println("password: " + password);

        // TODO - plenty! Check the username and password, and change to https, also error handle
        FilmModel film1 = null;

        try {
            // retrieving the session cookie, "JSESSIONID" after a log in with hard coded username and password
            ResponseEntity<Response> response = filmService.getFilmById(Integer.parseInt(filmId));

            if (response.getBody() instanceof FilmModel) {
                film1 = (FilmModel) response.getBody();

            } else {

                model.addAttribute("error", "ingen film med id: " + filmId);
                return "searchid-page";
            }

        } catch (Exception e) {

            model.addAttribute("error", "ingen film med id: " + filmId);
            return "searchid-page";
        }

        System.out.println("filmId: " + filmId);

        model.addAttribute("film", film1);

        //return "film-details";
        return "searchid-page";
    }

    @PostMapping("/movies/getfilm")
    public String getFilm (@ModelAttribute FilmModel film, Model model) {


        System.out.println("film.title: " + film.getTitle());
        System.out.println("film.id: " + film.getId());
        System.out.println("film.poster_path: " + film.getPoster_path());

        List<FilmModel> userFilms = userService.findUserByUsername(SecurityContextHolder.getContext().getAuthentication().getName()).get().getFilmList();

        for (FilmModel filmModel : userFilms) {

            if (Objects.equals(filmModel.getTitle(), film.getTitle())) {

                model.addAttribute("saved", "filmen är redan sparad");
            }

        }

        model.addAttribute("film", film);
        return "film-details";

    }


    @PostMapping("/movies/savefilm")
    public String saveFilm (@ModelAttribute FilmModel filmModel, Model model) throws IOException {

        //FilmModel film;

        int filmId = filmModel.getId();
        //ResponseEntity response1 = filmService.save(filmModel);

        try {

            if (filmService.getFilmById( filmId).getBody() instanceof FilmModel) {

                filmService.saveFilmById("movie", filmId);

            } else {

                model.addAttribute("error", "ingen film med id: " + filmId);
                return "searchid-page";
            }


        } catch (Exception e) {

            model.addAttribute("error", "ingen film med id: " + filmId);
            return "searchid-page";
        }

        //model.addAttribute("username", SecurityContextHolder.getContext().getAuthentication().getName());
        return "redirect:/";
    }


}