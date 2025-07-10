import java.sql.*;
import java.util.ArrayList;

public class Project {

    public static void main(String[] args) throws SQLException {

        Log.S("System Started");

        // All users
        ArrayList<User> users;

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

            // Load Current User data if Logged in.
            CurrentUser.initUserData(connection);
            Log.DB("Current User loaded successfully!");
            Log.S("Current User loaded successfully!");

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


        // Load All Users
        try {
            users = DatabaseIO.getUsers(connection);
            Log.DB("All Users loaded successfully!");
        }
        catch (SQLException e) {
            System.out.println("Database was unable to load! :(");
            Log.E("Users not loaded");
            return;
        }


        // Welcome Screen
        Utility.printWelcome();


        // Open Login Menu if not already logged in
        if (CurrentUser.data == null) {

            Utility.openLoginMenu();

        }

    }

}