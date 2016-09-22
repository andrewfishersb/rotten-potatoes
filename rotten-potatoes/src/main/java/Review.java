import org.sql2o.*;
import java.util.List;

public class Review {
  private String userName;
  private String userReview;
  private int rating;
  private int id;
  private int movieId;

  public Review (String userName, String userReview, int rating, int movieId){
    this.userName = userName;
    this.userReview = userReview;
    this.rating = rating;
    this.movieId = movieId;
  }

  public String getUserName() {
    return userName;
  }

  public String getUserReview() {
    return userReview;
  }

  public int getRating() {
    return rating;
  }

  public int getId() {
    return id;
  }

  public int getMovieId() {
    return movieId;
  }


public static List<Review> all(){
  String sql = "SELECT * FROM reviews";
  try(Connection con = DB.sql2o.open()){
    return con.createQuery(sql).executeAndFetch(Review.class);
  }
}

  @Override
  public boolean equals(Object otherReview){
    if (!(otherReview instanceof Review)) {
      return false;
    } else {
      Review newReview = (Review) otherReview;
      return this.getUserName().equals(newReview.getUserName()) &&
             this.getUserReview().equals(newReview.getUserReview()) &&
             this.getId() == newReview.getId() && this.getRating()==newReview.getRating();
    }
  }

  public void save(){
    String sql = "INSERT INTO reviews (username,userreview,rating, movieid) VALUES (:username,:userreview,:rating,:movieid)";
    try(Connection con = DB.sql2o.open()){
      this.id = (int) con.createQuery(sql,true).addParameter("username",this.userName).addParameter("userreview",this.userReview).addParameter("rating",this.rating).addParameter("movieid",this.movieId).executeUpdate().getKey();
    }
  }

  public static Review find(int id){
    String sql = "SELECT * FROM reviews WHERE id =:id";
    try(Connection con = DB.sql2o.open()){
      return con.createQuery(sql).addParameter("id",id).executeAndFetchFirst(Review.class);
    }
  }

  public void delete() {
    String sql = "DELETE FROM reviews WHERE id=:id";
    try(Connection con = DB.sql2o.open()){
      con.createQuery(sql).addParameter("id",id).executeUpdate();
    }
  }

  public void update (String userName, String userReview, int rating, int movieId) {
    String sql = "UPDATE reviews SET userName = :userName, userReview = :userReview, rating = :rating WHERE id = :id";
    try(Connection con = DB.sql2o.open()) {
      con.createQuery(sql).addParameter("userName", userName).addParameter("userReview", userReview).addParameter("rating", rating).addParameter("movieId",movieId).addParameter("id", this.id).executeUpdate();
    }
  }
}
