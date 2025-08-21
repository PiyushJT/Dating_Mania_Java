package model;

import java.sql.*;

import ds.UserLinkedList;
import util.*;

public class User {

    public static UserLinkedList users;


    // model.User data
    int userId;
    String name;
    String bio;
    String gender;
    int age;
    long phone;
    String email;
    String city;
    boolean isActive;
    long lastActive;
    boolean isDeleted;
    long createdAt;
    long updatedAt;



    // Constructor
    public User(
            int userId, String name, String bio,
            String gender, int age, long phone,
            String email, String city, boolean isActive,
            long lastActive, boolean isDeleted,
            long createdAt, long updatedAt
    ) {
        this.userId = userId;
        this.name = name;
        this.bio = bio;
        this.gender = gender;
        this.age = age;
        this.phone = phone;
        this.email = email;
        this.city = city;
        this.isActive = isActive;
        this.lastActive = lastActive;
        this.isDeleted = isDeleted;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }


    // Pretty print
    public String toString() {
        return String.format("%-3d %-20s %-2c %-3d", userId, name, gender, age);
    }


    // Function to load user from "database result set"
    public static User fromDB(ResultSet rs) throws SQLException {

        int id = rs.getInt("user_id");
        String name = rs.getString("name");
        String bio = rs.getString("bio");
        String gender = rs.getString("gender");
        int age = rs.getInt("age");
        long phone = rs.getLong("phone");
        String email = rs.getString("email");
        String city = rs.getString("city");
        boolean isActive = rs.getBoolean("is_active");
        boolean isDeleted = rs.getBoolean("is_deleted");


        long lastActive = Utility.getDateFromSQLDate(
                rs.getString("last_active")
        );
        long createdAt = Utility.getDateFromSQLDate(
                rs.getString("created_at")
        );
        long updatedAt = Utility.getDateFromSQLDate(
                rs.getString("updated_at")
        );

        return new User(id, name, bio, gender, age, phone, email, city, isActive, lastActive, isDeleted, createdAt, updatedAt);

    }


    // Getters
    public String getName() {
        return name;
    }

    public int getId() {
        return userId;
    }

    public String getBio() {
        return bio;
    }

    public String getGender() {
        return gender;
    }

    public int getAge() {
        return age;
    }

    public long getPhone() {
        return phone;
    }

    public String getEmail() {
        return email;
    }

    public String getCity() {
        return city;
    }

    public static UserLinkedList getUsers() {
        return users;
    }

    public int getUserId() {
        return userId;
    }

    public boolean isActive() {
        return isActive;
    }

    public long getLastActive() {
        return lastActive;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public long getUpdatedAt() {
        return updatedAt;
    }
}