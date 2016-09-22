import org.sql2o.*;
import org.junit.*;
import static org.junit.Assert.*;

public class ReviewTest {

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
 Review firstReview = new Review("Magnificent","Good",90 ,1);
 Review secondReview = new Review("Magnificent","Good",90 ,1);
 assertTrue(firstReview.equals(secondReview));
}

@Test
public void save_SavesInformation_true(){
  Review firstReview = new Review("Magnificent","Good",90 ,1);
  Review secondReview = new Review("Bad","Bad",50 ,1);
  firstReview.save();
  secondReview.save();
  assertEquals(Review.all().get(1).getUserReview(),firstReview.getUserReview());
}

}
