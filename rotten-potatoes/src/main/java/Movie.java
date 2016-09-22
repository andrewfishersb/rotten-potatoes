import org.sql2o.*;
import java.util.List;
import java.util.ArrayList;

public class Movie{
  private String name;
  private String releaseDate;
  private int id;
  private int genreId;


  public Movie(String name, String releaseDate, int genreID){
    this.name = name;
    this.releaseDate = releaseDate;
    this.genreId = genreID;
  }

  public String getName(){
    return name;
  }

  public String getReleaseDate(){
    return releaseDate;
  }

  public int getId(){
    return id;
  }

  public int getGenreId(){
    return genreId;
  }


  public static List<Movie> all(){
    String sql = "SELECT * FROM movies ORDER BY name ASC";
    try(Connection con = DB.sql2o.open()){
      return con.createQuery(sql).executeAndFetch(Movie.class);
    }
  }

  @Override
  public boolean equals(Object otherMovie){
    if (!(otherMovie instanceof Movie)) {
      return false;
    } else {
      Movie newMovie = (Movie) otherMovie;
      return this.getName().equals(newMovie.getName()) &&
             this.getReleaseDate().equals(newMovie.getReleaseDate()) &&
             this.getId() == newMovie.getId() &&
             this.getGenreId() == newMovie.getGenreId();
    }
  }

  public void save(){
    String sql = "INSERT INTO movies (name,releaseDate,genreId) VALUES (:name,:releaseDate,:genreId)";
    try(Connection con = DB.sql2o.open()){
      this.id = (int) con.createQuery(sql,true)
      .addParameter("name",this.name)
      .addParameter("releaseDate",this.releaseDate)
      .addParameter("genreId",this.genreId).executeUpdate().getKey();
    }
  }

  public static Movie find(int id){
    String sql = "SELECT * FROM movies WHERE id =:id";
    try(Connection con = DB.sql2o.open()){
      return con.createQuery(sql).addParameter("id",id).executeAndFetchFirst(Movie.class);
    }
  }

  public void delete() {
    String sql = "DELETE FROM movies WHERE id=:id";
    try(Connection con = DB.sql2o.open()){
      con.createQuery(sql).addParameter("id", id).executeUpdate();
    }
  }

  public void update (String name, String releaseDate, int genreId) {
    String sql = "UPDATE movies SET name = :name, releaseDate = :releaseDate, genreID = :genreId WHERE id = :id";
    try(Connection con = DB.sql2o.open()) {
      con.createQuery(sql).addParameter("name", name).addParameter("releaseDate", releaseDate).addParameter("genreId", genreId).addParameter("id", this.id).executeUpdate();
    }
  }
  public void formatDate(){
    String [] dateArray = releaseDate.split("-");
    this.releaseDate = dateArray[1]+"/"+dateArray[2]+"/"+dateArray[0];
  }

  public List<Review> getReviews(){ //list here
    try(Connection con = DB.sql2o.open()){
      String sql = "SELECT * FROM reviews WHERE movieid =:id ORDER BY rating DESC";
      return con.createQuery(sql).addParameter("id",this.id).executeAndFetch(Review.class);
    }
  }

  public int averageRating(){
    List<Review> allReviews = this.getReviews();
    int average = 0;
    for(int i =0;i<allReviews.size();i++){
      average+=allReviews.get(i).getRating();
    }
    if(allReviews.size()<=0){
      return 0;
    }else{
      return average/allReviews.size();
    }
  }
}
