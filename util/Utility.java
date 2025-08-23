package util;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import db.*;
import ds.*;
import logs.*;
import model.*;
import service.*;
import session.*;

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

        };

        System.out.println(colors[color] + str + "\u001B[0m");

    }




    // Function to open login menu
    public static void openLoginMenu() {


        printLines(2);
        println("\t\t=== 🔐 LOGIN MENU ===", 8);
        printLines(1);

        println("1. 🔑 Login", 5);
        println("2. 📝 Register (Don't have an account)", 5);
        println("3. 🛡️ Admin Access", 5);
        println("4. 🚪 Exit", 5);

        print("Enter your choice: ", 4);

        String choice = scanner.nextLine().trim();



        // Switch statement to handle user input
        switch (choice) {

            case "1":
                if (login()) {

                    printLines(2);
                    println("Welcome back " + CurrentUser.data.getName() + ".", 3);
                    Log.S("User logged in successfully");
                    openMainMenu();
                }
                else {
                    println("Login aborted.", 2);
                    Log.S("User login aborted");
                    openLoginMenu();
                }

                break;

            case "2":
                if (register()) {
                    println("Registration successful! Welcome, " + CurrentUser.data.getName() + ".", 3);
                    Log.S("User registered successfully");
                    openMainMenu();
                }
                else {
                    println("Registration aborted.", 7);
                    Log.S("User registration aborted");
                    openLoginMenu();
                }

                break;

            case "3":
                adminLogin();
                break;

            case "4":
                printLines(2);
                println("Exiting...", 6);
                Log.S("User exited manually");
                System.exit(0);

                break;

            default:
                println("Invalid choice.", 7);
                openLoginMenu();

                break;
        }

    }


    // Function to open registration menu
    public static boolean register() {

        // Prompt for user details

        String email;

        while (true) {

            print("Enter your email: ", 4);
            email = scanner.next();
            scanner.nextLine();

            if (!isEmailValid(email)) {
                println("Invalid email format. Try again", 7);
                continue;
            }

            try {
                if (DatabaseIO.isEmailPhoneDupe(email)) {
                    println("Email already exists. Try again", 7);
                    continue;
                }
            }
            catch (SQLException e) {
                println("Error checking for duplicate email. Trying again", 7);
                continue;
            }

            break;

        }


        long phone;

        while (true) {

            print("Enter your phone: ", 4);
            String strPhone = scanner.next();
            scanner.nextLine();

            if (!isPhoneValid(strPhone)) {
                println("Invalid phone number. Try again", 7);
                continue;
            }
            phone = Long.parseLong(strPhone);

            try {
                if (DatabaseIO.isEmailPhoneDupe(phone + "")) {
                    println("Phone already exists. Try again", 7);
                    continue;
                }
            }
            catch (SQLException e) {
                println("Error checking for duplicate phone. Trying again", 7);
                continue;
            }

            break;

        }


        String name;

        while (true) {

            print("Enter your name: ", 4);
            name = scanner.nextLine();
            if (name.length() > 40) {
                println("Name too long. Try a shorter name", 7);
                continue;
            }
            if (name.isEmpty()) {
                println("Name cannot be empty. Try again", 7);
                continue;
            }
            break;
        }

        String bio;

        while (true) {

            print("Enter bio: ", 4);
            bio = scanner.nextLine();
            if (bio.length() > 100) {
                println("Bio too long. Try a shorter bio", 7);
                continue;
            }
            if (bio.isEmpty()) {
                println("bio cannot be empty. Try again", 7);
                continue;
            }
            break;
        }


        String gender;

        while (true) {

            print("Enter your gender (m/f): ", 4);
            gender = scanner.next().toLowerCase();
            scanner.nextLine();

            if ( !(gender.equals("m") || gender.equals("f")) ) {
                println("Invalid input. Try again", 7);
                continue;
            }

            break;

        }

        int age;
        while (true) {

            print("Enter your age: ", 4);
            String ageS = scanner.next();
            scanner.nextLine();

            try {

                age = Integer.parseInt(ageS);

                if (age < 18 || age > 100) {
                    println("Your age is not suitable for this app. Try again", 7);
                    continue;
                }
            }
            catch (NumberFormatException e) {

                println("Invalid Age", 7);
                continue;
            }

            break;

        }


        String city;

        while (true) {

            print("Enter your city: ", 4);
            city = scanner.nextLine();

            if (city.isEmpty()) {
                println("City cannot be empty. Try again", 7);
                continue;
            }
            break;

        }


        String password;

        while (true) {

            print("Enter your password (min 8 chars): ", 4);
            password = scanner.next();
            scanner.nextLine();

            if (!isPasswordValid(password)) {
                println("Invalid password format. Try again", 7);
                continue;
            }
            break;

        }

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
            Log.E("Registration error: " + e.getMessage());

            return false;
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
            printLines(1);
            print("Enter email / phone: ", 4);
            emailPhone = scanner.next();
            scanner.nextLine();
            print("Enter password: ", 4);
            password = scanner.next();
            scanner.nextLine();


            // validating email/phone and password
            if (
                    !(isEmailValid(emailPhone)
                    ||
                    isPhoneValid(emailPhone))
                    ||
                    !isPasswordValid(password)
            ) {

                printLines(1);
                println("Invalid Credentials", 0);

                // if user chooses to try again, loop continues
                if (tryAgain())
                    continue;
                else
                    return false;
            }

            if( !DatabaseIO.isAccountActive(emailPhone) ) {

                println("This Account is deactivated.", 7);

                println("y. -> Reactivate your account.", 5);
                println("Any other -> Cancel and go back", 5);

                print("Enter your choice: ", 4);

                String choice = scanner.next();
                scanner.nextLine();

                if(choice.equalsIgnoreCase("y"))
                    DatabaseIO.reActivate(emailPhone);
                else
                    openLoginMenu();

            }

            break;

        }


        try {

            // Getting user data from database
            CurrentUser.data = DatabaseIO.getUserFromAuth(emailPhone, password);


            // If user data is null, user is not registered or if acc. is deleted
            if (CurrentUser.data == null || CurrentUser.data.isDeleted()) {

                if (CurrentUser.data != null)
                    println("Account not found.", 7);
                else
                    println("Wrong Credentials.", 7);

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
            println("Invalid Credentials.", 0);

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

        printLines(2);
        println("ADMIN LOGIN", 8);
        print("Enter admin password: ", 4);
        String adminPass = scanner.nextLine();

        if (adminPass.equals("Admin@123")) {
            println("Login successful.", 6);
            openAdminMenu();
        } else {
            println("Incorrect password.", 0);
            openLoginMenu();
        }
    }

    public static void openAdminMenu() {

        while (true) {
            printLines(2);
            printTabs(2);
            println("=== 🛡️ ADMIN MENU ===", 8);
            printLines(1);

            println("1. 👥 Get active user count", 6);
            println("2. ⚽ Add new hobby", 6);
            println("3. 🎵 Add new song", 6);
            println("4. 🔓 Log out", 6);

            print("Enter choice: ", 4);

            String choice = scanner.nextLine();

            try {
                switch (choice) {
                    case "1":
                        int activeCount = DatabaseIO.getActiveUserCount();
                        println("Active users: " + activeCount, 6);
                        break;

                    case "2":
                        addNewHobby();
                        break;

                    case "3":
                        addNewSong();
                        break;

                    case "4":
                        println("Logging out from admin...", 6);
                        openLoginMenu();
                        return;

                    default:
                        println("Invalid option. Try again.", 7);
                }
            } catch (Exception e) {
                println("Error: " + e.getMessage(), 7);
            }
        }
    }

    public static void addNewHobby() {

        while (true) {
            println("Enter new hobby name: ", 4);
            println("Enter 0 to abort addition ", 4);

            String hobbyName = scanner.nextLine().trim();


            if (hobbyName.isEmpty()) {
                println("Hobby name cannot be empty. Try again.", 7);
                continue;
            }
            if(hobbyName.equals("0")) {
                println("Hobby addition aborted",0);
                break;
            }

            try {
                // Fetch all existing hobbies (you can cache this as needed)
                HashMap<Integer, String> existingHobbies = DatabaseIO.getAllHobbies();

                boolean exists = existingHobbies.values().stream()
                        .anyMatch(h -> h.equalsIgnoreCase(hobbyName));

                if (exists) {
                    println("This hobby already exists. Please try again.", 7);
                    continue;
                }

                // Add hobby if unique
                boolean success = DatabaseIO.addNewIntoHobby(hobbyName);
                if (success) {
                    println("Hobby added successfully.", 6);
                } else {
                    println("Failed to add hobby.", 7);
                }
                break; // exit loop on success or failure

            } catch (SQLException e) {
                println("Database error: " + e.getMessage(), 7);
                break; // exit on DB error
            }
        }
    }

    public static void addNewSong() {

        try {
            println("Enter song name: ", 4);
            println("Enter 0 to abort addition ", 4);

            String songName = scanner.nextLine().trim();
            if (songName.equals("0")) {
                println("Song addition aborted", 0);
                return;
            }

            println("Enter song URL: ", 4);
            println("Enter 0 to abort addition ", 4);

            String songUrl = scanner.nextLine().trim();
            if (songUrl.equals("0")) {
                println("Song addition aborted", 0);
                return;
            }

            println("Enter artist name: ", 4);
            println("Enter 0 to abort addition ", 4);

            String artistName = scanner.nextLine().trim();

            if (artistName.equals("0")) {
                println("Song addition aborted", 0);
                return;
            }

            // Check for duplicates ignoring case for song name, artist, and URL
            SongLinkedList existingSongs = Song.songs; // Cached list of songs

            for (Song s : existingSongs.toArray()) {

                if (
                    s.getSongName().equalsIgnoreCase(songName)
                    &&
                    s.getArtistName().equalsIgnoreCase(artistName)
                ) {
                    println("This song by this artist already exists. Please try again.", 7);
                    return;
                }

                if (s.getSongUrl().equalsIgnoreCase(songUrl)) {
                    println("This song URL already exists. Please try again.", 7);
                    return;
                }
            }

            int songType;
            boolean checker = true;

            while (checker) {
                try {
                    println("Please select a song type by entering the corresponding number (1 to 10):", 4);
                    println("1. Bollywood", 4);
                    println("2. Pop", 4);
                    println("3. Rock", 4);
                    println("4. Classical", 4);
                    println("5. Hip Hop", 4);
                    println("6. Electronic/EDM", 4);
                    println("7. Indie", 4);
                    println("8. Jazz", 4);
                    println("9. Regional/Folk", 4);
                    println("10. Devotional", 4);
                    print("Enter 0 to abort addition ", 4);

                    songType = Integer.parseInt(scanner.nextLine());
                    if (songType == 0) {
                        println("Song addition aborted", 0);
                        break;
                    }

                    if (songType < 1 || songType > 10) {
                        println("Song type must be between 1 and 10.", 7);
                        continue;
                    }

                    boolean success = DatabaseIO.addNewIntoSong(songName, songUrl, artistName, songType);

                    if (success) {
                        println("Song added successfully.", 6);
                    } else {
                        println("Failed to add song.", 7);
                    }
                    checker = false;

                } catch (NumberFormatException e) {
                    println("Invalid input. Please enter a number between 1 and 10.", 7);
                }

            }
        }
        catch (SQLException e) {
            println("Database error: " + e.getMessage(), 7);
        }
    }


    public static void openMainMenu() {


        int matchReq = 0;
        try {
            matchReq = DatabaseIO.getNewMatchesByUid(CurrentUser.data.getUserId()).length();
        }
        catch (SQLException e) {
            Log.E("Error getting matches: " + e.getMessage());
        }

        printLines(2);
       // println("\t\tMenu", 5);
        println("\t\t=== MAIN MENU ===", 8);

        printLines(1);

        if(CurrentUser.hobbies.isEmpty())
            println(" 1. ⚽️ Register your hobbies", 5);
        else
            println(" 1. ⚽️ View your hobbies", 5);


        if(CurrentUser.songs.isEmpty())
            println(" 2. 🎧 Take the song quiz", 5);
        else
            println(" 2. 🎧 View your song", 5);

        println(" 3. ❤️ View your matches", 5);
        println(" 4. 👋 Create new match", 5);
        println(" 5. 💌 Match requests [" + matchReq + "]", 5);
        println(" 6. 🙎🏼‍♂️ Open my profile", 5);
        println(" 7. 🚫 Block / unblock user", 5);
        println(" 8. 🗑️ Delete / Deactivate account", 5);
        println(" 9. 🔓 Log out", 5);
        println("10. 🏃🏼 Exit", 5);

        print("Enter your choice: ", 4);

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

            case "3": {

                printLines(2);

                UserLinkedList matches = new UserLinkedList();

                try {
                    matches = DatabaseIO.getMatchedUsers();
                    println("Match count: " + matches.length(), 6);
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
                    printLines(1);
                    println("🚪 If you wish to unmatch, enter the user ID.", 5);
                    println("Enter 0 to go back.", 5);
                    print("Enter user ID to unmatch: ", 4);

                    String input = scanner.nextLine().trim();

                    if (!input.equals("0")) {
                        try {
                            int unmatchUid = Integer.parseInt(input);
                            if (DatabaseIO.unmatchUser(unmatchUid)) {
                                println("✅ You have unmatched with user ID: " + unmatchUid, 6);
                            } else {
                                println("⚠️ Could not unmatch (are you actually matched with this user?)", 7);
                            }
                        } catch (NumberFormatException e) {
                            println("Invalid input", 7);
                        } catch (SQLException e) {
                            Log.E("Error unmatching user: " + e.getMessage());
                            println("❌ Database error while unmatching.", 7);
                        }
                    }
                } else {
                    println("Get to matching and connect with some new people!", 6);
                }

                openMainMenu();
                break;
            }


            case "4": {

                printLines(2);

                println("BY Hobby / Song", 6);

                printLines(1);
                println("1. Match by Hobby", 5);
                println("2. Match by Song", 5);
                println("Any other -> Cancel", 5);

                print("Enter your choice: ", 4);

                String choice2 = scanner.next();
                scanner.nextLine();

                switch (choice2) {

                    case "1": {

                        BST matches = Matchmaking.matchMadeUsingHobby();

                        if (matches.isEmpty()) {
                            println("No matches found based on shared hobbies.", 6);
                        } else {
                            Scanner scanner = new Scanner(System.in);

                            while (!matches.isEmpty()) {
                                User potentialMatch = matches.pollMax();

                                try {
                                    // Display profile using your Profile class
                                    HobbyLinkedList theirHobbies = DatabaseIO.getHobbiesFromUID(potentialMatch.getId());

                                    Profile.display(potentialMatch, theirHobbies);
                                } catch (Exception e) {
                                    println("⚠️ Couldn't load full profile for this user.", 6);
                                    continue;
                                }

                                printLines(1);
                                println("Swipe [r] to match ✅, [l] to skip ⛔, or [q] to quit:", 5);
                                println("r. Send match request", 5);
                                println("l. Skip user", 5);
                                println("q. Quit matchmaking", 5);

                                boolean validInput = false;
                                while (!validInput) {
                                    print("Your choice (r/l/q): ", 4);
                                    String input = scanner.nextLine().trim().toLowerCase();

                                    switch (input) {
                                        case "r":
                                            try {
                                                DatabaseIO.sendMatchRequest(potentialMatch.getId(), "hobby");
                                                println("✅ Match request sent to " + potentialMatch.getName() + "!", 1);
                                            } catch (Exception e) {
                                                println("❌ Failed to send request. Please try again later.", 0);
                                            }
                                            validInput = true;
                                            break;

                                        case "l":
                                            printLines(1);
                                            println("⛔ Skipped " + potentialMatch.getName() + ".", 6);
                                            printLines(1);
                                            validInput = true;
                                            break;

                                        case "q":
                                            println("🚪 Exiting matchmaking.", 6);
                                            matches.clear(); // clear remaining matches to exit loop
                                            validInput = true;
                                            break;

                                        default:
                                            println("Invalid input", 0);
                                            break;
                                    }
                                }
                            }

                        }

                        openMainMenu();
                        break;
                    }

                    case "2":  {

                        BST matches = Matchmaking.matchMadeUsingSong();

                        if (matches.isEmpty()) {
                            println("No matches found based on shared songs.", 6);
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
                                    println("⚠️ Couldn't load full profile for this user.", 0);
                                    continue;
                                }

                                printLines(1);
                                println("Swipe [r] to match ✅, [l] to skip ⛔, or [q] to quit:", 5);
                                println("r. Send match request", 5);
                                println("l. Skip user", 5);
                                println("q. Quit matchmaking", 5);

                                boolean validInput = false;
                                while (!validInput) {
                                    print("Your choice (r/l/q): ", 4);
                                    String input = scanner.nextLine().trim().toLowerCase();

                                    switch (input) {
                                        case "r":
                                            try {
                                                DatabaseIO.sendMatchRequest(potentialMatch.getId(), "song");
                                                println("✅ Match request sent to " + potentialMatch.getName() + "!", 6);
                                                printLines(1);
                                            } catch (Exception e) {
                                                println("❌ Failed to send request. Please try again later.", 6);
                                            }
                                            validInput = true;
                                            break;

                                        case "l":
                                            println("⛔ Skipped " + potentialMatch.getName() + ".", 6);
                                            printLines(1);
                                            validInput = true;
                                            break;

                                        case "q":
                                            println("🚪 Exiting matchmaking.", 6);
                                            matches.clear(); // clear remaining matches to exit loop
                                            validInput = true;
                                            break;

                                        default:
                                            println("❓ Invalid input! Please enter [r], [l], or [q].", 4);
                                            break;
                                    }
                                }
                            }
                        }

                        openMainMenu();
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

                    matches = DatabaseIO.getNewMatchesByUid(CurrentUser.data.getUserId());
                    printLines(1);
                    println("⏳ Pending Match Requests: "+ matches.length(), 6);
                    printLines(1);

                }
                catch (SQLException e) {
                    Log.E("Error getting matches: " + e.getMessage());
                    break;
                }

                if (!matches.isEmpty()) {

                    println("===============================================", 8);
                    for (int i = 0; i < matches.length(); i++)
                        println(matches.get(i).toString(), 6);
                    println("===============================================", 8);

                    outer: while (true) {

                        printLines(1);
                        println("Enter id to accept ✅ or reject ❌: ", 5);
                        println("Enter 0 to go back: ", 5);
                        println("Enter your choice: ", 4);

                        String choice2 = scanner.next();
                        scanner.nextLine();

                        if (choice2.equalsIgnoreCase("0"))
                            break;


                        for (Match match : matches.toArray()) {

                            if (choice2.equals(match.getSenderUserId() + "")) {

                                printLines(1);
                                println("y -> to accept ✅", 5);
                                println("n -> to reject ❌: ", 5);
                                println("Any other -> Ignore request: ", 5);
                                println("Enter your choice: ", 4);
                                String ch = scanner.next();


                                try {

                                    if (ch.equalsIgnoreCase("y"))
                                        DatabaseIO.acceptMatch(match);
                                    else if (ch.equalsIgnoreCase("n"))
                                        DatabaseIO.rejectMatch(match);

                                } catch (SQLException e) {
                                    Log.E("Error accepting / Rejecting match: " + e.getMessage());
                                }

                                break outer;

                            }

                        }

                        printLines(1);
                        println("Invalid choice.", 0);

                        if (!tryAgain()) {
                            break;
                        }
                    }

                }

                else {
                    println("Get to matching and connect with some new people! 🌟", 6);
                    printLines(1);
                }

                openMainMenu();
                break;
            }


            case "6": {


                Profile.display(CurrentUser.data, CurrentUser.hobbies, CurrentUser.songs);

                printLines(2);
                println("1. -> Edit profile", 5);
                println("Any other -> Go Back", 5);

                print("Enter your choice: ", 4);
                String choice2 = scanner.nextLine();

                if (choice2.equals("1"))
                    CurrentUser.editProfile();

                openMainMenu();

                break;

            }

            case "7": {

                printLines(2);
                println("🛑 Block / 🔓 Unblock User", 6);
                printLines(1);

                // Fetch and display blocked users
                UserLinkedList blockedUsers;
                try {
                    blockedUsers = DatabaseIO.getBlockedUsers();

                    if (blockedUsers.isEmpty())
                        println("You have no blocked users.", 6);

                    else {
                        println("🛑Blocked Users:", 6);
                        for (User user : blockedUsers.toArray()) {
                            println(" - " + user.getName() + " (User ID: " + user.getUserId() + ")", 6);
                        }
                        printLines(1);
                    }
                }
                catch (SQLException e) {
                    println("Error fetching blocked users: " + e.getMessage(), 7);
                }

                println("1. 🛑 Block", 5);
                println("2. 🔓 Unblock", 5);
                println("Any other -> Back", 5);

                print("Enter your choice: ", 4);
                String choice2 = scanner.next();
                scanner.nextLine();

                switch (choice2) {

                    case "1": {

                        printLines(1);
                        println("🛑 Block User", 6);

                        printLines(1);
                        println("Enter user's name to block ", 4);
                        println("Enter 0 to abort blocking ", 4);

                        String name = scanner.next();
                        scanner.nextLine();

                        if(!Objects.equals(name, "0")) {

                            UserLinkedList foundUsers = new UserLinkedList();
                            boolean looper = true;


                            outer:
                            while (looper) {

                                printLines(1);
                                println("===============================================", 8);
                                for (User user : User.users.toArray())
                                    if (user.getName().toLowerCase().contains(name)) {

                                        if (user.getUserId() == CurrentUser.data.getUserId())
                                            continue;

                                        foundUsers.insert(user);
                                        println(user.toString(), 6);
                                    }
                                println("===============================================", 8);

                                printLines(1);
                                println("Enter user id to block: ", 4);
                                println("Enter 0 to abort blocking ", 4);

                                String id = scanner.next();
                                scanner.nextLine();

                                if(!Objects.equals(id, "0")) {
                                    try {
                                        int uid = Integer.parseInt(id);

                                        for (User users : foundUsers.toArray()) {
                                            if (users.getUserId() == uid) {
                                                if (DatabaseIO.blockUser(uid))
                                                    println("User blocked successfully.", 1);
                                                else
                                                    println("Error blocking user.", 0);
                                                break outer;
                                            }
                                        }

                                        println("Invalid user id.", 7);
                                        if (!tryAgain())
                                            break;

                                    } catch (NumberFormatException e) {
                                        println("Invalid user id.", 7);
                                        if (!tryAgain())
                                            break;
                                    } catch (SQLException e) {
                                        Log.E("Error blocking user: " + e.getMessage());
                                        if (!tryAgain())
                                            break;
                                    }
                                }
                                else {
                                    println("Blocking process aborted!",0);
                                    looper =false;
                                }

                            }
                        }

                        else
                            println("Blocking process aborted!",0);

                        break;
                    }

                    case "2": {

                        printLines(2);
                        println("🔓 Unblock user", 6);
                        printLines(1);

                        try {
                            blockedUsers = DatabaseIO.getBlockedUsers();
                        }
                        catch (Exception e) {
                            Log.E("Error getting blocked users: " + e.getMessage());
                            break;
                        }

                        if (blockedUsers.isEmpty()) {
                            println("Your block list is empty", 0);
                            break;
                        }

                        println("🛑Blocked Users:", 6);
                        for (User user : blockedUsers.toArray())
                            println(" - " + user.getName() + " (User ID: " + user.getUserId() + ")", 6);

                        printLines(1);
                        println("Enter user id to unblock: ", 4);
                        println("Enter 0 to abort unblocking ", 4);
                        String id = scanner.next();

                        if(!Objects.equals(id, "0")) {
                            try {
                                int uid = Integer.parseInt(id);

                                if(DatabaseIO.unblockUser(uid))
                                    println("User unblocked successfully.", 1);
                                else
                                    println("Error blocking user.", 0);

                            } catch (NumberFormatException e) {
                                println("Invalid user id.", 7);
                            } catch (SQLException e) {
                                Log.E("Error unblocking user: " + e.getMessage());
                            }

                        }
                        else
                            println("Blocking process aborted!",0);

                        break;

                    }


                    default: {

                        println("Process canceled", 6);
                        break;

                    }


                }

                openMainMenu();
                break;

            }


            case "8": {

                printLines(2);
                println("🗑️ Delete / Deactivate Account", 6);

                printLines(1);
                println("1. Delete Account", 5);
                println("2. Deactivate Account", 5);
                println("Any other -> Cancel", 5);

                print("Enter your choice: ", 4);

                String choice2 = scanner.next();
                scanner.nextLine();

                switch (choice2) {

                    case "1": {

                        printLines(2);
                        println("DELETE ACCOUNT PERMANENTLY ??", 0);
                        println("1. Delete Account", 3);
                        println("Any other -> Cancel", 3);

                        int confirmation = scanner.nextInt();

                        if(confirmation==1) {

                            print("Enter your password to confirm: ", 4);
                            String password = scanner.next();
                            scanner.nextLine();

                            try {
                                if (DatabaseIO.deleteUser(password)) {
                                    CurrentUser.logOut();

                                    println("Account deleted successfully.", 1);
                                }
                                else
                                    println("Wrong password.", 7);
                            } catch (Exception e) {
                                println("Some error occurred..", 7);
                                openMainMenu();
                                return;
                            }
                        }
                        else {
                            printLines(1);
                            println("Deletion aborted!",0);
                        }

                        break;

                    }

                    case "2":  {

                        printLines(2);
                        println("Are you sure you want to Deactivate your account??", 0);
                        println("1. Deactivate Account", 3);
                        println("Any other -> Cancel", 3);

                        int confirmation = scanner.nextInt();

                        if(confirmation==1) {

                            println("Enter your password to confirm: ", 4);
                            String password = scanner.next();
                            scanner.nextLine();

                            try {
                                if (DatabaseIO.deactivateUser(password)) {
                                    CurrentUser.logOut();

                                    println("Account deactivated successfully.", 6);
                                    println("Log in again to activate your account.", 6);
                                }
                                else
                                    println("Wrong password.", 6);
                            } catch (Exception e) {
                                println("Some error occurred..", 6);
                                openMainMenu();
                                return;
                            }
                        }
                        else {
                            printLines(1);
                            println("Deactivation aborted!",0);
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

                println("👋 Logging out...", 2);
                CurrentUser.logOut();

                openLoginMenu();

                break;

            }


            case "10": {

                println("Exiting...", 6);
                System.exit(0);


            }

            default: {

                println("❌ Invalid input.", 7);
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
        println("1. -> Try again", 5);
        println("Any other. -> Go back", 5);

        print("Enter your choice: ", 4);
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
