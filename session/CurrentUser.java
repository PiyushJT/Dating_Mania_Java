package session;

import java.io.*;
import java.sql.*;
import java.util.*;

import ds.HobbyLinkedList;
import ds.SongLinkedList;
import model.*;
import db.DatabaseIO;
import logs.Log;
import util.Utility;

public class CurrentUser {

    // current user data
    public static User data;

    public static HobbyLinkedList hobbies = new HobbyLinkedList();
    public static SongLinkedList songs = new SongLinkedList();

    static Scanner scanner = new Scanner(System.in);


    // function to initialize current user data from file
    public static void initUserData() throws IOException, SQLException {

        // data file
        File userData = new File("session/user_data.txt");


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

        // parse uid from file and get model.User data from it
        try {
            user_id = Integer.parseInt(line);
            reader.close();

            data = DatabaseIO.getUserFromUid(user_id);
            Log.S("Current User loaded successfully!");
        }
        catch (NumberFormatException e) {
            data = null;
            Log.E("Current User file is corrupted.");
        }

        if (data != null) {

            try {
                hobbies = DatabaseIO.getHobbiesFromUID(
                                data.getId()
                        );
            }
            catch (SQLException e) {
                hobbies = new HobbyLinkedList();
                Log.E("Current User hobbies could not be loaded.");
            }

            try {
                songs = DatabaseIO.getSongsFromUID(
                                data.getId()
                        );
            }
            catch (SQLException e) {
                songs = new SongLinkedList();
                Log.E("Current User songs could not be loaded.");
            }

        }

    }


    // function to add current user data to file
    public static void addCurrentUserToFile() {

        try {
            BufferedWriter writer = new BufferedWriter(
                    new FileWriter("session/user_data.txt")
            );

            writer.write(data.getId() + "");
            writer.close();

            Log.S("Current User's user_id saved to file!");

        }
        catch (IOException e) {
            Log.E("Login was unable to store in file.");
        }

    }


    public static void removeCurrentUserFromFile() {
        try {
            BufferedWriter writer = new BufferedWriter(
                    new FileWriter("session/user_data.txt")
            );

            writer.write("");
            writer.close();
        }
        catch (Exception e) {
            Log.E("Login was unable ot delete.");
        }
    }

    public static void updateHobbies() {

        Utility.printLines(2);
        Utility.println("YOUR HOBBIES", 6);

        Utility.printLines(1);
        if (CurrentUser.hobbies.isEmpty())
            Utility.println("You have no hobbies.", 6);
        else {

            Utility.println("Your hobbies are:", 6);

            for (Hobby hobby : CurrentUser.hobbies.toArray())
                Utility.println("- " + hobby.getHobbyName(), 6);

        }
        Utility.printLines(1);


        Utility.println("Enter 1. -> Edit hobbies", 5);
        Utility.println("Any other. -> Go back", 5);


        Utility.print("Enter your choice: ", 4);
        char choice1 = scanner.next().charAt(0);
        scanner.nextLine();


        if (choice1 == '1') {

            Utility.printLines(2);
            for (int i = 1; i <= Hobby.hobbies.size(); i++)
                Utility.println(i + ". " + Hobby.hobbies.get(i), 6);


            int[] ind;

            outer:
            while (true) {

                Utility.printLines(1);
                Utility.println("Enter comma-separated indices of hobbies to add to your new list.", 4);
                Utility.println("Example: 1,2,3", 4);

                String input = scanner.nextLine().replace(" ", "");
                String[] parts = input.split(",");

                ind = new int[parts.length];

                for (int i = 0; i < parts.length; i++) {
                    try {
                        ind[i] = Integer.parseInt(parts[i].trim());

                        if (ind[i] > 20 || ind[i] < 1) {
                            Utility.println("Invalid index. Try again.", 7);
                            continue outer; // <-- This skips current outer loop iteration
                        }

                    } catch (NumberFormatException e) {
                        Utility.println("Invalid input. Try again.", 7);
                        continue outer; // <-- This also skips to next outer loop iteration
                    }
                }

                break; // all inputs were valid, exit the outer loop
            }


            try {
                DatabaseIO.addHobbiesToDB(ind);
            }
            catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }

    }


    public static void takeSongQuiz() {

        SongLinkedList allSongs = Song.songs;

        Utility.printLines(2);
        Utility.println("YOUR SONG INTERESTS", 6);

        Utility.printLines(1);
        if (CurrentUser.songs.isEmpty())
            Utility.println("You have no song Interests.", 6);

        else {

            Utility.println("Your song interests are:", 6);

            for (Song song : CurrentUser.songs.toArray())
                System.out.println(song);

        }

        Utility.printLines(1);
        Utility.println("Enter 1. -> Take the Song quiz", 5);
        Utility.println("Any other. -> Go back", 5);


        Utility.print("Enter your choice: ", 4);
        char choice2 = scanner.next().charAt(0);
        scanner.nextLine();


        if (choice2 == '1') {

            // Shuffle songs in SongLinkedList and take first 10
            SongLinkedList shuffledSongs = new SongLinkedList();
            for (int i = 0; i < allSongs.length(); i++) {
                shuffledSongs.insert(allSongs.get(i));
            }

            // Use your existing shuffleSongs method
            shuffledSongs.shuffleSongs();

            // Limit to 10 songs max
            int quizSize = Math.min(10, shuffledSongs.length());

            SongLinkedList likedSongs = new SongLinkedList();

            Utility.printLines(2);
            Utility.println("Swipe right (r) to like 👍, left (l) to skip 👎, or 'q' to quit 🚪 the quiz.", 6);

            // Iterate over shuffledSongs using index-based access

            for (int i = 0; i < quizSize; i++) {
                Song song = shuffledSongs.get(i);

                Utility.printLines(1);
                Utility.println("🎧 Now playing: ", 6);
                Utility.println(song.toString(), 6);

                boolean validInput = false;
                while (!validInput) {

                    Utility.printLines(1);
                    Utility.print("Swipe choice (r/l/q): ", 4);
                    String choice = scanner.nextLine().trim().toLowerCase();

                    switch (choice) {
                        case "r":
                            likedSongs.insert(song);
                            Utility.println("💖 Added to your liked songs!", 6);
                            validInput = true;
                            break;
                        case "l":
                            Utility.println("⛔ Skipped.", 6);
                            validInput = true;
                            break;
                        case "q":
                            Utility.println("🚪 Exiting quiz early.", 6);
                            i = quizSize; // exit loop
                            validInput = true;
                            break;
                        default:
                            Utility.println("❓ Invalid input! Please enter 'r', 'l' or 'q'.", 6);
                            break;
                    }
                }
            }

            // Save liked songs to DB and update CurrentUser.songs
            try {
                DatabaseIO.clearSongsForUser(CurrentUser.data.getId());

                for (Song s : likedSongs.toArray()) {
                    DatabaseIO.addSongToUser(CurrentUser.data.getId(), s.getSongId());
                }

                CurrentUser.songs = likedSongs;

                Utility.println("\n🎉 Your song preferences have been updated based on your quiz choices! 🎉", 6);
            } catch (Exception e) {
                Utility.println("⚠️ Failed to update your song interests. Please try again later.", 6);
                e.printStackTrace();
            }
        }
    }



    public static void logOut() {

        removeCurrentUserFromFile();

        data = null;
        hobbies.clear();
        songs.clear();

    }


    public static void editProfile() {

        Utility.printLines(2);
        Utility.println("EDIT PROFILE", 6);


        while (true) {

            Utility.printLines(1);
            Utility.println("1. Edit name", 5);
            Utility.println("2. Edit bio", 5);
            Utility.println("3. Edit gender", 5);
            Utility.println("4. Edit age", 5);
            Utility.println("5. Edit phone", 5);
            Utility.println("6. Edit city", 5);
            Utility.println("7. Edit email", 5);
            Utility.println("8. Edit password", 5);
            Utility.println("Any other -> Go back", 5);

            Utility.print("Enter your choice: ", 4);

            String choice = scanner.nextLine();

            switch (choice) {

                case "1": {
                    String name;
                    while (true) {

                        Utility.print("Enter new name: ", 4);
                        name = scanner.nextLine();
                        if (name.length() > 40) {
                            Utility.println("Name too long. Try a shorter name", 7);
                            continue;
                        }
                        if (name.isEmpty()) {
                            Utility.println("Name cannot be empty. Try again", 7);
                            continue;
                        }
                        break;
                    }

                    try {
                        DatabaseIO.updateName(name);
                    }
                    catch (SQLException e) {
                        break;
                    }

                    break;
                }

                case "2": {

                    String bio;

                    while (true) {

                        Utility.print("Enter new bio: ", 4);
                        bio = scanner.nextLine();
                        if (bio.length() > 100) {
                            Utility.println("Bio too long. Try a shorter bio", 7);
                            continue;
                        }
                        if (bio.isEmpty()) {
                            Utility.println("bio cannot be empty. Try again", 7);
                            continue;
                        }
                        break;
                    }

                    try {
                        DatabaseIO.updateBio(bio);
                    }
                    catch (SQLException e) {
                        break;
                    }

                    break;

                }

                case "3": {
                    char gender;
                    while (true) {

                        Utility.print("Enter your gender (m/f): ", 4);
                        gender = scanner.next().toLowerCase().charAt(0);
                        scanner.nextLine();

                        if (gender != 'm' && gender != 'f') {
                            Utility.println("Your gender is not suitable for this app. Try again", 7);
                            continue;
                        }

                        break;

                    }
                    try {
                        DatabaseIO.updateGender(gender + "");
                    }
                    catch (SQLException e) {
                        break;
                    }

                    break;
                }


                case "4": {
                    int age;
                    while (true) {
                        Utility.print("Enter your age: ", 4);
                        age = scanner.nextInt();
                        scanner.nextLine();
                        if (age < 18 || age > 100) {
                            Utility.println("Your age is not suitable for this app. Try again", 7);
                            continue;
                        }
                        break;
                    }
                    try {
                        if (DatabaseIO.updateAge(age))
                            Utility.println("Update Successful", 1);
                        else
                            Utility.println("Update Failed", 0);
                    }
                    catch (SQLException e) {
                        break;
                    }

                    break;
                }

                case "5": {

                    long phone;
                    while (true) {
                        Utility.print("Enter your phone: ", 4);
                        phone = scanner.nextLong();
                        scanner.nextLine();

                        if (phone < 1000000000 || phone > 9999999999L) {
                            Utility.println("Invalid Phone", 7);
                            continue;
                        }
                        break;
                    }
                    try {
                        if (DatabaseIO.updatePhone(phone + ""))
                            Utility.println("Update Successful", 1);
                        else
                            Utility.println("Update Failed", 7);
                    }
                    catch (SQLException e) {
                        Utility.println("Update Failed", 0);
                        break;
                    }

                }

                case "6": {
                    String city;
                    while (true) {
                        Utility.print("Enter your city: ", 4);
                        city = scanner.nextLine();
                        if (city.length() > 20) {
                            Utility.println("City too long. Try a shorter city", 7);
                            continue;
                        }
                        if (city.isEmpty()) {
                            Utility.println("City cannot be empty. Try again", 7);
                            continue;
                        }
                        break;
                    }
                    try {
                        DatabaseIO.updateCity(city);
                    }
                    catch (SQLException e) {
                        break;
                    }

                    break;
                }

                case "7": {

                    String email;
                    while (true) {
                        Utility.print("Enter your email: ", 4);
                        email = scanner.nextLine();

                        if (email.length() > 30) {
                            Utility.println("Email too long. Try a shorter email", 7);
                            continue;
                        }
                        if (email.equals("")) {
                            Utility.println("Email cannot be empty. Try again", 7);
                            continue;
                        }
                        break;
                    }
                    try {
                        DatabaseIO.updateEmail(email);
                    }
                    catch (SQLException e) {
                        break;
                    }

                    break;
                }

                case "8": {


                    String password;

                    while (true) {
                        Utility.println("Enter current password: ", 4);
                        password = scanner.nextLine();

                        try {
                            if (CurrentUser.data.getUserId() == DatabaseIO.getUserFromAuth(
                                    CurrentUser.data.getEmail(), password)
                                    .getUserId()
                            )
                                break;
                            else {
                                Utility.println("Incorrect Password.", 0);
                                if (!Utility.tryAgain())
                                    break;
                            }
                        }
                        catch (Exception e) {
                            Utility.println("Incorrect Password.", 0);

                            if (!Utility.tryAgain())
                                break;

                        }

                    }

                    String newPassword;

                    while (true) {
                        Utility.print("Enter your new password: ", 4);
                        newPassword = scanner.nextLine();
                        if (newPassword.length() > 20) {
                            Utility.println("Password too long. Try a shorter password", 7);
                            continue;
                        }
                        if (newPassword.equals("")) {
                            Utility.println("Password cannot be empty. Try again", 7);
                            continue;
                        }
                        break;
                    }
                    try {
                        if(DatabaseIO.updatePassword(newPassword))
                            Utility.println("Update Successful", 1);
                        else
                            Utility.println("Update Failed", 7);
                    }
                    catch (SQLException e) {
                        Utility.println("Update Failed", 7);
                        break;
                    }

                    break;

                }

                default: {
                    return;
                }

            }

        }

    }

}