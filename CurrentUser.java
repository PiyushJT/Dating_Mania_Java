import java.io.*;
import java.sql.*;
import java.util.*;

public class CurrentUser {

    // current user data
    static User data;

    static ArrayList<Hobby> hobbies = new ArrayList<>();
    static ArrayList<Song> songs = new ArrayList<>();

    static Scanner scanner = new Scanner(System.in);


    // function to initialize current user data from file
    static void initUserData() throws IOException, SQLException {

        // data file
        File userData = new File("user_data.txt");


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

        // parse uid from file and get User data from it
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
                hobbies = new ArrayList<>();
                Log.E("Current User hobbies could not be loaded.");
            }

            try {
                songs = DatabaseIO.getSongsFromUID(
                                data.getId()
                        );
            }
            catch (SQLException e) {
                songs = new ArrayList<>();
                Log.E("Current User songs could not be loaded.");
            }

        }

    }


    // function to add current user data to file
    public static void addCurrentUserToFile() {

        try {
            BufferedWriter writer = new BufferedWriter(
                    new FileWriter("user_data.txt")
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
                    new FileWriter("user_data.txt")
            );

            writer.write("");
            writer.close();
        }
        catch (Exception e) {
            Log.E("Login was unable ot delete.");
        }
    }

    public static void updateHobbies() {

        System.out.println("Update hobbies");

        if (CurrentUser.hobbies.isEmpty()) {
            System.out.println("You have no hobbies.");
        } else {

            System.out.println("Your hobbies are:");

            for (Hobby hobby : CurrentUser.hobbies)
                System.out.println("- " + hobby.getHobbyName());

        }

        System.out.println("Enter 1. -> Edit hobbies");
        System.out.println("Any other. -> Go back");


        System.out.print("Enter your choice: ");
        char choice1 = scanner.next().charAt(0);
        scanner.nextLine();


        if (choice1 == '1') {

            for (int i = 1; i <= Hobby.hobbies.size(); i++)
                System.out.println(i + ". " + Hobby.hobbies.get(i));


            int[] ind;

            outer:
            while (true) {

                System.out.println("Enter comma-separated indices of hobbies to add to your new list.");
                System.out.println("Example: 1,2,3");

                String input = scanner.next();
                scanner.nextLine();
                String[] parts = input.split(",");

                ind = new int[parts.length];

                for (int i = 0; i < parts.length; i++) {
                    try {
                        ind[i] = Integer.parseInt(parts[i].trim());

                        if (ind[i] > 20 || ind[i] < 1) {
                            System.out.println("Invalid index. Try again.");
                            continue outer; // <-- This skips current outer loop iteration
                        }

                    } catch (NumberFormatException e) {
                        System.out.println("Invalid input. Try again.");
                        continue outer; // <-- This also skips to next outer loop iteration
                    }
                }

                break; // all inputs were valid, exit the outer loop
            }


            try {
                DatabaseIO.addHobbiesToDB(ind);
            }
            catch (SQLException e) {
                return;
            }
        }

    }


    public static void updateSongs() {

        System.out.println("Update song interests");

        if (CurrentUser.songs.isEmpty()) {
            System.out.println("You have no song Interests.");
        } else {

            System.out.println("Your song interests are:");

            for (Song song : CurrentUser.songs)
                System.out.println(song);

        }

        System.out.println("Enter 1. -> Edit song interests");
        System.out.println("Any other. -> Go back");


        System.out.print("Enter your choice: ");
        char choice2 = scanner.next().charAt(0);
        scanner.nextLine();


        if (choice2 == '1') {

            for (int i = 0; i < Song.songs.size(); i++)
                System.out.println(Song.songs.get(i));


            int[] ind;

            outer:
            while (true) {

                System.out.println("Enter comma-separated ids of songs to add to your new list.");
                System.out.println("Example: 1,2,3");

                String input = scanner.next();
                scanner.nextLine();
                String[] parts = input.split(",");

                ind = new int[parts.length];

                for (int i = 0; i < parts.length; i++) {
                    try {
                        ind[i] = Integer.parseInt(parts[i].trim());

                        if (ind[i] > 30 || ind[i] < 1) {
                            System.out.println("Invalid index. Try again.");
                            continue outer; // <-- This skips current outer loop iteration
                        }

                    } catch (NumberFormatException e) {
                        System.out.println("Invalid input. Try again.");
                        continue outer; // <-- This also skips to next outer loop iteration
                    }
                }

                break; // all inputs were valid, exit the outer loop
            }


            try {
                DatabaseIO.addSongsToDB(ind);
            }
            catch (SQLException e) {
                return;
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

        System.out.println("Edit Profile");


        while (true) {
            System.out.println("1. Edit name.");
            System.out.println("2. Edit bio.");
            System.out.println("3. Edit gender.");
            System.out.println("4. Edit age.");
            System.out.println("5. Edit phone.");
            System.out.println("6. Edit city.");
            System.out.println("7. Edit email.");
            System.out.println("8. Edit password.");
            System.out.println("Any other -> Go back.");

            System.out.print("Enter your choice: ");

            String choice = scanner.nextLine();

            switch (choice) {

                case "1": {
                    String name;
                    while (true) {

                        System.out.print("Enter new name: ");
                        name = scanner.nextLine();
                        if (name.length() > 40) {
                            System.out.println("Name too long. Try a shorter name");
                            continue;
                        }
                        if (name.isEmpty()) {
                            System.out.println("Name cannot be empty. Try again");
                            continue;
                        }
                        break;
                    }

                    try {
                        DatabaseIO.updateProfile("name", name);
                    }
                    catch (SQLException e) {
                        break;
                    }

                    break;
                }

                case "2": {

                    String bio;

                    while (true) {

                        System.out.print("Enter new bio: ");
                        bio = scanner.nextLine();
                        if (bio.length() > 100) {
                            System.out.println("Bio too long. Try a shorter bio");
                            continue;
                        }
                        if (bio.isEmpty()) {
                            System.out.println("bio cannot be empty. Try again");
                            continue;
                        }
                        break;
                    }

                    try {
                        DatabaseIO.updateProfile("bio", bio);
                    }
                    catch (SQLException e) {
                        break;
                    }

                    break;

                }

                case "3": {
                    char gender;
                    while (true) {

                        System.out.print("Enter your gender (m/f): ");
                        gender = scanner.next().toLowerCase().charAt(0);
                        scanner.nextLine();

                        if (gender != 'm' && gender != 'f') {
                            System.out.println("Your gender is not suitable for this app. Try again");
                            continue;
                        }

                        break;

                    }
                    try {
                        DatabaseIO.updateProfile("gender", gender + "");
                    }
                    catch (SQLException e) {
                        break;
                    }

                    break;
                }


                case "4": {
                    int age;
                    while (true) {
                        System.out.print("Enter your age: ");
                        age = scanner.nextInt();
                        scanner.nextLine();
                        if (age < 18 || age > 100) {
                            System.out.println("Your age is not suitable for this app. Try again");
                            continue;
                        }
                        break;
                    }
                    try {
                        DatabaseIO.updateProfile("age", age);
                    }
                    catch (SQLException e) {
                        break;
                    }

                    break;
                }

                case "5": {

                    long phone;
                    while (true) {
                        System.out.print("Enter your phone: ");
                        phone = scanner.nextLong();
                        scanner.nextLine();

                        if (phone < 1000000000 || phone > 9999999999L) {
                            System.out.println("Your phone number is not suitable for this app. Try again");
                            continue;
                        }
                        break;
                    }
                    try {
                        if (DatabaseIO.updateProfile("phone", phone + ""))
                            System.out.println("Update Successful");
                        else
                            System.out.println("Update Failed");
                    }
                    catch (SQLException e) {
                        System.out.println("Update Failed");
                        break;
                    }

                }

                case "6": {
                    String city;
                    while (true) {
                        System.out.print("Enter your city: ");
                        city = scanner.nextLine();
                        if (city.length() > 20) {
                            System.out.println("City too long. Try a shorter city");
                            continue;
                        }
                        if (city.isEmpty()) {
                            System.out.println("City cannot be empty. Try again");
                            continue;
                        }
                        break;
                    }
                    try {
                        DatabaseIO.updateProfile("city", city);
                    }
                    catch (SQLException e) {
                        break;
                    }

                    break;
                }

                case "7": {

                    String email;
                    while (true) {
                        System.out.print("Enter your email: ");
                        email = scanner.nextLine();

                        if (email.length() > 30) {
                            System.out.println("Email too long. Try a shorter email");
                            continue;
                        }
                        if (email.equals("")) {
                            System.out.println("Email cannot be empty. Try again");
                            continue;
                        }
                        break;
                    }
                    try {
                        DatabaseIO.updateProfile("email", email);
                    }
                    catch (SQLException e) {
                        break;
                    }

                    break;
                }

                case "8": {


                    String password = "";

                    while (true) {
                        System.out.println("Enter current password: ");
                        password = scanner.nextLine();

                        try {
                            if (CurrentUser.data.userId == DatabaseIO.getUserFromAuth(CurrentUser.data.email, password).userId)
                                break;
                            else {
                                System.out.println("Incorrect Password.");
                                if (!Utility.tryAgain())
                                    break;
                            }
                        }
                        catch (Exception e) {
                            System.out.println("Incorrect Password.");

                            if (!Utility.tryAgain())
                                break;

                        }

                    }

                    String newPassword;

                    while (true) {
                        System.out.print("Enter your new password: ");
                        newPassword = scanner.nextLine();
                        if (newPassword.length() > 20) {
                            System.out.println("Password too long. Try a shorter password");
                            continue;
                        }
                        if (newPassword.equals("")) {
                            System.out.println("Password cannot be empty. Try again");
                            continue;
                        }
                        break;
                    }
                    try {
                        if(DatabaseIO.updatePassword(newPassword))
                            System.out.println("Update Successful");
                        else
                            System.out.println("Update Failed");
                    }
                    catch (SQLException e) {
                        System.out.println("Update Failed");
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