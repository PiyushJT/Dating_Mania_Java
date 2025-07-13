import java.sql.*;
import java.util.ArrayList;

public class DatabaseIO {

    static Connection connection;

    // function to get all users from database
    static ArrayList<User> getUsers() throws SQLException {

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
    static User getUserFromUid(int user_id) throws SQLException {


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

    static User getUserFromAuth(String email, String password) throws SQLException {


        // query
        Statement st = connection.createStatement();
        String query = """
SELECT *
FROM users
WHERE user_id = (
    SELECT user_id
    FROM auth
    WHERE email = '%s' AND password = '%s'
);
    """.formatted(email, password);

        // result
        ResultSet rs = st.executeQuery(query);

        // return the user if exists
        if (!rs.next())
            return null;

        return User.fromDB(rs);

    }

}