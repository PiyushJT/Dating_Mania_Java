import java.sql.*;
import java.util.HashMap;

public class Hobby {

    static HashMap<Integer, String> hobbies = new HashMap<>();

    static {
        hobbies.put(1, "Photography");
        hobbies.put(2, "Traveling");
        hobbies.put(3, "Cooking");
        hobbies.put(4, "Reading");
        hobbies.put(5, "Fitness/Gym");
        hobbies.put(6, "Dancing");
        hobbies.put(7, "Painting");
        hobbies.put(8, "Gaming");
        hobbies.put(9, "Hiking");
        hobbies.put(10, "Music");
        hobbies.put(11, "Yoga");
        hobbies.put(12, "Writing");
        hobbies.put(13, "Movies");
        hobbies.put(14, "Sports");
        hobbies.put(15, "Gardening");
        hobbies.put(16, "Cycling");
        hobbies.put(17, "Swimming");
        hobbies.put(18, "Singing");
        hobbies.put(19, "Stand-up Comedy");
        hobbies.put(20, "Blogging");
    }



    int hobby_id;
    String hobby_name;

    public Hobby(int hobby_id, String hobby_name) {
        this.hobby_id = hobby_id;
        this.hobby_name = hobby_name;
    }

    public String getHobby_name() {
        return hobby_name;
    }


    static Hobby fromDB(ResultSet rs) throws SQLException {


        int hobby_id = rs.getInt("hobby_id");
        String hobby_name = rs.getString("hobby_name");

        return new Hobby(hobby_id, hobby_name);

    }

}