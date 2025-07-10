import java.io.*;
import java.sql.*;

public class CurrentUser {

    static User data;

    static void initUserData(Connection connection) throws IOException, SQLException {

        File userData = new File("userData.txt");

        if (!userData.exists())
            return;

        BufferedReader reader = new BufferedReader(
                new FileReader(userData)
        );

        String line = reader.readLine();

        if (line == null)
            return;

        int uid = Integer.parseInt(line);
        reader.close();

        data = DatabaseIO.getUserFromUid(connection, uid);


    }
}