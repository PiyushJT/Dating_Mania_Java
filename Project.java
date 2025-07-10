import java.sql.*;
import java.util.ArrayList;

public class Project {

    public static void main(String[] args) throws SQLException {

        ArrayList<User> users;

        Log.S("System Started");

        // Database Initialization
        String url = "jdbc:postgresql://localhost:5432/Dating";
        String user = "postgres";
        String password = "";

        Connection connection;

        // Connect to Database
        try {

            Class.forName("org.postgresql.Driver");
            connection = DriverManager.getConnection(url, user, password);

            Log.S("Connected to PostgreSQL successfully!");

            CurrentUser.initUserData(connection);

            System.out.println(CurrentUser.data);

        }
        catch (ClassNotFoundException e) {
            System.out.println("JDBC Driver not found.");
            Log.E("JDBC Driver not found.");
            return;
        }
        catch (SQLException e) {
            System.out.println("Connection failed.");
            Log.E("Connection failed.");
            return;
        }
        catch (Exception e) {
            System.out.println("Database was unable to load! :(");
            Log.E("Database was unable to load! :(");
            return;
        }

        try {
            users = DatabaseIO.getUsers(connection);
            Log.DB("Users loaded successfully!");
        }
        catch (SQLException e) {
            System.out.println("Database was unable to load! :(");
            Log.E("Database was unable to load! :(");
            return;
        }

        Utility.printWelcome();


        if (CurrentUser.data == null) {

            Utility.openLoginMenu();

        }

    }

}