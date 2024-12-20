package com.bojken.Projekt.backend.service;

import com.bojken.Projekt.backend.response.ListResponse;
import io.github.resilience4j.ratelimiter.RateLimiter;

import com.bojken.Projekt.backend.client.FilmApiClient;
import com.bojken.Projekt.backend.dao.IFilmDAO;
import com.bojken.Projekt.backend.model.FilmDTO;
import com.bojken.Projekt.backend.model.FilmModel;
import com.bojken.Projekt.backend.response.IntegerResponse;
import org.apache.catalina.connector.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.util.*;

@Service
public class FilmService implements IFilmService{

    @Value("${ApiKey}")
    private String ApiKey;

    //@Autowired
    //private final FilmRepository filmRepository;
    private final IFilmDAO filmDao;

    private final IUserService userService;
    //private final WebClient webClientConfig;
    private final FilmApiClient filmApiClient;
    private final RateLimiter rateLimiter;

    @Autowired
    public FilmService (//WebClient.Builder webClient,
                        FilmApiClient filmApiClient,
                        IFilmDAO filmDao, IUserService userService, RateLimiter rateLimiter) {
        //this.filmRepository = filmRepository;
        // this.webClientConfig = webClient.baseUrl("https://api.themoviedb.org/3/").build();
        this.filmDao = filmDao;
        this.userService = userService;
        this.rateLimiter = rateLimiter;
        this.filmApiClient = filmApiClient;
    }

    @Override
    public ResponseEntity<Response> getFilmById(int id) {

        Optional<FilmModel> film = filmApiClient.getFilmById(id);

        if (film.isPresent()) {
            return ResponseEntity.ok(film.get());
        } else {
            return ResponseEntity.status(404).body(new ErrorResponse("Film inte funnen"));
        }
        //return filmDao.getFilmById(id);

    }

    @Override
    public ResponseEntity<Response> saveFilmById(@RequestParam(defaultValue = "movie") String movie, @PathVariable int id) throws IOException {

        Optional<FilmModel> film = filmApiClient.getFilmById(id);

        if (film.isPresent()) {
            filmDao.saveFilm(film.get());
            return ResponseEntity.ok(film.get());
        } else {
            return ResponseEntity.status(404).body(new ErrorResponse("Film inte funnen"));
        }
        //return filmDao.saveFilmById(movie, id);
    }

    /*@Override
    public ResponseEntity<Response> save (FilmModel film) throws IOException {

        String poster = film.getPoster_path();

        String path = "https://image.tmdb.org/t/p/original/";

        String imagePath = path + poster;

        URL url = new URL(imagePath);

        URLConnection connection = url.openConnection();

        connection.connect();
        //TODO - Error handle if no image link present
        try (InputStream inputStream = connection.getInputStream();
             ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()
        ){

            byte[] buffer = new byte[1024];
            int bytesRead;

            while ((bytesRead = inputStream.read(buffer)) != -1) {
                byteArrayOutputStream.write(buffer, 0, bytesRead);
            }

            film.setImage(byteArrayOutputStream.toByteArray());


        }

        String base64 = Base64.getEncoder().encodeToString(film.getImage());

        film.setBase64Image(base64);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        String username = authentication.getName();

        CustomUser user = userService.findUserByUsername(username).get();

        System.out.println("film.getId: " + film.getId());
        System.out.println("film.getfilmid: " + film.getFilmid());
       // System.out.println("film.geCustomUser: " + film.getCustomUser());
        //System.out.println("current film: " + filmRepository.findById(film.getId()).get().);

       // List<FilmModel> allFilms = filmRepository.findAll();
        List<FilmModel> allFilms = filmDao.findAll();

        for (FilmModel film1 : allFilms) {

            if (film1.getId() == film.getId()) {

                //FilmModel currentFilm = filmRepository.findByTitle(film.getTitle()).get();
                FilmModel currentFilm = filmDao.findByTitle(film.getTitle()).get();

                //List<CustomUser> list = currentFilm.getCustomUser();

                //list.add(user);

                //currentFilm.setCustomUser(currentFilm.getCustomUser().add(user) );
                //currentFilm.setCustomUser(list);

                List<CustomUser> customUserList = currentFilm.getCustomUsers();
                customUserList.add(user);
                currentFilm.setCustomUsers(customUserList);

                return ResponseEntity.ok(filmDao.save(currentFilm));

            }

        }

       // List<CustomUser> list = film.getCustomUser();
        //list.add(user);

        //film.setCustomUser(list);

        //film.setCustomUser(user);
        //film.getCustomUsers().add(user);
        List<CustomUser> customUserList = new ArrayList<>();
        customUserList.add(user);

        film.setCustomUsers(customUserList);
        return ResponseEntity.ok(filmDao.save(film));
        //return filmRepository.findById(film.getId()).get();

    }*/

    @Override
    public List<FilmModel> findAll () {
        return filmDao.findAll();
    }

    @Override
    public ResponseEntity<Response> findById (Integer id) {

        try {

            Optional<FilmModel> optionalFilm = filmDao.findById(id);

            if (optionalFilm.isPresent()) {

                return ResponseEntity.ok((optionalFilm.get()));
            } else {

                return ResponseEntity.status(404).body(new ErrorResponse("film finns inte"));
            }
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new ErrorResponse("något fel"));
        }

    }

    @Override
    public Optional<FilmModel> getFilmById(Integer id) {
        return filmDao.findById(id);
    }

    @Override
    public ResponseEntity<String> deleteById (Integer id) throws Exception {

        // TODO - why this? Use in if statement perhaps
        Optional<FilmModel> optionalFilm = filmDao.findById(id);

        try {

            if (filmDao.findById(id).isPresent()) {

                filmDao.deleteById(id);
                return ResponseEntity.ok("Film med id "+ id + " tagen borta");

            } else {

                return ResponseEntity.status(404).body("no film found with id: " + id);
            }
        } catch (Exception e) {
            throw new Exception();
        }
    }

    @Override
    public ResponseEntity<Response> changeCountryOfOrigin (int id, String country) {

        List<String> newCountryOfOrigins = new ArrayList<>() {};

        newCountryOfOrigins.add(country);

        Optional<FilmModel> filmOptional = filmDao.findById(id);

        if (filmOptional.isEmpty()) {
            return ResponseEntity.status(404).body(new ErrorResponse("Film finns inte! <@:)"));
        }

        try {

            FilmModel film = filmOptional.get();

            film.setOrigin_country(newCountryOfOrigins);

            filmDao.save(film);

            return ResponseEntity.ok(film);

        } catch (NoSuchElementException e) {

            return ResponseEntity.status(404).body(new ErrorResponse("film finns inte"));
        }

    }

    @Override
    public ResponseEntity<Response> searchFilmByName (String filmName) {

        try {

            if (filmName == null || filmName.isBlank()) {
                return ResponseEntity.status(400).body(new ErrorResponse("Du måste skriva namn"));
            }

            //filmRepository.findByTitleIgnoreCase(filmName.trim().toLowerCase());

            List<FilmModel> allFilms = filmDao.findAll();

            for (FilmModel film : allFilms) {
                //System.out.println(film.getOriginal_title());

                if (film.getTitle().equals(filmName)) {

                    FilmDTO filmDto = new FilmDTO();
                    filmDto.setTitle(film.getTitle());
                    filmDto.setId((long) film.getFilmid());


                    return ResponseEntity.ok(filmDto);
                }
            }

            return ResponseEntity.status(404).body(new ErrorResponse("Ingen film funnen med namn: " + filmName));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new ErrorResponse("något fel"));
        }
    }

    @Override
    public ResponseEntity<Response> getFilmByCountry (String country, String title) {

        if (rateLimiter.acquirePermission()) {

            List<FilmModel> savedFilms = filmDao.findAll();

            List<FilmModel> filmsByCountry = new ArrayList<>();

            try {


                if (title == null || title.isBlank()) {

                    for (FilmModel film : savedFilms) {

                        if (film.getOrigin_country().get(0).equals(country.toUpperCase())) {

                            filmsByCountry.add(film);
                        }
                    }

                    return ResponseEntity.ok(new ListResponse(filmsByCountry));
                }

                for (FilmModel film : savedFilms) {

                    if (film.getOrigin_country().get(0).equals(country.toUpperCase()) && film.getOriginal_title().equals(title)) {

                        return ResponseEntity.ok(film);
                    }
                }

                return ResponseEntity.status(400).body(new ErrorResponse("Finns inte film: " + title));
            } catch (Exception e) {
                return ResponseEntity.status(500).body(new ErrorResponse("något fel"));
            }

        } else {
            return ResponseEntity.status(429).body(new ErrorResponse("för många förfrågan"));
        }
    }

    @Override
    public ResponseEntity<Response> getAverageRuntime () {

        try {

            List<FilmModel> films = filmDao.findAll();
            if (films.isEmpty()) {
                return ResponseEntity.status(404).body(new ErrorResponse("inga filmer sparade än"));
            }

            int runtimeInMin = 0;

            for (FilmModel film : films) {

                runtimeInMin += film.getRuntime();

            }

            return ResponseEntity.ok(new IntegerResponse(runtimeInMin / filmDao.findAll().size()));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new ErrorResponse("något fel"));
        }
    }

    @Override
    public ResponseEntity<String> addOpinion (Integer id, String opinion) {

        try {
            if (opinion == null || opinion.isEmpty() || opinion.isBlank()) {
                return ResponseEntity.status(400).body("måste ha body");
            }

            Optional<FilmModel> optionalFilm = filmDao.findById(id);

            if (optionalFilm.isPresent()) {

                optionalFilm.get().setOpinion(opinion);
                filmDao.save(filmDao.findById(id).get());
                return ResponseEntity.status(201).body("Opinion adderad!");

            } else {

                return ResponseEntity.status(404).body("kan int finne film");
            }
        } catch (Exception e) {
            return ResponseEntity.status(500).body("något fel");
        }
    }

    @Override
    public ResponseEntity<Response> getFilmWithAdditionalInfo(int filmId, boolean opinion, boolean description) {

        if (rateLimiter.acquirePermission()) {

            FilmModel film;
            FilmDTO filmDTO = new FilmDTO();
            try {

                if (filmDao.findById(filmId).isPresent()) {
                    film = filmDao.findById(filmId).get();
                } else {
                    return ResponseEntity.status(404).body(new ErrorResponse("Film finns inte"));
                }

                if (opinion == true && description == true) {
                    filmDTO.setDescription(film.getOverview());
                    filmDTO.setOpinion(film.getOpinion());
                    filmDTO.setTitle(film.getTitle());

                    return ResponseEntity.ok(filmDTO);
                }

                if (opinion == true) {
                    filmDTO.setTitle(film.getTitle());
                    filmDTO.setOpinion(film.getOpinion());
                    filmDTO.setDescription("inget här");

                    return ResponseEntity.ok(filmDTO);

                }

                if (description == true) {
                    filmDTO.setTitle(film.getTitle());
                    filmDTO.setDescription(film.getOverview());
                    filmDTO.setOpinion("inget här");

                    return ResponseEntity.ok(filmDTO);
                }
                filmDTO.setTitle(film.getTitle());
                filmDTO.setDescription("inget här");
                filmDTO.setOpinion("inget här");

                return ResponseEntity.ok(filmDTO);
            } catch (Exception e) {
                return ResponseEntity.status(500).body(new ErrorResponse("något fel på databas"));
            }

        } else {
            return ResponseEntity.status(429).body(new ErrorResponse("för många förfrågan"));
        }

    }

    @Override
    public ResponseEntity<Response> getInfo() {

        if (rateLimiter.acquirePermission()) {

            int USfilms = 0;
            int nonUSfilms = 0;

            ArrayList<FilmModel> adultFilms = new ArrayList<>();
            ArrayList<String> budgetFilms = new ArrayList<>();

            try {


                List<FilmModel> films = findAll();
                Collections.sort(films, new Comparator<FilmModel>() {
                    @Override
                    public int compare(FilmModel o1, FilmModel o2) {
                        return Integer.compare(o1.getBudget(), o2.getBudget());
                    }
                });

                for (FilmModel film : films) {

                    if (film.isAdult() == true) {
                        adultFilms.add(film);
                    }

                    if (Objects.equals(film.getOrigin_country().get(0), "US")) {
                        USfilms++;
                    } else {
                        nonUSfilms++;
                    }

                    System.out.println(film.getOriginal_title() + ": " + film.getBudget() + " origin country " + film.getOrigin_country().get(0));
                    budgetFilms.add(film.getOriginal_title() + " " + film.getBudget());
                }


                if (findAll().isEmpty()) {
                    return ResponseEntity.ok(new ErrorResponse("Du har inga sparade filmer"));
                }


                IntegerResponse intRes = (IntegerResponse) getAverageRuntime().getBody();
                int averageRuntime = intRes.getAverageRuntime();

                return ResponseEntity.ok(new ErrorResponse("du har: " + findAll().size() + " filmer sparade." + "\n\r" +
                        " medellängden på filmerna är: " + averageRuntime + " minuter, " +
                        "varav " + adultFilms.size() + " porrfilm(er)" + "budge rank " + budgetFilms + " av dessa är " + USfilms + " amerkikanska och resten " + nonUSfilms + " från andra länder"));


            } catch (Exception e) {
                return ResponseEntity.status(500).body(new ErrorResponse("något fel"));
            }
        } else {
            return ResponseEntity.status(429).body(new ErrorResponse("för många förfrågan"));
        }

    }

    @Override
    public Optional<FilmModel> findByTitle(String filmName) {

        return filmDao.findByTitle(filmName);
    }

    @Override
    public Optional<FilmModel> findByTitleIgnoreCase(String filmName) {

        return filmDao.findByTitleIgnoreCase(filmName);
    }
}
