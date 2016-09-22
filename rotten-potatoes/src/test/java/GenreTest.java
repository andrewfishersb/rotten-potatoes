import org.sql2o.*;
import org.junit.*;
import static org.junit.Assert.*;

public class GenreTest {

  @Before
  public void setUp() {
    DB.sql2o = new Sql2o("jdbc:postgresql://localhost:5432/rotten_potatoes_test", null, null);
  }

  @After
    public void tearDown() {
      try(Connection con = DB.sql2o.open()) {
        String deleteMoviesQuery = "DELETE FROM movies *;";
        String deleteGenresQuery = "DELETE FROM genres *;";
        con.createQuery(deleteMoviesQuery).executeUpdate();
        con.createQuery(deleteGenresQuery).executeUpdate();
      }
    }
    @Test
   public void equals_returnsTrueIfDescriptionsAretheSame() {
     Genre firstGenre = new Genre("Western");
     Genre secondGenre = new Genre("Western");
     assertTrue(firstGenre.equals(secondGenre));
   }

    @Test
    public void save_SavesInformation_true(){
      Genre aGenre = new Genre("Western");
      aGenre.save();
      assertTrue(Genre.all().get(0).equals(aGenre));
    }

    @Test
    public void all_returnsAllInstancesOfGenre_true() {
      Genre firstGenre = new Genre("Western");
      firstGenre.save();
      Genre secondGenre = new Genre("Western");
      secondGenre.save();
      assertEquals(true, Genre.all().get(0).equals(firstGenre));
      assertEquals(true, Genre.all().get(1).equals(secondGenre));
    }
    @Test
    public void find_FindsASpecificItemById_Genre(){
      Genre firstGenre = new Genre("Western");
      firstGenre.save();
      Genre secondGenre = new Genre("Western");
      secondGenre.save();
      assertTrue(Genre.find(secondGenre.getId()).equals(secondGenre));
    }

    @Test
    public void delete_DeletesAGenre_Null(){
      Genre aGenre = new Genre("Comedy");
      aGenre.save();
      int genreId = aGenre.getId();
      aGenre.delete();
      assertEquals(null,Genre.find(genreId));
    }

    @Test
    public void update_UpdatesTheGenre_Void(){
      Genre aGenre = new Genre("Western");
      aGenre.save();
      aGenre.update("Sci-Fi");
      assertEquals("Sci-Fi",Genre.find(aGenre.getId()).getName());
    }

    @Test
    public void getMovies_RetrieveAllMoviesFromOneGenre_List(){
      Genre aGenre = new Genre("Western");
      aGenre.save();
      Movie aMovie = new Movie("Magnificent 7","Octorber 23,1960" ,aGenre.getId());
      aMovie.save();
      Movie anotherMovie = new Movie("Good the Bad and The Ugly","December 29,1966" ,aGenre.getId());
      anotherMovie.save();
      assertTrue(aGenre.getMovies().equals(Movie.all()));

    }
 }
