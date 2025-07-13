import java.io.*;
import java.sql.*;

public class CurrentUser {

    // current user data
    static User data;


    // function to initialize current user data from file
    static void initUserData() throws IOException, SQLException {

        // data file
        File userData = new File("userData.txt");


        // if file doesn't exist, not logged in. and return (no need to load data)
        if (!userData.exists())
            return;

        // read data from file
        BufferedReader reader = new BufferedReader(
                new FileReader(userData)
        );

        String line = reader.readLine();


        // if file is empty, not logged in. and return (no need to load data)
        if (line == null)
            return;

        int user_id;

        try {
            user_id = Integer.parseInt(line);
            reader.close();

            data = DatabaseIO.getUserFromUid(user_id);
        }
        catch (NumberFormatException e) {
            Log.E("Current User file is corrupted.");
        }

    }

}