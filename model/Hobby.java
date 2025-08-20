package model;

import java.sql.*;
import java.util.HashMap;

import db.DatabaseIO;

public class Hobby {

    public static HashMap<Integer, String> hobbies;

    static {

        try {
            hobbies = DatabaseIO.getAllHobbies();
        }
        catch (SQLException e) {
            hobbies = new HashMap<>();
        }

    }


    int hobbyId;
    String hobbyName;

    public Hobby(int hobbyId, String hobbyName) {
        this.hobbyId = hobbyId;
        this.hobbyName = hobbyName;
    }

    public int getHobbyId() {
        return hobbyId;
    }

    public String getHobbyName() {
        return hobbyName;
    }


    public static Hobby fromDB(ResultSet rs) throws SQLException {


        int hobbyId = rs.getInt("hobby_id");
        String hobbyName = rs.getString("hobby_name");

        return new Hobby(hobbyId, hobbyName);

    }

}