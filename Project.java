import java.sql.*;

import db.DatabaseIO;
import logs.Log;
import model.User;
import session.CurrentUser;
import util.Utility;

public class Project {

    public static void main(String[] args) {

        Log.S("System Started");


        for (int i = 0; i < 13; i++)
            Utility.println(i + ". Color demo", i);

        // Database Initialization
        String url = "jdbc:postgresql://localhost:5432/Dating_Mania";
        String user = "postgres";
        String password = "15manav@";

        // Connect to Database
        try {

            Class.forName("org.postgresql.Driver");
            DatabaseIO.connection = DriverManager.getConnection(url, user, password);

            Log.S("Connected to PostgreSQL successfully!");

            // Load Current model.User data if Logged in.
            CurrentUser.initUserData();

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
            Utility.println("Welcome back " + CurrentUser.data.getName() + ".", 3);
            Utility.openMainMenu();
        }

    }

}