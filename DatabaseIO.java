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


    // function to get user from auth (email and password)
    static User getUserFromAuth(String email, String password) throws SQLException {

        // query
        String query = """
            SELECT *
            FROM users
            WHERE user_id = (
                SELECT user_id
                FROM auth
                WHERE email = ? AND password = ?
            );
            """;

        // run query
        PreparedStatement pst = connection.prepareStatement(query);
        pst.setString(1, email);
        pst.setString(2, password);

        // result
        ResultSet rs = pst.executeQuery();

        // return the user if exists
        if (!rs.next())
            return null;

        return User.fromDB(rs);

    }


    // function to register a new user
    static void addUserToDB(User user, String password) throws SQLException {

        // get current time
        Timestamp now = new Timestamp(Utility.getNowLong());

        // Insert into users table
        String userInsert = """
                INSERT INTO users (name, bio, gender, age, phone, email, city, is_active, last_active, is_deleted, created_at, updated_at)
                VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);
        """;

        PreparedStatement userStmt = connection.prepareStatement(userInsert);

        userStmt.setString(1, user.name);
        userStmt.setString(2, user.bio);
        userStmt.setString(3, String.valueOf(user.gender));
        userStmt.setInt(4, user.age);
        userStmt.setLong(5, user.phone);
        userStmt.setString(6, user.email);
        userStmt.setString(7, user.city);
        userStmt.setBoolean(8, true); // is_active
        userStmt.setTimestamp(9, now); // last_active
        userStmt.setBoolean(10, false); // is_deleted
        userStmt.setTimestamp(11, now); // created_at
        userStmt.setTimestamp(12, now); // updated_at

        int r = userStmt.executeUpdate();


        // Insert into auth table first to get uid
        String authInsert = """
            INSERT INTO auth VALUES (?, ?, (
                    SELECT user_id FROM users WHERE email = ?
                )
            );
        """;

        PreparedStatement authStmt = connection.prepareStatement(authInsert);

        authStmt.setString(1, user.email);
        authStmt.setString(2, password);
        authStmt.setString(3, user.email);

        r = authStmt.executeUpdate();

    }


    static ArrayList<Hobby> getHobbiesFromUID(int uid) throws SQLException {

        ArrayList<Hobby> hobbies = new ArrayList<>();

        // query
        String query = """
            SELECT
                H.hobby_id, H.hobby_name
            FROM
                user_hobbies UH
                INNER JOIN
                hobbies H
                ON
                UH.hobby_id = H.hobby_id
            WHERE
                UH.user_id = ?;
        """;


        // query
        PreparedStatement pst = connection.prepareStatement(query);
        pst.setInt(1, uid);

        // result
        ResultSet rs = pst.executeQuery();


        // getting users' data from result
        while (rs.next())
            hobbies.add(Hobby.fromDB(rs));

        return hobbies;
    }



    public static void addHobbiesToDB(int[] ind) throws SQLException {


        String deleteQuery = """
            DELETE FROM user_hobbies
            WHERE user_id = ?;
        """;

        PreparedStatement pst = connection.prepareStatement(deleteQuery);
        pst.setInt(1, CurrentUser.data.userId);

        pst.executeUpdate();


        String insertQuery = """
            INSERT INTO user_hobbies (user_id, hobby_id)
            VALUES (?, ?);
        """;


        pst = connection.prepareStatement(insertQuery);
        pst.setInt(1, CurrentUser.data.userId);

        for (int i : ind) {
            pst.setInt(2, i);
            pst.executeUpdate();
        }

        CurrentUser.hobbies = getHobbiesFromUID(CurrentUser.data.userId);

    }

    public static ArrayList<Song> getSongsFromUID(int uid) throws SQLException {


        ArrayList<Song> hobbies = new ArrayList<>();

        // query
        String query = """
            SELECT
            	S.song_id, S.song_name,
            	S.song_url, S.artist_name,
            	T.type_name
            FROM
            	user_song US
                INNER JOIN
                songs S
                ON
                US.song_id = S.song_id
            	INNER JOIN
            	song_types T
            	on
            	T.type_id = S.type_id
            WHERE
            	US.user_id = ?;
        """;


        // query
        PreparedStatement pst = connection.prepareStatement(query);
        pst.setInt(1, uid);

        // result
        ResultSet rs = pst.executeQuery();


        // getting users' data from result
        while (rs.next())
            hobbies.add(Song.fromDB(rs));

        return hobbies;

    }


    public static void addSongsToDB(int[] ind) throws SQLException {

        String deleteQuery = """
                    DELETE FROM user_song
                    WHERE user_id = ?;
                """;

        PreparedStatement pst = connection.prepareStatement(deleteQuery);
        pst.setInt(1, CurrentUser.data.userId);

        pst.executeUpdate();


        String insertQuery = """
                    INSERT INTO user_song (user_id, song_id)
                    VALUES (?, ?);
                """;


        pst = connection.prepareStatement(insertQuery);
        pst.setInt(1, CurrentUser.data.userId);

        for (int i : ind) {
            pst.setInt(2, i);
            pst.executeUpdate();

        }

        CurrentUser.songs = getSongsFromUID(CurrentUser.data.userId);
    }


    static boolean deleteUser(String password) throws SQLException {

        String getPassQuery = """
                    SELECT password
                    FROM auth
                    WHERE user_id = ?;
                """;

        PreparedStatement pst = connection.prepareStatement(getPassQuery);
        pst.setInt(1, CurrentUser.data.userId);

        ResultSet rs = pst.executeQuery();

        if (!rs.next())
            return false;

        String pass = rs.getString("password");

        System.out.println(pass);

        if (!pass.equals(password))
            return false;

        String deleteQuery = """
                    UPDATE users
                    SET is_deleted = true
                    WHERE user_id = ?
                """;

        pst = connection.prepareStatement(deleteQuery);
        pst.setInt(1, CurrentUser.data.userId);

        int r = pst.executeUpdate();

        return r == 1;


    }


    static boolean deactivateUser(String password) throws SQLException {

        String getPassQuery = """
                    SELECT password
                    FROM auth
                    WHERE user_id = ?;
                """;

        PreparedStatement pst = connection.prepareStatement(getPassQuery);
        pst.setInt(1, CurrentUser.data.userId);

        ResultSet rs = pst.executeQuery();

        if (!rs.next())
            return false;

        String pass = rs.getString("password");

        System.out.println(pass);

        if (!pass.equals(password))
            return false;

        String deleteQuery = """
                    UPDATE users
                    SET is_active = false
                    WHERE user_id = ?
                """;

        pst = connection.prepareStatement(deleteQuery);
        pst.setInt(1, CurrentUser.data.userId);

        int r = pst.executeUpdate();

        return r == 1;


    }


    static boolean updateProfile(String toUpdate, String value) throws SQLException {


        String query = """
                UPDATE users
                SET
                """ + toUpdate +  """
                 = ?
                WHERE user_id = ?;
            """;

        PreparedStatement pst = connection.prepareStatement(query);
        pst.setString(1, value);
        pst.setInt(2, CurrentUser.data.userId);

        int r = pst.executeUpdate();

        return r == 1;

    }


    static boolean updateProfile(String toUpdate, int value) throws SQLException {


        String query = """
                UPDATE users
                SET
                """ + toUpdate +  """
                 = ?
                WHERE user_id = ?;
            """;

        PreparedStatement pst = connection.prepareStatement(query);
        pst.setInt(1, value);
        pst.setInt(2, CurrentUser.data.userId);

        int r = pst.executeUpdate();

        return r == 1;

    }


    static boolean updatePassword(String password) throws SQLException {


        String query = """
                UPDATE auth
                SET password = ?
                WHERE user_id = ?;
            """;

        PreparedStatement pst = connection.prepareStatement(query);
        pst.setString(1, password);
        pst.setInt(2, CurrentUser.data.userId);

        int r = pst.executeUpdate();

        return r == 1;

    }





}