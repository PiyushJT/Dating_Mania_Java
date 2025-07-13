import java.sql.*;
import java.util.ArrayList;

public class DatabaseIO {


    // function to get all users from database
    static ArrayList<User> getUsers(Connection connection) throws SQLException {

        // base arraylist
        ArrayList<User> users = new ArrayList<>();

        // query
        Statement st = connection.createStatement();
        String query = "SELECT * FROM users";

        // result
        ResultSet rs = st.executeQuery(query);


        // getting users' data from result
        while (rs.next())
            users.add(User.fromDB(rs));

        return users;

    }


    // function to get user from uid
    static User getUserFromUid(Connection connection, int user_id) throws SQLException {


        // query
        Statement st = connection.createStatement();
        String query = "SELECT * FROM users WHERE user_id = " + user_id + ";";

        // result
        ResultSet rs = st.executeQuery(query);


        // return the user if exists
        if (!rs.next())
            return null;

        return User.fromDB(rs);

    }

}