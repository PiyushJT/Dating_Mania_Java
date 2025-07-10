import java.sql.*;
import java.util.ArrayList;

public class DatabaseIO {

    static ArrayList<User> getUsers(Connection connection) throws SQLException {

        ArrayList<User> users = new ArrayList<>();

        Statement st = connection.createStatement();
        String query = "SELECT * FROM \"user\"";

        ResultSet rs = st.executeQuery(query);

        while (rs.next())
            users.add(User.fromDB(rs));

        return users;

    }

    static User getUserFromUid(Connection connection, int uid) throws SQLException {

        Statement st = connection.createStatement();
        String query = "SELECT * FROM \"user\" WHERE uid = " + uid + ";";

        ResultSet rs = st.executeQuery(query);

        return User.fromDB(rs);



    }

}