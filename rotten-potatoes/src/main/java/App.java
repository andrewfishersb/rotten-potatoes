import java.util.HashMap;
import java.util.Map;
import spark.ModelAndView;
import spark.template.velocity.VelocityTemplateEngine;
import static spark.Spark.*;

public class App {
  public static void main(String[] args) {
    staticFileLocation("/public");
    String layout = "templates/layout.vtl";

    get("/", (request, response) -> {
      Map<String, Object> model = new HashMap<String, Object>();
      model.put("genres", Genre.all());
      model.put("template", "templates/index.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

//posts genre to homepage
    post("/", (request, response) -> {
      Map<String, Object> model = new HashMap<String, Object>();
      String genreName = request.queryParams("genre");
      Genre newGenre = new Genre(genreName);
      newGenre.save();
      model.put("genres", Genre.all());
      model.put("template", "templates/index.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

//gets genre page
    get("/genre/:id", (request, response) -> {
      Map<String, Object> model = new HashMap<String, Object>();
      Genre theGenre = Genre.find(Integer.parseInt(request.params(":id")));
      model.put("genre", theGenre);
      model.put("movies", theGenre.getMovies());
      model.put("template", "templates/genre.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

//prints movies on genre page
    post("/genre/genreId", (request, response) -> {
      Map<String, Object> model = new HashMap<String, Object>();
      Genre theGenre = Genre.find(Integer.parseInt(request.queryParams("genreId")));
      String movieTitle = request.queryParams("movie-title");
      String movieReleaseDate = request.queryParams("movie-release-date");
      Movie theMovie = new Movie(movieTitle,movieReleaseDate,theGenre.getId());
      theMovie.save();
      String url = String.format("/genre/%d", theMovie.getGenreId());
      response.redirect(url);
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

//lists all movies
    get("/movies", (request, response) -> {
      Map<String, Object> model = new HashMap<String, Object>();
      model.put("movies", Movie.all());
      model.put("template", "templates/movies.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());


//shows movie information
    get("/genre/:genreId/movie/:id", (request, response) -> {
      Map<String, Object> model = new HashMap<String, Object>();
      Genre aGenre = Genre.find(Integer.parseInt(request.params(":genreId")));
      Movie aMovie = Movie.find(Integer.parseInt(request.params(":id")));
      aMovie.formatDate();
      model.put("movie", aMovie);
      model.put("reviews",aMovie.getReviews());
      model.put("genre", aGenre);
      model.put("template", "templates/movie.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

//updates movie
    post("/genre/:genreId/movie/:id", (request, response) -> {
      Map<String, Object> model = new HashMap<String, Object>();
      Movie aMovie = Movie.find(Integer.parseInt(request.params("id")));
      String name = request.queryParams("name");
      String releaseDate = request.queryParams("releaseDate");
      Genre aGenre = Genre.find(aMovie.getGenreId());
      aMovie.update(name,releaseDate, aMovie.getGenreId());
      aMovie.formatDate();
      String url = String.format("/genre/%d/movie/%d", aGenre.getId(), aMovie.getId());
      response.redirect(url);
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    //deletes movie
    post("/genre/:genreId/movie/:id/delete", (request, response) -> {
      HashMap<String, Object> model = new HashMap<String, Object>();
      Movie aMovie = Movie.find(Integer.parseInt(request.params("id")));
      Genre aGenre = Genre.find(aMovie.getGenreId());
      aMovie.delete();
      model.put("movies",Movie.all());
      model.put("genre", aGenre);
      model.put("template", "templates/genre.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    //updates genre
    post("/genre/:id", (request, response) -> {
      Map<String, Object> model = new HashMap<String, Object>();
      Genre aGenre = Genre.find(Integer.parseInt(request.params("id")));
      String name = request.queryParams("genre");
      aGenre.update(name);
      String url = String.format("/genre/%d", aGenre.getId());
      response.redirect(url);
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    //deletes a genre
    post("/genre/:id/delete", (request, response) -> {
      HashMap<String, Object> model = new HashMap<String, Object>();
      Genre aGenre = Genre.find(Integer.parseInt(request.params("id")));
      aGenre.delete();
      for(Movie aMovie : aGenre.getMovies()){
        aMovie.delete();
      }
      model.put("genres",Genre.all());
      model.put("template", "templates/index.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    //post review
    post("/genre/:genreId/movie/:id/added-review", (request, response) -> {
      Map<String, Object> model = new HashMap<String, Object>();
      Movie aMovie = Movie.find(Integer.parseInt(request.params("id")));
      String userName =request.queryParams("username");
      int userRating =Integer.parseInt(request.queryParams("rating"));
      String userReview = request.queryParams("userreview");
      Genre aGenre = Genre.find(aMovie.getGenreId());
      Review aReview = new Review(userName,userReview,userRating,aMovie.getId());
      aReview.save();
      aMovie.formatDate();
      model.put("movie",aMovie);
      model.put("reviews",aMovie.getReviews());
      model.put("genre",aGenre);
      model.put("template", "templates/movie.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());
    //shows review
    get("/genre/:genreId/movie/:id/added-review", (request, response) -> {
      Map<String, Object> model = new HashMap<String, Object>();
      Genre aGenre = Genre.find(Integer.parseInt(request.params("genreId")));
      Movie aMovie = Movie.find(Integer.parseInt(request.params("id")));
      // Review aReview = new Review("hi","good",90,aMovie.getId());
      // aReview.save();
      model.put("reviews",aMovie.getReviews());
      aMovie.formatDate();
      model.put("movie", aMovie);
      model.put("genre", aGenre);
      model.put("template", "templates/movie.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());
  }
}
