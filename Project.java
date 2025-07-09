import java.sql.*;

public class Project {

    public static void main(String[] args) {

        Log.S("System Started");

        // Database Initialization
        String url = "jdbc:postgresql://localhost:5432/Dating";
        String user = "postgres";
        String password = "";

        Connection connection;

        // Connect to PostgreSQL
        try {

            Class.forName("org.postgresql.Driver");
            connection = DriverManager.getConnection(url, user, password);

            Log.S("Connected to PostgreSQL successfully!");

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

        Utility.printWelcome();


        if (CurrentUserData.username == null) {

            Utility.openLoginMenu();

        }

    }

}