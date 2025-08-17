import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;

public class DatabaseIO {

    static Connection connection;

    // function to get all users from database
    static ArrayList<User> getUsers() throws SQLException {

        // base arraylist
        ArrayList<User> users = new ArrayList<>();

        // query
        Statement st = connection.createStatement();
        String query = """
            SELECT
                *
            FROM
                users;
        """;

        // result
        ResultSet rs = st.executeQuery(query);


        // getting users' data from result
        while (rs.next())
            users.add(User.fromDB(rs));

        return users;

    }


    static Timestamp now() {
        return Timestamp.valueOf(LocalDateTime.now());
    }


    // function to get user from uid
    static User getUserFromUid(int user_id) throws SQLException {


        // query
        String query = """
            SELECT
                *
            FROM
                users
            WHERE
                user_id = ?;
        """;

        PreparedStatement pst = connection.prepareStatement(query);
        pst.setInt(1, user_id);

        // result
        ResultSet rs = pst.executeQuery();


        // return the user if exists
        if (!rs.next())
            return null;

        return User.fromDB(rs);

    }


    static void updateLastActive(int uid) throws SQLException{

        String sql = """
            UPDATE
                users
            SET
                last_active = ?
            WHERE
                user_id = ?;

        """;

        PreparedStatement pst = connection.prepareStatement(sql);
        pst.setTimestamp(1, now());
        pst.setInt(2, uid);

        pst.executeUpdate();

    }

    // function to get user from auth (email/Phone and password)
    static User getUserFromAuth(String emailPhone, String password) throws SQLException {

        // query
        String query = """
            SELECT
                *
            FROM
                users
            WHERE
                user_id = (
                    SELECT
                        user_id
                    FROM
                        auth
                    WHERE
                        (
                            email = ?
                            OR
                            phone = ?
                        )
                        AND
                        password = ?
                );
            """;

        // run query
        PreparedStatement pst = connection.prepareStatement(query);
        pst.setString(1, emailPhone);
        pst.setString(2, emailPhone);
        pst.setString(3, password);

        // result
        ResultSet rs = pst.executeQuery();

        // return the user if exists
        if (!rs.next())
            return null;

        return User.fromDB(rs);

    }


    // function to register a new user
    static void addUserToDB(User user, String password) throws SQLException {

        // Insert into users table
        String userInsert = """
            INSERT INTO
                users (name, bio, gender, age, phone, email, city, is_active, last_active, is_deleted, created_at, updated_at)
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
        userStmt.setTimestamp(9, now()); // last_active
        userStmt.setBoolean(10, false); // is_deleted
        userStmt.setTimestamp(11, now()); // created_at
        userStmt.setTimestamp(12, now()); // updated_at

        int r = userStmt.executeUpdate();


        // Insert into auth table first to get uid
        String authInsert = """
            INSERT INTO
                auth
            VALUES (?, ?, (
                    SELECT
                        user_id
                    FROM
                        users
                    WHERE
                    email = ?
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
            DELETE FROM
                user_hobbies
            WHERE
                user_id = ?;
        """;

        PreparedStatement pst = connection.prepareStatement(deleteQuery);
        pst.setInt(1, CurrentUser.data.userId);

        pst.executeUpdate();


        String insertQuery = """
            INSERT INTO
                user_hobbies (user_id, hobby_id)
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
            DELETE FROM
                user_song
            WHERE
                user_id = ?;
        """;

        PreparedStatement pst = connection.prepareStatement(deleteQuery);
        pst.setInt(1, CurrentUser.data.userId);

        pst.executeUpdate();


        String insertQuery = """
            INSERT INTO
                user_song (user_id, song_id)
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
            SELECT
                password
            FROM
                auth
            WHERE
                user_id = ?
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
            UPDATE
                users
            SET
                is_deleted = true
            WHERE
                user_id = ?
        """;

        pst = connection.prepareStatement(deleteQuery);
        pst.setInt(1, CurrentUser.data.userId);
        pst.executeUpdate();



        deleteQuery = """
            DELETE FROM
                user_hobbies
            WHERE
                user_id = ?
        """;

        pst = connection.prepareStatement(deleteQuery);
        pst.setInt(1, CurrentUser.data.userId);
        pst.executeUpdate();



        deleteQuery = """
            DELETE FROM
                user_song
            WHERE
                user_id = ?
        """;

        pst = connection.prepareStatement(deleteQuery);
        pst.setInt(1, CurrentUser.data.userId);
        pst.executeUpdate();

        return true;


    }


    static boolean deactivateUser(String password) throws SQLException {

        String getPassQuery = """
            SELECT
                password
            FROM
                auth
            WHERE
                user_id = ?;
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
            UPDATE
                users
            SET
                is_active = false
            WHERE
                user_id = ?
        """;

        pst = connection.prepareStatement(deleteQuery);
        pst.setInt(1, CurrentUser.data.userId);

        int r = pst.executeUpdate();

        return r == 1;


    }


    public static boolean amIBlockedBy(int senderUid) throws SQLException{

        String query = """
                SELECT
                    1
                FROM
                    block
                WHERE
                    user_id = ?
                    AND
                    blocked_user_id = ?
                    AND
                    is_deleted = false;
        """;

        PreparedStatement pst = connection.prepareStatement(query);
        pst.setInt(1, senderUid);
        pst.setInt(2, CurrentUser.data.getId());

        ResultSet rs = pst.executeQuery();

        return rs.next();

    }

    static boolean updateName(String value) throws SQLException {


        String query = """
            UPDATE
                users
            SET
                name = ?,
                updated_at = ?
            WHERE
                user_id = ?;
        """;

        PreparedStatement pst = connection.prepareStatement(query);
        pst.setString(1, value);
        pst.setTimestamp(2, now());
        pst.setInt(3, CurrentUser.data.userId);

        int r = pst.executeUpdate();

        return r == 1;

    }

    static boolean updateBio(String value) throws SQLException {


        String query = """
            UPDATE
                users
            SET
                bio = ?,
                updated_at = ?
            WHERE
                user_id = ?;
        """;

        PreparedStatement pst = connection.prepareStatement(query);
        pst.setString(1, value);
        pst.setTimestamp(2, now());
        pst.setInt(3, CurrentUser.data.userId);

        int r = pst.executeUpdate();

        return r == 1;

    }

    static boolean updateGender(String value) throws SQLException {


        String query = """
            UPDATE
                users
            SET
                gender = ?,
                updated_at = ?
            WHERE
                user_id = ?;
        """;

        PreparedStatement pst = connection.prepareStatement(query);
        pst.setString(1, value);
        pst.setTimestamp(2, now());
        pst.setInt(3, CurrentUser.data.userId);

        int r = pst.executeUpdate();

        return r == 1;

    }

    static boolean updatePhone(String value) throws SQLException {


        String query = """
            UPDATE
                users
            SET
                phone = ?,
                updated_at = ?
            WHERE
                user_id = ?;
        """;

        PreparedStatement pst = connection.prepareStatement(query);
        pst.setString(1, value);
        pst.setTimestamp(2, now());
        pst.setInt(3, CurrentUser.data.userId);

        int r = pst.executeUpdate();

        return r == 1;

    }

    static boolean updateCity(String value) throws SQLException {


        String query = """
            UPDATE
                users
            SET
                city = ?,
                updated_at = ?
            WHERE
                user_id = ?;
        """;

        PreparedStatement pst = connection.prepareStatement(query);
        pst.setString(1, value);
        pst.setTimestamp(2, now());
        pst.setInt(3, CurrentUser.data.userId);

        int r = pst.executeUpdate();

        return r == 1;

    }

    static boolean updateEmail(String value) throws SQLException {


        String query = """
            UPDATE
                users
            SET
                email = ?,
                updated_at = ?
            WHERE
                user_id = ?;
        """;

        PreparedStatement pst = connection.prepareStatement(query);
        pst.setString(1, value);
        pst.setTimestamp(2, now());
        pst.setInt(3, CurrentUser.data.userId);

        int r = pst.executeUpdate();

        return r == 1;

    }



    static boolean updatePassword(String password) throws SQLException {


        String query = """
            UPDATE
                auth
            SET
                password = ?,
                updated_at = ?
            WHERE
                user_id = ?;
        """;

        PreparedStatement pst = connection.prepareStatement(query);
        pst.setString(1, password);
        pst.setTimestamp(2, now());
        pst.setInt(3, CurrentUser.data.userId);

        int r = pst.executeUpdate();

        return r == 1;

    }


    static boolean updateAge(int value) throws SQLException {


        String query = """
            UPDATE
                users
            SET
                age = ?,
                updated_at = ?
            WHERE
                user_id = ?;
        """;

        PreparedStatement pst = connection.prepareStatement(query);
        pst.setInt(1, value);
        pst.setTimestamp(2, now());
        pst.setInt(3, CurrentUser.data.userId);

        int r = pst.executeUpdate();

        return r == 1;

    }


    static ArrayList<Match> getMatchesByUid(int uid) throws SQLException {


        ArrayList<Match> matches = new ArrayList<>();

        // query
        String query = """
            SELECT *
            FROM matches
            WHERE
                receiver_user_id = ?
                AND
                is_accepted is null
                AND
                accepted_at is null
                AND
                is_deleted = false;
        """;

        // run query
        PreparedStatement pst = connection.prepareStatement(query);
        pst.setInt(1, uid);

        // result
        ResultSet rs = pst.executeQuery();

        while (rs.next()) {
            matches.add(Match.fromDB(rs));
        }

        return matches;

    }


    public static ArrayList<User> getMatches() throws SQLException {

        ArrayList<User> list = new ArrayList<>();


        String sentQuery = """
            SELECT
                U.*
            FROM
                matches M
                INNER JOIN
                users U
                ON
                M.receiver_user_id = U.user_id
            WHERE
                sender_user_id = ?
                AND
                is_accepted = true
                AND
                M.is_deleted = false
            ORDER BY
                accepted_at DESC;
    """;

        PreparedStatement pst = connection.prepareStatement(sentQuery);
        pst.setInt(1, CurrentUser.data.getId());
        ResultSet rs = pst.executeQuery();

        while (rs.next())
            list.add(User.fromDB(rs));


        String receivedQuery = """
            SELECT
                U.*
            FROM
                matches M
                INNER JOIN
                users U
                ON
                M.sender_user_id = U.user_id
            WHERE
                receiver_user_id = ?
                AND
                is_accepted = true
                AND
                M.is_deleted = false
            ORDER BY
                accepted_at DESC;
    """;


        pst = connection.prepareStatement(receivedQuery);
        pst.setInt(1, CurrentUser.data.getId());
        rs = pst.executeQuery();

        while (rs.next())
            list.add(User.fromDB(rs));


        return list;

    }


    static boolean isAccountActive(String emailPhone) {

        boolean isActive = false;

        try {
            // Assuming DatabaseIO.connection is your open JDBC Connection

            String sql = """
                SELECT
                    is_active
                FROM
                    users
                WHERE
                    email = ?
                    OR
                    phone = ?;
            """;

            PreparedStatement pst = DatabaseIO.connection.prepareStatement(sql);
            pst.setString(1, emailPhone);
            pst.setString(2, emailPhone);

            ResultSet rs = pst.executeQuery();

            if (rs.next()) {

                isActive = rs.getBoolean("is_active");

            }

            rs.close();
            pst.close();

        }
        catch (Exception e) {
            Log.E("Error checking account active status: " + e.getMessage());
        }

        return isActive;

    }


    static void reActivate(String emailPhone) {

        try {

            String sql = """
                    UPDATE
                        users
                    SET
                        is_active = true
                    WHERE
                        email = ?
                        OR
                        phone = ?;
            """;


            PreparedStatement pst = DatabaseIO.connection.prepareStatement(sql);
            pst.setString(1, emailPhone);
            pst.setString(2, emailPhone);

            pst.executeUpdate();

        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
        }

    }

    static void sendMatchRequest(int receiverId, String by) throws SQLException {
        String sql = """
                INSERT INTO
                    matches
                VALUES
                    (?, ?, null, null, NOW(), ?, false)
        """;
        PreparedStatement stmt = connection.prepareStatement(sql);
        stmt.setInt(1, CurrentUser.data.getId());
        stmt.setInt(2, receiverId);
        stmt.setString(3, by);

        int rows = stmt.executeUpdate();
        if (rows > 0) {
            System.out.println("Request sent successfully from " + CurrentUser.data.getId() + " to " + receiverId);
        }
        else {
            System.out.println("Request failed to send.");
        }

    }

    static void acceptMatch(Match match) throws SQLException {
        String update = """
            UPDATE
                matches
            SET
                is_accepted = true,
                accepted_at = ?
            WHERE
                sender_user_id = ?
                AND
                receiver_user_id = ?;
        """;
        PreparedStatement pst = connection.prepareStatement(update);
        pst.setTimestamp(1, now());
        pst.setInt(2, match.senderUserId);
        pst.setInt(3, match.receiverUserId);
        pst.executeUpdate();

    }

    static void rejectMatch(Match match) throws SQLException {
        String update = """
            UPDATE
                matches
            SET
                is_accepted = false
            WHERE
                sender_user_id = ?
                AND
                receiver_user_id = ?;
        """;
        PreparedStatement pst = connection.prepareStatement(update);
        pst.setInt(1, match.senderUserId);
        pst.setInt(2, match.receiverUserId);
        pst.executeUpdate();

    }



    static ArrayList<User> getBlockedUsers(int uid) throws SQLException{

        ArrayList<User> blockedUsers = new ArrayList<>();

        // query
        String query = """
            SELECT
                *
            FROM
                users
            WHERE
                user_id IN (
                    SELECT
                        blocked_user_id
                    FROM
                        block
                    WHERE
                        user_id = ?
                        AND
                        is_deleted = false
                );
        """;

        // run query
        PreparedStatement pst = connection.prepareStatement(query);
        pst.setInt(1, uid);

        // result
        ResultSet rs = pst.executeQuery();

        while (rs.next()) {
            blockedUsers.add(User.fromDB(rs));
        }

        return blockedUsers;

    }


    static boolean unblockUser(int uid) throws SQLException {

        String update = """
            UPDATE
                block
            SET
                is_deleted = true
            WHERE
                user_id = ?
                AND
                blocked_user_id = ?;
        """;

        PreparedStatement pst = connection.prepareStatement(update);
        pst.setInt(1, CurrentUser.data.userId);
        pst.setInt(2, uid);
        int r = pst.executeUpdate();

        return r == 1;
    }


    static boolean isEmailPhoneDupe(String emailPhone) throws SQLException{

        String existsQuery = """
            SELECT
                1
            FROM
                users
            WHERE
                email = ?
                OR
                phone = ?
        """;

        PreparedStatement pst1 = connection.prepareStatement(existsQuery);
        pst1.setString(1, emailPhone);
        pst1.setString(2, emailPhone);

        ResultSet rs = pst1.executeQuery();

        if (rs.next())
            return true;

        return false;

    }


    public static boolean blockUser(int uid) throws SQLException {

        String existsQuery = """
            SELECT
                *
            FROM
                block
            WHERE
                user_id = ?
                AND
                blocked_user_id = ?;
        """;

        PreparedStatement pst1 = connection.prepareStatement(existsQuery);
        pst1.setInt(1, CurrentUser.data.userId);
        pst1.setInt(2, uid);
        ResultSet rs = pst1.executeQuery();

        if (rs.next()) {

            String updateQuery = """
                UPDATE
                    block
                SET
                    is_deleted = false
                WHERE
                    user_id = ?
                    AND
                    blocked_user_id = ?;
            """;

            PreparedStatement pst2 = connection.prepareStatement(updateQuery);
            pst2.setInt(1, CurrentUser.data.userId);
            pst2.setInt(2, uid);

            int r = pst2.executeUpdate();

            return r == 1;

        }

        String insertQuery = """
            INSERT INTO
                block
            VALUES
                (?, ?, ?, ?);
        """;

        PreparedStatement pst = connection.prepareStatement(insertQuery);
        pst.setInt(1, CurrentUser.data.userId);
        pst.setInt(2, uid);
        pst.setTimestamp(3, now());
        pst.setBoolean(4, false);
        int r = pst.executeUpdate();

        return r == 1;

    }


    static ArrayList<Song> getAllSongs() throws SQLException {

        ArrayList<Song> list = new ArrayList<>();

        String query = """
                        SELECT
                            *
                        FROM
                            songs
                """;


        PreparedStatement pst1 = connection.prepareStatement(query);
        ResultSet rs = pst1.executeQuery();

        while (rs.next())
            list.add(Song.fromDB(rs));


        return list;

    }


    static HashMap<Integer, String> getAllHobbies() throws SQLException {


        HashMap<Integer, String> map = new HashMap<>();

        String query = """
                        SELECT
                            *
                        FROM
                            hobbies
                """;


        PreparedStatement pst1 = connection.prepareStatement(query);
        ResultSet rs = pst1.executeQuery();

        while (rs.next()) {

            Hobby hobby = Hobby.fromDB(rs);
            map.put(hobby.hobbyId, hobby.hobbyName);

        }

        return map;

    }



}