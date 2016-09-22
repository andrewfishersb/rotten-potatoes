import org.sql2o.*;
import java.util.List;

public class Genre {

private String name;
private int id;

  public Genre (String name){
    this.name = name;
  }

  public String getName() {
    return name;
  }

  public int getId() {
    return id;
  }

  public static List<Genre> all() {
    String sql = "SELECT * FROM genres ORDER BY name ASC";
    try (Connection con = DB.sql2o.open()) {
      return con.createQuery(sql).executeAndFetch(Genre.class);
    }
  }

  public void save() {
    String sql = "INSERT INTO genres (name) VALUES (:name)";
    try (Connection con = DB.sql2o.open()) {
      this.id = (int) con.createQuery(sql, true)
        .addParameter("name", name)
        .executeUpdate()
        .getKey();
    }
  }

  @Override
  public boolean equals(Object otherGenre){
    if(!(otherGenre instanceof Genre)){
      return false;
    }else{
      Genre aGenre = (Genre) otherGenre;
      return this.getName().equals(aGenre.getName()) && this.getId() == aGenre.getId();
    }
  }

  public static Genre find(int id) {
    String sql = "SELECT * FROM genres WHERE id = :id";
    try (Connection con = DB.sql2o.open()) {
      return con.createQuery(sql).addParameter("id", id).executeAndFetchFirst(Genre.class);
    }
  }

  public void delete(){
    String sql = "DELETE FROM genres WHERE id = :id";
    try(Connection con = DB.sql2o.open()){
      con.createQuery(sql).addParameter("id",id).executeUpdate();
    }
  }

  public void update(String name){
    try(Connection con = DB.sql2o.open()){
      String sql = "UPDATE genres SET name= :name WHERE id= :id";
      con.createQuery(sql).addParameter("name",name).addParameter("id",id).executeUpdate();
    }
  }

  public List<Movie> getMovies(){
    String sql = "SELECT * FROM movies WHERE genreId =:id ORDER BY name ASC";
    try(Connection con = DB.sql2o.open()){
      return con.createQuery(sql).addParameter("id",this.id).executeAndFetch(Movie.class);
    }
  }



}
