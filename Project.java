import java.sql.*;

public class Project {

    public static void main(String[] args) throws Exception {

        Log.S("System Started");

        // Database Initialization
        String url = "jdbc:postgresql://localhost:5432/Dating_Mania";
        String user = "postgres";
        String password = "";

        // Connect to Database
        Exception exception = null;
        try {

            Class.forName("org.postgresql.Driver");
            DatabaseIO.connection = DriverManager.getConnection(url, user, password);

            Log.S("Connected to PostgreSQL successfully!");

            // Load Current User data if Logged in.
            CurrentUser.initUserData();

        }
        catch (ClassNotFoundException e) {
            exception = e;
            System.out.println("JDBC Driver not found.");
            Log.E("JDBC Driver not found.");
            return;
        }
        catch (SQLException e) {
            exception = e;
            System.out.println("Connection failed.");
            Log.E("Connection failed.");
            return;
        }
        catch (Exception e) {
            exception = e;
            System.out.println("Database was unable to load! :(");
            Log.E("Database was unable to load! :(");
            return;
        }
        finally {
            if (exception != null)
                exception.printStackTrace();
        }


        // Load All Users
        try {
            User.users = DatabaseIO.getUsers();
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
        if (CurrentUser.data == null)
            Utility.openLoginMenu();
        else {
            System.out.println("Welcome back " + CurrentUser.data.name + "!");
            Utility.openMainMenu();
        }

    }

}