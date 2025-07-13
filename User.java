import java.sql.*;

public class User {

    // User data
    int uid;
    String name;
    char gender;


    // Constructor
    public User(int uid, String name, char gender) {
        this.uid = uid;
        this.name = name;
        this.gender = gender;
    }


    // Pretty print
    public String toString() {
        return String.format("%-4d %-20s %-2c", uid, name, gender);
    }


    // Function to load user from "database result set"
    static User fromDB(ResultSet rs) throws SQLException {

        int id = rs.getInt("uid");
        String name = rs.getString("name");
        char gender = rs.getString("gender").charAt(0);

        return new User(id, name, gender);

    }

}