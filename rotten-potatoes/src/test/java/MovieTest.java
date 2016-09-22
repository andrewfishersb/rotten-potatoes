import org.sql2o.*;
import org.junit.*;
import static org.junit.Assert.*;

public class MovieTest {

  @Before
  public void setUp() {
DB.sql2o = new Sql2o("jdbc:postgresql://localhost:5432/rotten_potatoes_test", null, null);  }

  @After
    public void tearDown() {
      try(Connection con = DB.sql2o.open()) {
        String deleteMoviesQuery = "DELETE FROM movies *;";
        String deleteGenresQuery = "DELETE FROM genres *;";
        String deleteReviewsQuery = "DELETE FROM reviews *;";
        con.createQuery(deleteMoviesQuery).executeUpdate();
        con.createQuery(deleteGenresQuery).executeUpdate();
        con.createQuery(deleteReviewsQuery).executeUpdate();
      }
    }

 @Test
public void equals_returnsTrueIfDescriptionsAretheSame() {
  Movie firstMovie = new Movie("Magnificent 7","September 23,2016" ,1);
  Movie secondMovie = new Movie("Magnificent 7","September 23,2016" ,1);
  assertTrue(firstMovie.equals(secondMovie));
}

 @Test
 public void save_SavesInformation_true(){
   Movie aMovie = new Movie("Magnificent 7","September 23,2016", 1);
   aMovie.save();
   assertTrue(Movie.all().get(0).equals(aMovie));
 }

 @Test
 public void all_returnsAllInstancesOfMovie_true() {
   Movie firstMovie = new Movie("Magnificent 7","September 23,2016", 1);
   firstMovie.save();
   Movie secondMovie = new Movie("The Unforgiven", "September 18, 1992", 2);
   secondMovie.save();
   assertEquals(true, Movie.all().get(0).equals(firstMovie));
   assertEquals(true, Movie.all().get(1).equals(secondMovie));
 }

 @Test
 public void find_FindsASpecificItemById_Movie(){
   Movie firstMovie = new Movie("Magnificent 7","September 23,2016", 1);
   firstMovie.save();
   Movie secondMovie = new Movie("The Unforgiven", "September 18, 1992", 2);
   secondMovie.save();
   assertTrue(Movie.find(secondMovie.getId()).equals(secondMovie));
 }

 @Test
  public void delete_deletesMovie_true() {
    Movie myMovie = new Movie("Magnificent 7","September 23,2016", 1);
    myMovie.save();
    int myMovieId = myMovie.getId();
    myMovie.delete();
    assertEquals(null, Movie.find(myMovieId));
  }
  @Test
  public void update_UpdatesTheMovie_Void(){
    Movie aMovie = new Movie("Magnificent 7","September 23,2016", 1);
    aMovie.save();
    aMovie.update("Unforgiven","August 15, 2016", 2);
    assertEquals("Unforgiven",Movie.find(aMovie.getId()).getName());
    assertEquals("August 15, 2016",Movie.find(aMovie.getId()).getReleaseDate());
    assertEquals(2 ,Movie.find(aMovie.getId()).getGenreId());
  }

  // @Test
  // public void averageRating_CalculatesTheAverageRating_70(){
  //   Movie aMovie = new Movie("Dazed and Confused","Today",1);
  //   aMovie.save();
  //   Review firstReview = new Review("Mickey Mouse","It was amazing",90,aMovie.getId());
  //   firstReview.save();
  //   Review secondReview = new Review("Minny Mouse","Eh",50,aMovie.getId());
  //   secondReview.save();
  //   int rating = aMovie.averageRating();
  //   assertEquals(75,aMovie.getId());
  // }

  @Test
  public void getReviews_RetrieveAllREviewsFromOneMovie_List(){
    Movie aMovie = new Movie("Magnificent 7","September 23,2016", 1);
    aMovie.save();
    Review aReview = new Review("Magnificent","good",90 ,aMovie.getId());
    aReview.save();
    Review anotherReview = new Review("Ugly","bad",30 ,aMovie.getId());
    anotherReview.save();
    assertEquals(90,aMovie.getReviews().get(0).getRating());

  }
}
