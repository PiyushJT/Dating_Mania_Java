package util;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import db.DatabaseIO;
import ds.BST;
import ds.HobbyLinkedList;
import ds.MatchLinkedList;
import ds.SongLinkedList;
import ds.UserLinkedList;
import model.*;
import service.Matchmaking;
import session.CurrentUser;
import logs.Log;

public class Utility {

    public static Scanner scanner = new Scanner(System.in);


    // Functions to print lines and tabs
    public static void printLines(int lines) {

        for (int i = 1; i <= lines; i++)
            System.out.println();

    }

    public static void printTabs(int tabs) {

        for (int i = 1; i <= tabs; i++)
            System.out.print("\t");

    }


    // Functions to print welcome screen
    public static void printWelcome() {
        printLines(2);
        println("||==========================================||", 2);
        print("||", 2);
        printTabs(4);
        print("DATING MANIA", 2);
        printTabs(4);
        println("||", 2);
        println("||==========================================||", 2);
        printLines(2);

    }


    public static void print(String str, int color) {

        String[] colors = {
                "\u001B[91m", // 0 red
                "\u001B[92m", // 1 lime
                "\u001B[93m", // 2 yellow
                "\u001B[94m", // 3 blue
                "\u001B[97m", // 4 white
                "\u001B[34m", // 5 Blue
                "\u001B[32m", // 6 Green
                "\u001B[31m", // 7 Red
                "\u001B[33m", // 8 Yellow

        };

        System.out.print(colors[color] + str + "\u001B[0m");

    }


    public static void println(String str, int color) {

        String[] colors = {
                "\u001B[91m", // 0 red
                "\u001B[92m", // 1 lime
                "\u001B[93m", // 2 yellow
                "\u001B[94m", // 3 blue
                "\u001B[97m", // 4 white
                "\u001B[34m", // 5 Blue
                "\u001B[32m", // 6 Green
                "\u001B[31m", // 7 Red
                "\u001B[33m", // 8 Yellow

                "\u001B[30m", // Black
                "\u001B[31m", // Red
                "\u001B[35m", // Purple
                "\u001B[36m", // Cyan
                "\u001B[37m"  // White

        };

        System.out.println(colors[color] + str + "\u001B[0m");

    }




    // Function to open login menu
    public static void openLoginMenu() {


        Utility.printLines(2);
        Utility.println("\t\t=== 🔐 LOGIN MENU ===", 8);
        Utility.printLines(1);

        Utility.println("1. 🔑 Login", 5);
        Utility.println("2. 📝 Register (Don't have an account)", 5);
        Utility.println("3. 🛡️ Admin Access", 5);
        Utility.println("4. 🚪 Exit", 5);

        Utility.print("Enter your choice: ", 4);

        String choice = scanner.nextLine().trim();



        // Switch statement to handle user input
        switch (choice) {

            case "1":
                if (login()) {

                    Utility.printLines(2);
                    Utility.println("Welcome back " + CurrentUser.data.getName() + ".", 3);
                    Log.S("User logged in successfully");
                    openMainMenu();
                }
                else {
                    Utility.println("Login aborted.", 2);
                    Log.S("User login aborted");
                    openLoginMenu();
                }

                break;

            case "2":
                if (register()) {
                    Utility.println("Registration successful! Welcome, " + CurrentUser.data.getName() + ".", 3);
                    Log.S("User registered successfully");
                    openMainMenu();
                }
                else {
                    Utility.println("Registration aborted.", 7);
                    Log.S("User registration aborted");
                    openLoginMenu();
                }

                break;

            case "3":
                adminLogin();
                break;

            case "4":
                Utility.printLines(2);
                Utility.println("Exiting...", 6);
                Log.S("User exited manually");
                System.exit(0);

                break;

            default:
                Utility.println("Invalid choice.", 7);
                openLoginMenu();

                break;
        }

    }


    // Function to open registration menu
    public static boolean register() {

        // Prompt for user details

        String email;

        while (true) {

            Utility.print("Enter your email: ", 4);
            email = scanner.next();
            scanner.nextLine();

            if (!isEmailValid(email)) {
                Utility.println("Invalid email format. Try again", 7);
                continue;
            }

            try {
                if (DatabaseIO.isEmailPhoneDupe(email)) {
                    Utility.println("Email already exists. Try again", 7);
                    continue;
                }
            }
            catch (SQLException e) {
                Utility.println("Error checking for duplicate email. Trying again", 7);
                continue;
            }

            break;

        }


        long phone;

        while (true) {

            Utility.print("Enter your phone: ", 4);
            String strPhone = scanner.next();
            scanner.nextLine();

            if (!isPhoneValid(strPhone)) {
                Utility.println("Invalid phone number. Try again", 7);
                continue;
            }
            phone = Long.parseLong(strPhone);

            try {
                if (DatabaseIO.isEmailPhoneDupe(phone + "")) {
                    Utility.println("Phone already exists. Try again", 7);
                    continue;
                }
            }
            catch (SQLException e) {
                Utility.println("Error checking for duplicate phone. Trying again", 7);
                continue;
            }

            break;

        }


        String name;

        while (true) {

            Utility.print("Enter your name: ", 4);
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

        String bio;

        while (true) {

            Utility.print("Enter bio: ", 4);
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


        String gender;

        while (true) {

            Utility.print("Enter your gender (m/f): ", 4);
            gender = scanner.next().toLowerCase();
            scanner.nextLine();

            if ( !(gender.equals("m") || gender.equals("f")) ) {
                Utility.println("Invalid input. Try again", 7);
                continue;
            }

            break;

        }

        int age;
        while (true) {

            Utility.print("Enter your age: ", 4);
            String ageS = scanner.next();
            scanner.nextLine();

            try {

                age = Integer.parseInt(ageS);

                if (age < 18 || age > 100) {
                    Utility.println("Your age is not suitable for this app. Try again", 7);
                    continue;
                }
            }
            catch (NumberFormatException e) {

                Utility.println("Invalid Age", 7);
                continue;
            }

            break;

        }


        String city;

        while (true) {

            Utility.print("Enter your city: ", 4);
            city = scanner.nextLine();

            if (city.isEmpty()) {
                Utility.println("City cannot be empty. Try again", 7);
                continue;
            }
            break;

        }


        String password;

        while (true) {

            Utility.print("Enter your password (min 8 chars): ", 4);
            password = scanner.next();
            scanner.nextLine();

            if (!isPasswordValid(password)) {
                Utility.println("Invalid password format. Try again", 7);
                continue;
            }
            break;

        }


        Exception exception = null;
        try {

            long now = getNowLong();

            // Register user in database
            User user = new User(0, name, bio, gender, age, phone, email, city, true, now, false, now, now);

            DatabaseIO.addUserToDB(user, password);

            // Set current user and save to file
            CurrentUser.data = DatabaseIO.getUserFromAuth(email, password);
            CurrentUser.addCurrentUserToFile();

            return true;
        }
        catch (Exception e) {
            exception = e;
            Log.E("Registration error: " + e.getMessage());

            return false;
        }
        finally {
            if (exception != null)
                exception.printStackTrace();
        }

    }


    // Function to open login menu
    public static boolean login() {

        // Initialize variables
        String emailPhone;
        String password;


        // Loop until valid email and password are valid
        while (true) {

            // Getting user input
            Utility.printLines(1);
            Utility.print("Enter email / phone: ", 4);
            emailPhone = scanner.next();
            scanner.nextLine();
            Utility.print("Enter password: ", 4);
            password = scanner.next();
            scanner.nextLine();


            // validating email/phone and password
            if (
                    !(isEmailValid(emailPhone) || isPhoneValid(emailPhone))
                            ||
                            !isPasswordValid(password)
            ) {

                Utility.printLines(1);
                Utility.println("Invalid Credentials", 0);

                // if user chooses to try again, loop continues
                if (tryAgain())
                    continue;
                else
                    return false;
            }
            if( !DatabaseIO.isAccountActive(emailPhone) ) {

                Utility.println("This Account is deactivated.", 7);

                Utility.println("y. -> Reactivate your account.", 5);
                Utility.println("Any other -> Cancel and go back", 5);

                Utility.print("Enter your choice: ", 4);

                String choice = scanner.next();
                scanner.nextLine();

                if(choice.equalsIgnoreCase("y"))
                    DatabaseIO.reActivate(emailPhone);
                else
                    openLoginMenu();

            }

            break;

        }


        // Exception for debugging
        try {

            // Getting user data from database
            CurrentUser.data = DatabaseIO.getUserFromAuth(emailPhone, password);


            // If user data is null, user is not registered or if acc. is deleted
            if (CurrentUser.data == null || CurrentUser.data.isDeleted()) {

                if (CurrentUser.data != null)
                    Utility.println("Account not found.", 7);
                else
                    Utility.println("Wrong Credentials.", 7);

                if (tryAgain())
                    return login();

                return false;

            }

            try {
                DatabaseIO.updateLastActive(CurrentUser.data.getUserId());
            }
            catch (SQLException e) {
                System.out.println(e.getMessage());
            }

            // Method to add user_id to current user file
            CurrentUser.addCurrentUserToFile();

        }
        // Invalid email or password
        catch (Exception e) {
            Utility.println("Invalid Credentials.", 0);

            if (tryAgain())
                return login();

            return false;
        }


        try {
            CurrentUser.initUserData();
        }
        catch (Exception e) {
            Log.E("Error initializing user data: " + e.getMessage());
        }
        return true;

    }

    public static void adminLogin() {
        Utility.printLines(2);
        Utility.println("ADMIN LOGIN", 8);
        Utility.print("Enter admin password: ", 4);
        String adminPass = scanner.nextLine();

        if (adminPass.equals("ADMIN123")) {
            Utility.println("Login successful.", 6);
            openAdminMenu();
        } else {
            Utility.println("Incorrect password.", 0);
            openLoginMenu();
        }
    }

    public static void openAdminMenu() {
        while (true) {
            Utility.printLines(2);
            Utility.println("\t\t=== 🛡️ ADMIN MENU ===", 8);
            Utility.printLines(1);

            Utility.println("1. 👥 Get active user count", 6);
            Utility.println("2. ⚽ Add new hobby", 6);
            Utility.println("3. 🎵 Add new song", 6);
            Utility.println("4. 🔓 Log out", 6);

            Utility.print("Enter choice: ", 4);

            String choice = scanner.nextLine();

            try {
                switch (choice) {
                    case "1":
                        int activeCount = DatabaseIO.getActiveUserCount();
                        Utility.println("Active users: " + activeCount, 6);
                        break;

                    case "2":
                        addNewHobby();
                        break;

                    case "3":
                        addNewSong();
                        break;

                    case "4":
                        Utility.println("Logging out from admin...", 6);
                        openLoginMenu();
                        return;

                    default:
                        Utility.println("Invalid option. Try again.", 7);
                }
            } catch (Exception e) {
                Utility.println("Error: " + e.getMessage(), 7);
            }
        }
    }

    public static void addNewHobby() {
        boolean checker = true;
        while (checker) {
            Utility.println("Enter new hobby name: ", 4);
            Utility.println("Enter 0 to abort addition ", 4);

            String hobbyName = scanner.nextLine().trim();


            if (hobbyName.isEmpty()) {
                Utility.println("Hobby name cannot be empty. Try again.", 7);
                continue;
            }
            if(hobbyName.equals("0")) {
                Utility.println("Hobby addition aborted",0);
                 checker =false;
                break;
            }

            try {
                // Fetch all existing hobbies (you can cache this as needed)
                HashMap<Integer, String> existingHobbies = DatabaseIO.getAllHobbies();

                boolean exists = existingHobbies.values().stream()
                        .anyMatch(h -> h.equalsIgnoreCase(hobbyName));

                if (exists) {
                    Utility.println("This hobby already exists. Please try again.", 7);
                    continue;
                }

                // Add hobby if unique
                boolean success = DatabaseIO.addNewIntoHobby(hobbyName);
                if (success) {
                    Utility.println("Hobby added successfully.", 6);
                } else {
                    Utility.println("Failed to add hobby.", 7);
                }
                break; // exit loop on success or failure
            } catch (SQLException e) {
                Utility.println("Database error: " + e.getMessage(), 7);
                break; // exit on DB error
            }
        }
    }

public static void addNewSong() {
    try {
        Utility.println("Enter song name: ", 4);
        Utility.println("Enter 0 to abort addition ", 4);

        String songName = scanner.nextLine().trim();
        if (songName.equals("0")) {
            Utility.println("Song addition aborted", 0);
            return;
        }

        Utility.println("Enter song URL: ", 4);
        Utility.println("Enter 0 to abort addition ", 4);
        String songUrl = scanner.nextLine().trim();
        if (songUrl.equals("0")) {
            Utility.println("Song addition aborted", 0);
            return;
        }

        Utility.println("Enter artist name: ", 4);
        Utility.println("Enter 0 to abort addition ", 4);
        String artistName = scanner.nextLine().trim();
        if (artistName.equals("0")) {
            Utility.println("Song addition aborted", 0);
            return;
        }

        // Check for duplicates ignoring case for song name, artist, and URL
        SongLinkedList existingSongs = Song.songs; // Cached list of songs
        for (Song s : existingSongs.toArray()) {
            if (s.getSongName().equalsIgnoreCase(songName) &&
                    s.getArtistName().equalsIgnoreCase(artistName)) {
                Utility.println("This song by this artist already exists. Please try again.", 7);
                return;
            }
            if (s.getSongUrl().equalsIgnoreCase(songUrl)) {
                Utility.println("This song URL already exists. Please try again.", 7);
                return;
            }
        }

        int songType;
        boolean checker = true;
        while (checker) {
            try {
                Utility.println("Please select a song type by entering the corresponding number (1 to 10):", 4);
                Utility.println("1. Bollywood", 4);
                Utility.println("2. Pop", 4);
                Utility.println("3. Rock", 4);
                Utility.println("4. Classical", 4);
                Utility.println("5. Hip Hop", 4);
                Utility.println("6. Electronic/EDM", 4);
                Utility.println("7. Indie", 4);
                Utility.println("8. Jazz", 4);
                Utility.println("9. Regional/Folk", 4);
                Utility.println("10. Devotional", 4);
                Utility.print("Enter 0 to abort addition ", 4);

                songType = Integer.parseInt(scanner.nextLine());
                if (songType == 0) {
                    Utility.println("Song addition aborted", 0);
                    checker = false;
                    break;
                }
                if (songType < 1 || songType > 10) {
                    Utility.println("Song type must be between 1 and 10.", 7);
                    continue;
                }
                boolean success = DatabaseIO.addNewIntoSong(songName, songUrl, artistName, songType);
                if (success) {
                    Utility.println("Song added successfully.", 6);
                } else {
                    Utility.println("Failed to add song.", 7);
                }
                checker = false;

            } catch (NumberFormatException e) {
                Utility.println("Invalid input. Please enter a number between 1 and 10.", 7);
            }
        }
    } catch (SQLException e) {
        Utility.println("Database error: " + e.getMessage(), 7);
    }
}


    public static void openMainMenu() {


        int matchReq = 0;
        try {
            matchReq = DatabaseIO.getMatchesByUid(CurrentUser.data.getUserId()).length();
        }
        catch (SQLException e) {
            Log.E("Error getting matches: " + e.getMessage());
        }

        Utility.printLines(2);
       // println("\t\tMenu", 5);
        Utility.println("\t\t=== MAIN MENU ===", 8);

        Utility.printLines(1);

        if(CurrentUser.hobbies.isEmpty())
            Utility.println(" 1. ⚽️ Register your hobbies", 5);
        else
            Utility.println(" 1. ⚽️ View your hobbies", 5);


        if(CurrentUser.songs.isEmpty())
            Utility.println(" 2. 🎧 Take the song quiz", 5);
        else
            Utility.println(" 2. 🎧 View your song", 5);

        Utility.println(" 3. ❤️ View your matches", 5);
        Utility.println(" 4. 👋 Create new match", 5);
        Utility.println(" 5. 💌 Match requests [" + matchReq + "]", 5);
        Utility.println(" 6. 🙎🏼‍♂️ Open my profile", 5);
        Utility.println(" 7. 🚫 Block / unblock user", 5);
        Utility.println(" 8. 🗑️ Delete / Deactivate account", 5);
        Utility.println(" 9. 🔓 Log out", 5);
        Utility.println("10. 🏃🏼 Exit", 5);

        Utility.print("Enter your choice: ", 4);

        String choice = scanner.next();
        scanner.nextLine();

        switch (choice) {

            case "1": {

                CurrentUser.updateHobbies();
                openMainMenu();

                break;
            }


            case "2": {

                CurrentUser.takeSongQuiz();
                openMainMenu();

                break;

            }

            /*
            case "3": {


                printLines(2);

                UserLinkedList matches = new UserLinkedList();

                try {
                    matches = DatabaseIO.getMatchedUsers();
                    Utility.println("Match count: " + matches.length(), 6);
                }
                catch (SQLException e) {
                    Log.E("Error getting matches: " + e.getMessage());
                    openMainMenu();
                }

                if(!matches.isEmpty()) {

                    for (User user : matches.toArray()) {
                        int uid = user.getId();

                        try {
                            Profile.display(
                                    DatabaseIO.getUserFromUid(uid),
                                    DatabaseIO.getHobbiesFromUID(uid),
                                    DatabaseIO.getSongsFromUID(uid)
                            );
                        } catch (SQLException e) {
                            Log.E("Error getting user: " + e.getMessage());
                        }

                    }

                }
                else
                    Utility.println("Get to matching and connect with some new people!", 6);


                openMainMenu();
                break;

            }
            */
            case "3": {
                printLines(2);

                UserLinkedList matches = new UserLinkedList();

                try {
                    matches = DatabaseIO.getMatchedUsers();
                    Utility.println("Match count: " + matches.length(), 6);
                }
                catch (SQLException e) {
                    Log.E("Error getting matches: " + e.getMessage());
                    openMainMenu();
                }

                if (!matches.isEmpty()) {
                    for (User user : matches.toArray()) {
                        int uid = user.getId();
                        try {
                            Profile.display(
                                    DatabaseIO.getUserFromUid(uid),
                                    DatabaseIO.getHobbiesFromUID(uid),
                                    DatabaseIO.getSongsFromUID(uid)
                            );
                        } catch (SQLException e) {
                            Log.E("Error getting user: " + e.getMessage());
                        }
                    }

                    //  Unmatch Option
                    Utility.printLines(1);
                    Utility.println("🚪 If you wish to unmatch, enter the user ID.", 5);
                    Utility.println("Enter 0 to go back.", 5);
                    Utility.print("Enter user ID to unmatch: ", 4);

                    String input = scanner.nextLine().trim();

                    if (!input.equals("0")) {
                        try {
                            int unmatchUid = Integer.parseInt(input);
                            if (DatabaseIO.unmatchUser(unmatchUid)) {
                                Utility.println("❌ You have unmatched with user ID: " + unmatchUid, 6);
                            } else {
                                Utility.println("⚠️ Could not unmatch (are you actually matched with this user?)", 7);
                            }
                        } catch (NumberFormatException e) {
                            Utility.println("Invalid input", 7);
                        } catch (SQLException e) {
                            Log.E("Error unmatching user: " + e.getMessage());
                            Utility.println("❌ Database error while unmatching.", 7);
                        }
                    }
                } else {
                    Utility.println("Get to matching and connect with some new people!", 6);
                }

                openMainMenu();
                break;
            }


            case "4": {

                Utility.printLines(2);

                Utility.println("BY Hobby / Song", 6);

                Utility.printLines(1);
                Utility.println("1. Match by Hobby", 5);
                Utility.println("2. Match by Song", 5);
                Utility.println("Any other -> Cancel", 5);

                Utility.print("Enter your choice: ", 4);

                String choice2 = scanner.next();
                scanner.nextLine();

                switch (choice2) {

                    case "1": {

                        BST matches = Matchmaking.matchMadeUsingHobby();

                        if (matches.isEmpty()) {
                            Utility.println("No matches found based on shared hobbies.", 6);
                        } else {
                            Scanner scanner = new Scanner(System.in);

                            while (!matches.isEmpty()) {
                                User potentialMatch = matches.pollMax();

                                try {
                                    // Display profile using your Profile class
                                    HobbyLinkedList theirHobbies = DatabaseIO.getHobbiesFromUID(potentialMatch.getId());

                                    Profile.display(potentialMatch, theirHobbies);
                                } catch (Exception e) {
                                    Utility.println("⚠️ Couldn't load full profile for this user.", 6);
                                    continue;
                                }

                                Utility.printLines(1);
                                Utility.println("Swipe [r] to match ✅, [l] to skip ⛔, or [q] to quit:", 5);
                                Utility.println("r. Send match request", 5);
                                Utility.println("l. Skip user", 5);
                                Utility.println("q. Quit matchmaking", 5);

                                boolean validInput = false;
                                while (!validInput) {
                                    Utility.print("Your choice (r/l/q): ", 4);
                                    String input = scanner.nextLine().trim().toLowerCase();

                                    switch (input) {
                                        case "r":
                                            try {
                                                DatabaseIO.sendMatchRequest(potentialMatch.getId(), "hobby");
                                                Utility.println("✅ Match request sent to " + potentialMatch.getName() + "!", 1);
                                            } catch (Exception e) {
                                                Utility.println("❌ Failed to send request. Please try again later.", 0);
                                            }
                                            validInput = true;
                                            break;

                                        case "l":
                                            Utility.printLines(1);
                                            Utility.println("⛔ Skipped " + potentialMatch.getName() + ".", 6);
                                            Utility.printLines(1);
                                            validInput = true;
                                            break;

                                        case "q":
                                            Utility.println("🚪 Exiting matchmaking.", 6);
                                            matches.clear(); // clear remaining matches to exit loop
                                            validInput = true;
                                            break;

                                        default:
                                            Utility.println("Invalid input", 0);
                                            break;
                                    }
                                }
                            }
                        }

                        Utility.openMainMenu();
                        break;
                    }

                    case "2":  {

                        BST matches = Matchmaking.matchMadeUsingSong();

                        if (matches.isEmpty()) {
                            Utility.println("No matches found based on shared songs.", 6);
                        }
                        else {
                            Scanner scanner = new Scanner(System.in);

                            while (!matches.isEmpty()) {
                                User potentialMatch = matches.pollMax();

                                try {
                                    SongLinkedList theirSongs = DatabaseIO.getSongsFromUID(potentialMatch.getId());

                                    Profile.display(potentialMatch, theirSongs);
                                }
                                catch (Exception e) {
                                    Utility.println("⚠️ Couldn't load full profile for this user.", 0);
                                    continue;
                                }

                                Utility.printLines(1);
                                Utility.println("Swipe [r] to match ✅, [l] to skip ⛔, or [q] to quit:", 5);
                                Utility.println("r. Send match request", 5);
                                Utility.println("l. Skip user", 5);
                                Utility.println("q. Quit matchmaking", 5);

                                boolean validInput = false;
                                while (!validInput) {
                                    Utility.print("Your choice (r/l/q): ", 4);
                                    String input = scanner.nextLine().trim().toLowerCase();

                                    switch (input) {
                                        case "r":
                                            try {
                                                DatabaseIO.sendMatchRequest(potentialMatch.getId(), "song");
                                                Utility.println("✅ Match request sent to " + potentialMatch.getName() + "!", 6);
                                                Utility.printLines(1);
                                            } catch (Exception e) {
                                                Utility.println("❌ Failed to send request. Please try again later.", 6);
                                            }
                                            validInput = true;
                                            break;

                                        case "l":
                                            Utility.println("⛔ Skipped " + potentialMatch.getName() + ".", 6);
                                            Utility.printLines(1);
                                            validInput = true;
                                            break;

                                        case "q":
                                            Utility.println("🚪 Exiting matchmaking.", 6);
                                            matches.clear(); // clear remaining matches to exit loop
                                            validInput = true;
                                            break;

                                        default:
                                            Utility.println("❓ Invalid input! Please enter [r], [l], or [q].", 4);
                                            break;
                                    }
                                }
                            }
                        }

                        Utility.openMainMenu();
                        break;

                    }

                    default: {
                        openMainMenu();
                        break;
                    }

                }
                openMainMenu();
                break;
            }




            case "5": {


                MatchLinkedList matches;
                try {
                    matches = DatabaseIO.getMatchesByUid(CurrentUser.data.getUserId());
                    printLines(1);
                    Utility.println("⏳ Pending Match Requests: "+ matches.length(), 6);
                    printLines(1);
                }
                catch (SQLException e) {
                    Log.E("Error getting matches: " + e.getMessage());
                    break;
                }

                if (!matches.isEmpty()) {

                    Utility.println("===============================================", 8);
                    for (int i = 0; i < matches.length(); i++)
                        Utility.println(matches.get(i).toString(), 6);
                    Utility.println("===============================================", 8);

                    outer:
                    while (true) {
                        Utility.printLines(1);
                        Utility.println("Enter id to accept ✅ or reject ❌: ", 5);
                        Utility.println("Enter 0 to go back: ", 5);
                        Utility.println("Enter your choice: ", 4);

                        String choice2 = scanner.next();
                        scanner.nextLine();

                        if (choice2.equalsIgnoreCase("0"))
                            break;


                        for (Match match : matches.toArray()) {

                            if (choice2.equals(match.getSenderUserId() + "")) {

                                Utility.printLines(1);
                                Utility.println("y -> to accept ✅", 5);
                                Utility.println("Anything else -> to reject ❌: ", 5);
                                Utility.println("Enter your choice: ", 4);
                                String ch = scanner.next();


                                try {

                                    if (ch.equalsIgnoreCase("y"))
                                        DatabaseIO.acceptMatch(match);
                                    else
                                        DatabaseIO.rejectMatch(match);

                                } catch (SQLException e) {
                                    Log.E("Error accepting / Rejecting match: " + e.getMessage());
                                }

                                break outer;

                            }

                        }

                        Utility.printLines(1);
                        Utility.println("Invalid choice.", 0);

                        if (!Utility.tryAgain()) {
                            break;
                        }
                    }

                }

                else
                {
                    Utility.println("Get to matching and connect with some new people! 🌟", 6);
                    printLines(1);
                }
                Utility.openMainMenu();
                break;
            }


            case "6": {


                Profile.display(CurrentUser.data, CurrentUser.hobbies, CurrentUser.songs);

                Utility.printLines(2);
                Utility.println("1. -> Edit profile", 5);
                Utility.println("Any other -> Go Back", 5);

                Utility.print("Enter your choice: ", 4);
                String choice2 = scanner.nextLine();

                if (choice2.equals("1"))
                    CurrentUser.editProfile();

                openMainMenu();

                break;

            }

            case "7": {

                Utility.printLines(2);
                Utility.println("🛑 Block / 🔓 Unblock User", 6);
                Utility.printLines(1);

                // Fetch and display blocked users
                UserLinkedList blockedUsers;
                try {
                    blockedUsers = DatabaseIO.getBlockedUsers(CurrentUser.data.getUserId());

                    if (blockedUsers.isEmpty()) {
                        Utility.println("You have no blocked users.", 6);
                    } else {
                        Utility.println("🛑Blocked Users:", 6);
                        for (User user : blockedUsers.toArray()) {
                            Utility.println(" - " + user.getName() + " (User ID: " + user.getUserId() + ")", 6);
                        }
                        printLines(1);
                    }
                } catch (SQLException e) {
                    Utility.println("Error fetching blocked users: " + e.getMessage(), 7);
                }

                Utility.println("1. 🛑 Block", 5);
                Utility.println("2. 🔓 Unblock", 5);
                Utility.println("Any other -> Back", 5);

                Utility.print("Enter your choice: ", 4);
                String choice2 = scanner.next();
                scanner.nextLine();

                switch (choice2) {

                    case "1": {

                        Utility.printLines(1);
                        Utility.println("🛑 Block User", 6);

                        Utility.printLines(1);
                        Utility.println("Enter user's name to block ", 4);
                        Utility.println("Enter 0 to abort blocking ", 4);

                        String name = scanner.next();
                        scanner.nextLine();

                        if(!Objects.equals(name, "0")) {

                            UserLinkedList foundUsers = new UserLinkedList();
                            boolean looper = true;


                            outer:
                            while (looper) {

                                Utility.printLines(1);
                                Utility.println("===============================================", 8);
                                for (User user : User.users.toArray())
                                    if (user.getName().toLowerCase().contains(name)) {

                                        if (user.getUserId() == CurrentUser.data.getUserId())
                                            continue;

                                        foundUsers.insert(user);
                                        Utility.println(user.toString(), 6);
                                    }
                                Utility.println("===============================================", 8);

                                Utility.printLines(1);
                                Utility.println("Enter user id to block: ", 4);
                                Utility.println("Enter 0 to abort blocking ", 4);

                                String id = scanner.next();
                                scanner.nextLine();

                                if(!Objects.equals(id, "0")) {
                                    try {
                                        int uid = Integer.parseInt(id);

                                        for (User users : foundUsers.toArray())
                                            if (users.getUserId() == uid) {
                                                if (DatabaseIO.blockUser(uid))
                                                    Utility.println("User blocked successfully.", 1);
                                                else
                                                    Utility.println("Error blocking user.", 0);
                                                break outer;
                                            }

                                        Utility.println("Invalid user id.", 7);
                                        if (!tryAgain())
                                            break;

                                    } catch (NumberFormatException e) {
                                        Utility.println("Invalid user id.", 7);
                                        if (!tryAgain())
                                            break;
                                    } catch (SQLException e) {
                                        Log.E("Error blocking user: " + e.getMessage());
                                        if (!tryAgain())
                                            break;
                                    }
                                }
                                else
                                {
                                    Utility.println("Blocking process aborted!",0);
                                    looper =false;
                                }
                            }
                        }

                        else
                        {
                            Utility.println("Blocking process aborted!",0);
                        }

                        break;
                    }

                    case "2": {

                        Utility.printLines(2);
                        Utility.println("🔓 Unblock user", 6);
                        Utility.printLines(1);

                        try {
                            blockedUsers = DatabaseIO.getBlockedUsers(CurrentUser.data.getUserId());
                        }
                        catch (Exception e) {
                            Log.E("Error getting blocked users: " + e.getMessage());
                            break;
                        }

                        if (blockedUsers.isEmpty()) {
                            Utility.println("Your block list is empty", 0);
                            break;
                        }

                        Utility.println("🛑Blocked Users:", 6);
                        for (User user : blockedUsers.toArray()) {
                            Utility.println(" - " + user.getName() + " (User ID: " + user.getUserId() + ")", 6);
                        }

                        Utility.printLines(1);
                        Utility.println("Enter user id to unblock: ", 4);
                        Utility.println("Enter 0 to abort unblocking ", 4);
                        String id = scanner.next();

                        if(!Objects.equals(id, "0")) {
                            try {
                                int uid = Integer.parseInt(id);

                                if(DatabaseIO.unblockUser(uid)){
                                    Utility.println("User unblocked successfully.", 1);
                                }
                                else
                                    Utility.println("Error blocking user.", 0);
                            } catch (NumberFormatException e) {
                                Utility.println("Invalid user id.", 7);
                            } catch (SQLException e) {
                                Log.E("Error unblocking user: " + e.getMessage());
                            }
                        }
                        else
                        {
                            Utility.println("Blocking process aborted!",0);
                        }


                        break;
                    }


                    default: {

                        Utility.println("Process canceled", 6);
                        break;

                    }


                }

                openMainMenu();
                break;

            }


            case "8": {

                Utility.printLines(2);
                Utility.println("🗑️ Delete / Deactivate Account", 6);

                Utility.printLines(1);
                Utility.println("1. Delete Account", 5);
                Utility.println("2. Deactivate Account", 5);
                Utility.println("Any other -> Cancel", 5);

                Utility.print("Enter your choice: ", 4);

                String choice2 = scanner.next();
                scanner.nextLine();

                switch (choice2) {

                    case "1": {

                        Utility.printLines(2);
                        Utility.println("DELETE ACCOUNT PERMANENTLY ??", 0);
                        Utility.println("1. Delete Account", 3);
                        Utility.println("Any other -> Cancel", 3);

                        int confirmation = scanner.nextInt();
                        if(confirmation==1) {

                            Utility.print("Enter your password to confirm: ", 4);
                            String password = scanner.next();
                            scanner.nextLine();

                            try {
                                if (DatabaseIO.deleteUser(password)) {
                                    CurrentUser.logOut();

                                    Utility.println("Account deleted successfully.", 1);
                                } else
                                    Utility.println("Wrong password.", 7);
                            } catch (Exception e) {
                                Utility.println("Some error occurred..", 7);
                                openMainMenu();
                                return;
                            }
                        }
                        else {
                            printLines(1);
                            Utility.println("Deletion aborted!",0);
                        }
                        break;
                    }

                    case "2":  {

                        Utility.printLines(2);
                        Utility.println("Are you sure you want to Deactivate your account??", 0);
                        Utility.println("1. Deactivate Account", 3);
                        Utility.println("Any other -> Cancel", 3);

                        int confirmation = scanner.nextInt();
                        if(confirmation==1) {


                            Utility.println("Enter your password to confirm: ", 4);
                            String password = scanner.next();
                            scanner.nextLine();

                            try {
                                if (DatabaseIO.deactivateUser(password)) {
                                    CurrentUser.logOut();

                                    Utility.println("Account deactivated successfully.", 6);
                                    Utility.println("Log in again to activate your account.", 6);
                                } else
                                    Utility.println("Wrong password.", 6);
                            } catch (Exception e) {
                                Utility.println("Some error occurred..", 6);
                                openMainMenu();
                                return;
                            }
                        }
                        else {
                            printLines(1);
                            Utility.println("Deactivation aborted!",0);
                        }

                        break;
                    }


                    default: {
                        openMainMenu();
                        break;
                    }

                }

                if (CurrentUser.data == null)
                    openLoginMenu();
                else
                    openMainMenu();
                break;


            }



            case "9": {

                Utility.println("👋 Logging out...", 2);
                CurrentUser.logOut();

                openLoginMenu();

                break;

            }


            case "10": {

                Utility.println("Exiting...", 6);
                System.exit(0);


            }

            default: {

                Utility.println("❌ Invalid input.", 7);
                openMainMenu();
                break;

            }

        }

    }


    // Functions to validate email and password
    public static boolean isEmailValid(String email) {
        if (email == null || email.isEmpty())
            return false;
        return email.matches("[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}");
    }

    public static boolean isPhoneValid(String phone) {
        try {
            long ph = Long.parseLong(phone);

            return ph >= 6000000000L && ph <= 9999999999L;
        }
        catch (Exception e) {
            return false;
        }
    }

    public static boolean isPasswordValid(String password) {
        if (password == null)
            return false;
        return password.length() >= 8;
    }


    // Function to print error message when log file is unaccessible
    public static void printLogInaccessibleWarning() {
        printLines(2);
        System.out.print("||");
        printTabs(2);
        System.out.print("Warning");
        printTabs(2);
        System.out.print("||");
        printLines(1);
        System.out.println("Log file unaccessible.");
        printLines(2);
    }


    public static long getDateFromSQLDate(String dateStr) {

        return Long.parseLong(
                dateStr
                        .replace("-", "")
                        .replace(":", "")
                        .replace(" ", "")
                        .substring(0, 14)
        );

    }


    public static String getDateString(long dateLong) {

        // Parse the long to LocalDateTime
        String dateStr = String.valueOf(dateLong);
        java.time.format.DateTimeFormatter formatter = java.time.format.DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        java.time.LocalDateTime dateTime;

        try {
            dateTime = java.time.LocalDateTime.parse(dateStr, formatter);
        }
        catch (Exception e) {
            return dateLong + "";
        }

        java.time.LocalDateTime now = java.time.LocalDateTime.now();
        java.time.LocalDate today = now.toLocalDate();

        java.time.LocalDate date = dateTime.toLocalDate();
        java.time.format.DateTimeFormatter timeFmt = java.time.format.DateTimeFormatter.ofPattern("h:mm a");
        java.time.format.DateTimeFormatter dateFmt = java.time.format.DateTimeFormatter.ofPattern("d MMMM yyyy");
        String timePart = dateTime.format(timeFmt);

        if (date.equals(today)) {
            return timePart + ", today";
        }
        else if (date.equals(today.minusDays(1))) {
            return timePart + ", yesterday";
        }
        else {
            return timePart + ", " + dateTime.format(dateFmt);
        }

    }


    // Function to get user input for try again (multiple uses)
    public static boolean tryAgain() {
        Utility.println("1. -> Try again", 5);
        Utility.println("Any other. -> Go back", 5);

        Utility.print("Enter your choice: ", 4);
        String choice = scanner.next();
        scanner.nextLine();
        return choice.equals("1");
    }

    public static long getNowLong() {
        return Long.parseLong(
                LocalDateTime
                        .now()
                        .format(
                                DateTimeFormatter.ofPattern("yyyyMMddHHmmss")
                        )
        );
    }

}
