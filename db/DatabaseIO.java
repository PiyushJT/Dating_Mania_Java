package db;

import java.sql.*;
import java.util.HashMap;

import ds.SongLinkedList;
import logs.Log;
import model.Hobby;
import model.Match;
import model.Song;
import model.User;
import session.CurrentUser;
import ds.*;
import util.Utility;

public class DatabaseIO {

    public static Connection connection;

    // function to get all users from database
    public static UserLinkedList getUsers() throws SQLException {

        // base list
        UserLinkedList users = new UserLinkedList();

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
            users.insert(User.fromDB(rs));

        return users;

    }


    // function to get user from uid
    public static User getUserFromUid(int user_id) throws SQLException {


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


    public static void updateLastActive(int uid) throws SQLException{

        String sql = """
            UPDATE
                users
            SET
                last_active = NOW()
            WHERE
                user_id = ?;

        """;

        PreparedStatement pst = connection.prepareStatement(sql);
        pst.setInt(1, uid);

        pst.executeUpdate();

    }

    // function to get user from auth (email/Phone and password)
    public static User getUserFromAuth(String emailPhone, String password) throws SQLException {

        // query
        String query = """
            SELECT
                U.*
            FROM
                users U
                INNER JOIN
                auth A
                ON
                U.user_id = A.user_id
            WHERE
                (
                    U.email = ?
                    or
                    U.phone = ?
                )
                AND
                A.password = ?
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

    public static int getActiveUserCount() throws SQLException {
        String sql = "SELECT COUNT(*) FROM users WHERE is_active = true AND is_deleted = false";
        PreparedStatement pst = connection.prepareStatement(sql);
        ResultSet rs = pst.executeQuery();
        if (rs.next()) {
            return rs.getInt(1);
        }
        return 0;
    }

    public static boolean addNewIntoHobby(String hobbyName) throws SQLException {
        String sql = "INSERT INTO hobbies (hobby_name) VALUES (?)";
        PreparedStatement pst = connection.prepareStatement(sql);
        pst.setString(1, hobbyName);
        int rows = pst.executeUpdate();
        return rows > 0;
    }

    public static boolean addNewIntoSong(String name, String url, String artist, int typeId) throws SQLException {
        String sql = "INSERT INTO songs (song_name, song_url, artist_name, type_id) VALUES (?, ?, ?, ?)";
        PreparedStatement pst = connection.prepareStatement(sql);
        pst.setString(1, name);
        pst.setString(2, url);
        pst.setString(3, artist);
        pst.setInt(4, typeId);
        int rows = pst.executeUpdate();
        return rows > 0;
    }



    // function to register a new user
    public static void addUserToDB(User user, String password) throws SQLException {

        // Insert into users table
        String userInsert = """
            INSERT INTO
                users (name, bio, gender, age, phone, email, city, is_active, last_active, is_deleted, created_at, updated_at)
            VALUES(?, ?, ?, ?, ?, ?, ?, ?, NOW(), ?, NOW(), NOW());
        """;

        PreparedStatement userStmt = connection.prepareStatement(userInsert);

        userStmt.setString(1, user.getName());
        userStmt.setString(2, user.getBio());
        userStmt.setString(3, String.valueOf(user.getGender()));
        userStmt.setInt(4, user.getAge());
        userStmt.setLong(5, user.getPhone());
        userStmt.setString(6, user.getEmail());
        userStmt.setString(7, user.getCity());
        userStmt.setBoolean(8, true); // is_active
        userStmt.setBoolean(9, false); // is_deleted

        userStmt.executeUpdate();


        // Insert into auth table first to get uid
        String authInsert = """
            INSERT INTO
                auth
            VALUES (?, (
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

        authStmt.setString(1, password);
        authStmt.setString(2, user.getEmail());

        authStmt.executeUpdate();

    }


    public static HobbyLinkedList getHobbiesFromUID(int uid) throws SQLException {

        HobbyLinkedList hobbies = new HobbyLinkedList();

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
        while (rs.next()) {
            hobbies.insert(Hobby.fromDB(rs));
        }

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
        pst.setInt(1, CurrentUser.data.getUserId());

        pst.executeUpdate();


        String insertQuery = """
            INSERT INTO
                user_hobbies (user_id, hobby_id)
            VALUES (?, ?);
        """;


        pst = connection.prepareStatement(insertQuery);
        pst.setInt(1, CurrentUser.data.getUserId());

        for (int i : ind) {
            pst.setInt(2, i);
            pst.executeUpdate();
        }

        CurrentUser.hobbies = getHobbiesFromUID(CurrentUser.data.getUserId());

    }

    public static SongLinkedList getSongsFromUID(int uid) throws SQLException {


        SongLinkedList songs = new SongLinkedList();

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
            songs.insert(Song.fromDB(rs));

        return songs;

    }


    public static void addSongsToDB(int[] ind) throws SQLException {

        String deleteQuery = """
            DELETE FROM
                user_song
            WHERE
                user_id = ?;
        """;

        PreparedStatement pst = connection.prepareStatement(deleteQuery);
        pst.setInt(1, CurrentUser.data.getUserId());

        pst.executeUpdate();


        String insertQuery = """
            INSERT INTO
                user_song (user_id, song_id)
            VALUES (?, ?);
        """;


        pst = connection.prepareStatement(insertQuery);
        pst.setInt(1, CurrentUser.data.getUserId());

        for (int i : ind) {
            pst.setInt(2, i);
            pst.executeUpdate();

        }

        CurrentUser.songs = getSongsFromUID(CurrentUser.data.getUserId());
    }

    public static void clearSongsForUser(int userId) throws SQLException {
        String sql = "DELETE FROM user_song WHERE user_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            stmt.executeUpdate();
        }
    }

    public static void addSongToUser(int userId, int songId) throws SQLException {
        String sql = "INSERT INTO user_song (user_id, song_id) VALUES (?, ?) ON CONFLICT DO NOTHING";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            stmt.setInt(2, songId);
            stmt.executeUpdate();
        }
    }




    public static boolean deleteUser(String password) throws SQLException {

        String getPassQuery = """
            SELECT
                password
            FROM
                auth
            WHERE
                user_id = ?
        """;

        PreparedStatement pst = connection.prepareStatement(getPassQuery);
        pst.setInt(1, CurrentUser.data.getUserId());

        ResultSet rs = pst.executeQuery();

        if (!rs.next())
            return false;

        String pass = rs.getString("password");

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
        pst.setInt(1, CurrentUser.data.getUserId());
        pst.executeUpdate();



        deleteQuery = """
            DELETE FROM
                user_hobbies
            WHERE
                user_id = ?
        """;

        pst = connection.prepareStatement(deleteQuery);
        pst.setInt(1, CurrentUser.data.getUserId());
        pst.executeUpdate();



        deleteQuery = """
            DELETE FROM
                user_song
            WHERE
                user_id = ?
        """;

        pst = connection.prepareStatement(deleteQuery);
        pst.setInt(1, CurrentUser.data.getUserId());
        pst.executeUpdate();

        return true;


    }


    public static boolean deactivateUser(String password) throws SQLException {

        String getPassQuery = """
            SELECT
                password
            FROM
                auth
            WHERE
                user_id = ?;
        """;

        PreparedStatement pst = connection.prepareStatement(getPassQuery);
        pst.setInt(1, CurrentUser.data.getUserId());

        ResultSet rs = pst.executeQuery();

        if (!rs.next())
            return false;

        String pass = rs.getString("password");

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
        pst.setInt(1, CurrentUser.data.getUserId());

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

    public static boolean updateName(String value) throws SQLException {


        String query = """
            UPDATE
                users
            SET
                name = ?,
                updated_at = NOW()
            WHERE
                user_id = ?;
        """;

        PreparedStatement pst = connection.prepareStatement(query);
        pst.setString(1, value);
        pst.setInt(2, CurrentUser.data.getUserId());

        int r = pst.executeUpdate();

        return r == 1;

    }

    public static boolean updateBio(String value) throws SQLException {


        String query = """
            UPDATE
                users
            SET
                bio = ?,
                updated_at = NOW()
            WHERE
                user_id = ?;
        """;

        PreparedStatement pst = connection.prepareStatement(query);
        pst.setString(1, value);
        pst.setInt(2, CurrentUser.data.getUserId());

        int r = pst.executeUpdate();

        return r == 1;

    }

    public static boolean updateGender(String value) throws SQLException {


        String query = """
            UPDATE
                users
            SET
                gender = ?,
                updated_at = NOW()
            WHERE
                user_id = ?;
        """;

        PreparedStatement pst = connection.prepareStatement(query);
        pst.setString(1, value);
        pst.setInt(2, CurrentUser.data.getUserId());

        int r = pst.executeUpdate();

        return r == 1;

    }

    public static boolean updatePhone(String value) throws SQLException {


        String query = """
            UPDATE
                users
            SET
                phone = ?,
                updated_at = NOW()
            WHERE
                user_id = ?;
        """;

        PreparedStatement pst = connection.prepareStatement(query);
        pst.setString(1, value);
        pst.setInt(2, CurrentUser.data.getUserId());

        int r = pst.executeUpdate();

        return r == 1;

    }

    public static boolean updateCity(String value) throws SQLException {


        String query = """
            UPDATE
                users
            SET
                city = ?,
                updated_at = NOW()
            WHERE
                user_id = ?;
        """;

        PreparedStatement pst = connection.prepareStatement(query);
        pst.setString(1, value);
        pst.setInt(2, CurrentUser.data.getUserId());

        int r = pst.executeUpdate();

        return r == 1;

    }

    public static boolean updateEmail(String value) throws SQLException {


        String query = """
            UPDATE
                users
            SET
                email = ?,
                updated_at = NOW()
            WHERE
                user_id = ?;
        """;

        PreparedStatement pst = connection.prepareStatement(query);
        pst.setString(1, value);
        pst.setInt(2, CurrentUser.data.getUserId());

        int r = pst.executeUpdate();

        return r == 1;

    }



    public static boolean updatePassword(String password) throws SQLException {


        String query = """
            UPDATE
                auth
            SET
                password = ?
            WHERE
                user_id = ?
        """;

        PreparedStatement pst = connection.prepareStatement(query);
        pst.setString(1, password);
        pst.setInt(2, CurrentUser.data.getUserId());

        int r = pst.executeUpdate();

        return r == 1;

    }


    public static boolean updateAge(int value) throws SQLException {


        String query = """
            UPDATE
                users
            SET
                age = ?,
                updated_at = NOW()
            WHERE
                user_id = ?;
        """;

        PreparedStatement pst = connection.prepareStatement(query);
        pst.setInt(1, value);
        pst.setInt(2, CurrentUser.data.getUserId());

        int r = pst.executeUpdate();

        return r == 1;

    }

    // check is_deleted
    public static boolean hasExistingMatch(int Sender_uid, int Receiver_uid) throws SQLException {
        String sql = """
                    SELECT
                        1
                    FROM
                        matches
                    WHERE
                        (Sender_user_id = ? AND Receiver_user_id = ?)
                        OR
                        (Sender_user_id = ? AND Receiver_user_id = ?)
                        AND
                        is_deleted = false
                """;
        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setInt(1, Sender_uid);
        ps.setInt(2, Receiver_uid);
        ps.setInt(3, Receiver_uid);
        ps.setInt(4, Sender_uid);
        ResultSet rs = ps.executeQuery();

        return rs.next();
    }


    public static MatchLinkedList getMatchesByUid(int uid) throws SQLException {


        MatchLinkedList matches = new MatchLinkedList();

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
            matches.insert(Match.fromDB(rs));
        }

        return matches;

    }


    public static UserLinkedList getMatchedUsers() throws SQLException {

        UserLinkedList list = new UserLinkedList();


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
            list.insert(User.fromDB(rs));


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
            list.insert(User.fromDB(rs));


        return list;

    }


    public static boolean isAccountActive(String emailPhone) {

        boolean isActive = false;

        try {
            // Assuming db.DatabaseIO.connection is your open JDBC Connection

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


    public static void reActivate(String emailPhone) {

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

    public static void sendMatchRequest(int receiverId, String by) throws SQLException {
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
            Utility.println("Request sent successfully from " + CurrentUser.data.getId() + " to " + receiverId, 0);
        }
        else {
            Utility.println("Request failed to send.", 7);
        }

    }

    public static void acceptMatch(Match match) throws SQLException {
        String update = """
            UPDATE
                matches
            SET
                is_accepted = true,
                accepted_at = NOW()
            WHERE
                sender_user_id = ?
                AND
                receiver_user_id = ?;
        """;
        PreparedStatement pst = connection.prepareStatement(update);
        pst.setInt(1, match.getSenderUserId());
        pst.setInt(2, match.getReceiverUserId());
        pst.executeUpdate();

    }

    public static void rejectMatch(Match match) throws SQLException {
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
        pst.setInt(1, match.getSenderUserId());
        pst.setInt(2, match.getReceiverUserId());
        pst.executeUpdate();

    }

    public static boolean unmatchUser(int otherUserId) throws SQLException {
        String sql = """
        UPDATE matches
        SET is_deleted = true
        WHERE (
                (sender_user_id = ? AND receiver_user_id = ?)
             OR (sender_user_id = ? AND receiver_user_id = ?)
              )
          AND is_deleted = false;
    """;
        PreparedStatement pst = connection.prepareStatement(sql);
        int myId = CurrentUser.data.getId();
        pst.setInt(1, myId);
        pst.setInt(2, otherUserId);
        pst.setInt(3, otherUserId);
        pst.setInt(4, myId);
        int rows = pst.executeUpdate();
        return rows > 0;
    }




    public static UserLinkedList getBlockedUsers(int uid) throws SQLException{

        UserLinkedList blockedUsers = new UserLinkedList();

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
            blockedUsers.insert(User.fromDB(rs));
        }

        return blockedUsers;

    }


    public static boolean unblockUser(int uid) throws SQLException {

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
        pst.setInt(1, CurrentUser.data.getUserId());
        pst.setInt(2, uid);
        int r = pst.executeUpdate();

        return r == 1;
    }


    public static boolean isEmailPhoneDupe(String emailPhone) throws SQLException{

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
        pst1.setInt(1, CurrentUser.data.getUserId());
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
            pst2.setInt(1, CurrentUser.data.getUserId());
            pst2.setInt(2, uid);

            int r = pst2.executeUpdate();

            return r == 1;

        }

        String insertQuery = """
            INSERT INTO
                block
            VALUES
                (?, ?, NOW(), ?);
        """;

        PreparedStatement pst = connection.prepareStatement(insertQuery);
        pst.setInt(1, CurrentUser.data.getUserId());
        pst.setInt(2, uid);
        pst.setBoolean(3, false);
        int r = pst.executeUpdate();

        return r == 1;

    }


    public static SongLinkedList getAllSongs() throws SQLException {

        SongLinkedList list = new SongLinkedList();

        String query = """
                        SELECT
                            *
                        FROM
                            songs S
                            INNER JOIN
                            song_types ST
                            ON
                            S.type_id = ST.type_id
                        ORDER BY
                            S.song_id
                """;


        PreparedStatement pst1 = connection.prepareStatement(query);
        ResultSet rs = pst1.executeQuery();

        while (rs.next())
            list.insert(Song.fromDB(rs));


        return list;

    }


    public static HashMap<Integer, String> getAllHobbies() throws SQLException {


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
            map.put(hobby.getHobbyId(), hobby.getHobbyName());

        }

        return map;

    }



}