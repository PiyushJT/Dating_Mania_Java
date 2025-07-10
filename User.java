import java.sql.*;

public class User {

    int uid;
    String name;
    char gender;

    public User(int uid, String name, char gender) {
        this.uid = uid;
        this.name = name;
        this.gender = gender;
    }

    public String toString() {
        return String.format("%-4d %-20s %-2c", uid, name, gender);
    }

    static User fromDB(ResultSet rs) {

        try {

            int id = rs.getInt("uid");
            String name = rs.getString("name");
            char gender = rs.getString("gender").charAt(0);

            return new User(id, name, gender);
        }
        catch (SQLException e) {
            e.printStackTrace();
            return null;
        }

    }
}