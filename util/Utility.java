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

                "\u001B[30m", // Black
                "\u001B[31m", // Red
                "\u001B[32m", // Green
                "\u001B[33m", // Yellow
                "\u001B[34m", // Blue
                "\u001B[35m", // Purple
                "\u001B[36m", // Cyan
                "\u001B[37m"  // White

        };

        System.out.println(colors[color] + str + "\u001B[0m");

    }




    // Function to open login menu
    public static void openLoginMenu() {

        System.out.println("1. Login");
        System.out.println("2. Register (Don't have an account)");
        System.out.println("3. Exit");

        Utility.print("Enter your choice: ", 4);

        char choice = scanner.next().charAt(0);
        scanner.nextLine();


        // Switch statement to handle user input
        switch (choice) {

            case '1':
                if (login()) {
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

            case '2':
                if (register()) {
                    System.out.println("Registration successful! Welcome, \n" + CurrentUser.data.getName() + ".");
                    Log.S("User registered successfully");
                    openMainMenu();
                }
                else {
                    System.out.println("Registration aborted.");
                    Log.S("User registration aborted");
                    openLoginMenu();
                }

                break;

            case '3':
                System.out.println("Exiting...");
                Log.S("User exited manually");
                System.exit(0);

                break;

            default:
                System.out.println("Invalid choice.");
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
                System.out.println("Invalid email format. Try again");
                continue;
            }

            try {
                if (DatabaseIO.isEmailPhoneDupe(email)) {
                    System.out.println("Email already exists. Try again");
                    continue;
                }
            }
            catch (SQLException e) {
                System.out.println("Error checking for duplicate email. Trying again");
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
                System.out.println("Invalid phone number. Try again");
                continue;
            }
            phone = Long.parseLong(strPhone);

            try {
                if (DatabaseIO.isEmailPhoneDupe(phone + "")) {
                    System.out.println("Phone already exists. Try again");
                    continue;
                }
            }
            catch (SQLException e) {
                System.out.println("Error checking for duplicate phone. Trying again");
                continue;
            }

            break;

        }


        String name;

        while (true) {

            Utility.print("Enter your name: ", 4);
            name = scanner.nextLine();
            if (name.length() > 40) {
                System.out.println("Name too long. Try a shorter name");
                continue;
            }
            if (name.equals("")) {
                System.out.println("Name cannot be empty. Try again");
                continue;
            }
            break;
        }

        String bio;

        while (true) {

            Utility.print("Enter bio: ", 4);
            bio = scanner.nextLine();
            if (bio.length() > 100) {
                System.out.println("Bio too long. Try a shorter bio");
                continue;
            }
            if (bio.equals("")) {
                System.out.println("bio cannot be empty. Try again");
                continue;
            }
            break;
        }


        char gender;

        while (true) {

            Utility.print("Enter your gender (m/f): ", 4);
            gender = scanner.next().toLowerCase().charAt(0);
            scanner.nextLine();

            if (gender != 'm' && gender != 'f') {
                System.out.println("Your gender is not suitable for this app. Try again");
                continue;
            }

            break;

        }

        int age;
        while (true) {

            Utility.print("Enter your age: ", 4);
            age = scanner.nextInt();
            scanner.nextLine();

            if (age < 18 || age > 100) {
                System.out.println("Your age is not suitable for this app. Try again");
                continue;
            }
            break;

        }


        String city;

        while (true) {

            Utility.print("Enter your city: ", 4);
            city = scanner.nextLine();

            if (city.isEmpty()) {
                System.out.println("City cannot be empty. Try again");
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
                System.out.println("Invalid password format. Try again");
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
                System.out.println("Invalid Credentials");

                // if user chooses to try again, loop continues
                if (tryAgain())
                    continue;
                else
                    return false;
            }
            if( !DatabaseIO.isAccountActive(emailPhone) ) {

                System.out.println(("This Account is deactivated."));
                System.out.println("Enter y to reactivate your account.");
                Utility.println("Enter anything else to cancel and go back", 4);

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
                    System.out.println("Account not found.");
                else
                    System.out.println("Wrong Credentials.");

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
            System.out.println("Invalid Credentials.");

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


    public static void openMainMenu() {

        if(CurrentUser.hobbies.isEmpty())
            System.out.println(" 1. Register your hobbies");
        else
            System.out.println(" 1. Update hobbies");


        if(CurrentUser.songs.isEmpty())
            System.out.println(" 2. Take the song quiz");
        else
            System.out.println(" 2. Retake song quiz");

        System.out.println(" 3. View your matches");
        System.out.println(" 4. Create new match");
        System.out.println(" 5. Match requests");
        System.out.println(" 6. Open my profile");
        System.out.println(" 7. Block / unblock user");
        System.out.println(" 8. Delete / Deactivate account");
        System.out.println(" 9. Log out");
        System.out.println("10. Exit");

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

            case "3": {


                printLines(2);

                System.out.println("Your matches");

                UserLinkedList matches = new UserLinkedList();
                try {
                    matches = DatabaseIO.getMatches();
                }
                catch (SQLException e) {
                    Log.E("Error getting matches: " + e.getMessage());
                }

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

                    printLines(2);

                }


                openMainMenu();
                break;

            }

            case "4": {

                System.out.println("BY Hobby / Song");

                System.out.println("1. Match by Hobby");
                System.out.println("2. Match by Song");

                System.out.println("Any other -> Cancel");

                Utility.print("Enter your choice: ", 4);

                String choice2 = scanner.next();
                scanner.nextLine();

                switch (choice2) {

                    case "1": {

                        BST matches = Matchmaking.matchMadeUsingHobby();

                        if (matches.isEmpty()) {
                            System.out.println("No matches found based on shared hobbies.");
                        } else {
                            Scanner scanner = new Scanner(System.in);

                            while (!matches.isEmpty()) {
                                User potentialMatch = matches.pollMax();

                                try {
                                    // Display profile using your Profile class
                                    HobbyLinkedList theirHobbies = DatabaseIO.getHobbiesFromUID(potentialMatch.getId());

                                    Profile.displayHobbies(potentialMatch, theirHobbies);
                                    System.out.println();
                                } catch (Exception e) {
                                    System.out.println("⚠️ Couldn't load full profile for this user.");
                                    continue;
                                }

                                System.out.println("Swipe [r] to match ✅, [l] to skip ⛔, or [q] to quit:");

                                boolean validInput = false;
                                while (!validInput) {
                                    System.out.print("Your choice (r/l/q): ");
                                    String input = scanner.nextLine().trim().toLowerCase();

                                    switch (input) {
                                        case "r":
                                            try {
                                                DatabaseIO.sendMatchRequest(potentialMatch.getId(), "hobby");
                                                System.out.println("✅ Match request sent to " + potentialMatch.getName() + "!");
                                                System.out.println();
                                            } catch (Exception e) {
                                                System.out.println("❌ Failed to send request. Please try again later.");
                                            }
                                            validInput = true;
                                            break;

                                        case "l":
                                            System.out.println("⛔ Skipped " + potentialMatch.getName() + ".");
                                            System.out.println();
                                            validInput = true;
                                            break;

                                        case "q":
                                            System.out.println("🚪 Exiting matchmaking.");
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

                    case "2":  {

                        BST matches = Matchmaking.matchMadeUsingSong();

                        if (matches.isEmpty()) {
                            System.out.println("No matches found based on shared songs.");
                        }
                        else {
                            Scanner scanner = new Scanner(System.in);

                            while (!matches.isEmpty()) {
                                User potentialMatch = matches.pollMax();

                                try {
                                    SongLinkedList theirSongs = DatabaseIO.getSongsFromUID(potentialMatch.getId());

                                    Profile.displaySongs(potentialMatch, theirSongs);
                                    System.out.println();
                                }
                                catch (Exception e) {
                                    System.out.println("⚠️ Couldn't load full profile for this user.");
                                    continue;
                                }

                                System.out.println("Swipe [r] to match ✅, [l] to skip ⛔, or [q] to quit:");

                                boolean validInput = false;
                                while (!validInput) {
                                    System.out.print("Your choice (r/l/q): ");
                                    String input = scanner.nextLine().trim().toLowerCase();

                                    switch (input) {
                                        case "r":
                                            try {
                                                DatabaseIO.sendMatchRequest(potentialMatch.getId(), "song");
                                                System.out.println("✅ Match request sent to " + potentialMatch.getName() + "!");
                                                System.out.println();
                                            } catch (Exception e) {
                                                System.out.println("❌ Failed to send request. Please try again later.");
                                            }
                                            validInput = true;
                                            break;

                                        case "l":
                                            System.out.println("⛔ Skipped " + potentialMatch.getName() + ".");
                                            System.out.println();
                                            validInput = true;
                                            break;

                                        case "q":
                                            System.out.println("🚪 Exiting matchmaking.");
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

                System.out.println("Match requests: ");

                MatchLinkedList matches;
                try {
                    matches = DatabaseIO.getMatchesByUid(CurrentUser.data.getUserId());
                }
                catch (SQLException e) {
                    Log.E("Error getting matches: " + e.getMessage());
                    break;
                }

                for (int i = 0; i < matches.length(); i++)
                    System.out.println(matches.get(i));


                outer: while (true) {
                    System.out.print("Select a match to accept or reject: ");

                    String choice2 = scanner.next();
                    scanner.nextLine();

                    for (Match match : matches.toArray()) {

                        if (choice2.equals(match.getSenderUserId() + "")) {

                            System.out.println("Accept match?");
                            System.out.println("Enter y to accept");
                            Utility.println("Enter anything else to reject: ", 4);
                            String ch = scanner.next();


                            try {

                                if (ch.equalsIgnoreCase("y"))
                                    DatabaseIO.acceptMatch(match);
                                else
                                    DatabaseIO.rejectMatch(match);

                            }
                            catch (SQLException e) {
                                Log.E("Error accepting / Rejecting match: " + e.getMessage());
                            }

                            break outer;

                        }

                    }

                    System.out.println("Invalid choice.");

                    if (!Utility.tryAgain()) {
                        break;
                    }

                }
                Utility.openMainMenu();
                break;
            }


            case "6": {


                System.out.println("My Profile");
                Profile.display(CurrentUser.data, CurrentUser.hobbies, CurrentUser.songs);

                System.out.println("1. Edit profile");
                System.out.println("Any other -> Go Back");

                Utility.print("Enter your choice: ", 4);
                String choice2 = scanner.nextLine();

                if (choice2.equals("1"))
                    CurrentUser.editProfile();

                openMainMenu();

                break;

            }

            case "7": {

                System.out.println("Block / unblock user");

                System.out.println("1. Block");
                System.out.println("2. Unblock");
                Utility.println("Any other -> Back", 4);

                Utility.print("Enter your choice: ", 4);
                String choice2 = scanner.next();
                scanner.nextLine();

                switch (choice2) {

                    case "1": {

                        System.out.println("Block User");

                        Utility.println("Enter user's name to block: ", 4);
                        String name = scanner.next();
                        scanner.nextLine();

                        for (User user : User.users.toArray())
                            if (user.getName().toLowerCase().contains(name))
                                System.out.println(user);

                        Utility.println("Enter user id to block: ", 4);
                        int uid = scanner.nextInt();
                        scanner.nextLine();

                        try {
                            DatabaseIO.blockUser(uid);
                        }
                        catch (SQLException e) {
                            Log.E("Error blocking user: " + e.getMessage());
                        }

                        break;
                    }

                    case "2": {

                        System.out.println("Unblock user");

                        UserLinkedList blockedUsers;

                        try {
                            blockedUsers = DatabaseIO.getBlockedUsers(CurrentUser.data.getUserId());
                        }
                        catch (Exception e) {
                            Log.E("Error getting blocked users: " + e.getMessage());
                            break;
                        }

                        if (blockedUsers.isEmpty())
                            break;

                        for (User user : blockedUsers.toArray())
                            System.out.println(user);

                        Utility.println("Enter user id to unblock: ", 4);
                        int uid = scanner.nextInt();

                        try {
                            DatabaseIO.unblockUser(uid);
                        }
                        catch (Exception e) {
                            Log.E("Error unblocking user: " + e.getMessage());
                        }

                        break;
                    }


                    default: {

                        System.out.println("Process canceled");
                        break;

                    }


                }

                openMainMenu();
                break;

            }


            case "8": {

                System.out.println("Delete / Deactivate account");

                System.out.println("1. Delete account");
                System.out.println("2. Deactivate account");

                System.out.println("Any other -> Cancel");

                Utility.print("Enter your choice: ", 4);

                String choice2 = scanner.next();
                scanner.nextLine();

                switch (choice2) {

                    case "1": {

                        System.out.println("Delete account");

                        Utility.println("Enter your password to confirm: ", 4);
                        String password = scanner.next();
                        scanner.nextLine();

                        try {
                            if (DatabaseIO.deleteUser(password)) {
                                CurrentUser.logOut();

                                System.out.println("Account deleted successfully.");
                            }
                            else
                                System.out.println("Wrong password.");
                        }
                        catch (Exception e) {
                            System.out.println("Some error occurred..");
                            openMainMenu();
                            return;
                        }

                        break;
                    }

                    case "2":  {

                        System.out.println("Deactivate account");

                        Utility.println("Enter your password to confirm: ", 4);
                        String password = scanner.next();
                        scanner.nextLine();

                        try {
                            if (DatabaseIO.deactivateUser(password)) {
                                CurrentUser.logOut();

                                System.out.println("Account deactivated successfully.");
                                System.out.println("Log in again to activate your account.");
                            }
                            else
                                System.out.println("Wrong password.");
                        }
                        catch (Exception e) {
                            System.out.println("Some error occurred..");
                            openMainMenu();
                            return;
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

                System.out.println("Logging out...");
                CurrentUser.logOut();

                openLoginMenu();

                break;

            }


            case "10": {

                System.out.println("Exiting...");
                System.exit(0);


            }

            default: {

                System.out.println("Invalid choice.");
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
        System.out.println("Enter 1. -> Try again");
        System.out.println("Any other. -> Go back");

        Utility.print("Enter your choice: ", 4);
        char choice = scanner.next().charAt(0);
        scanner.nextLine();
        return choice == '1';
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
