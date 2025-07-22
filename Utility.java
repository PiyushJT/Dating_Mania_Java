import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import javax.swing.Icon;
import javax.swing.SwingConstants;

public class Utility {

    static Scanner scanner = new Scanner(System.in);


    // Functions to print lines and tabs
    static void printLines(int lines) {

        for (int i = 1; i <= lines; i++)
            System.out.println();

    }

    static void printTabs(int tabs) {

        for (int i = 1; i <= tabs; i++)
            System.out.print("\t");

    }


    // Functions to print welcome screen
    static void printWelcome() {
        printLines(2);
        System.out.println("||======================================||");
        System.out.print("||");
        printTabs(2);
        System.out.print("Welcome to the Dating App");
        printTabs(2);
        System.out.println("||");
        System.out.println("||======================================||");
        printLines(2);

    }


    // Function to open login menu
    static void openLoginMenu() {

        System.out.println("1. Login");
        System.out.println("2. Register (Don't have an account)");
        System.out.println("3. Exit");

        System.out.print("Enter your choice: ");

        char choice = scanner.next().charAt(0);
        scanner.nextLine();


        // Switch statement to handle user input
        switch (choice) {

            case '1':
                if (login()) {
                    System.out.println("Welcome back " + CurrentUser.data.getName() + ".");
                    Log.S("User logged in successfully");
                    openMainMenu();
                }
                else {
                    System.out.println("Login aborted.");
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
    static boolean register() {

        // Prompt for user details

        String name;

        while (true) {

            System.out.print("Enter your name: ");
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

            System.out.print("Enter bio: ");
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

            System.out.print("Enter your gender (m/f): ");
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

            System.out.print("Enter your age: ");
            age = scanner.nextInt();
            scanner.nextLine();

            if (age < 18 || age > 100) {
                System.out.println("Your age is not suitable for this app. Try again");
                continue;
            }
            break;

        }


        long phone;

        while (true) {

            System.out.print("Enter your phone: ");
            phone = scanner.nextLong();
            scanner.nextLine();

            if (phone < 1000000000 || phone > 9999999999L) {
                System.out.println("Invalid phone number. Try again");
                continue;
            }
            break;

        }

        String city;

        while (true) {

            System.out.print("Enter your city: ");
            city = scanner.nextLine();

            if (city.isEmpty()) {
                System.out.println("City cannot be empty. Try again");
                continue;
            }
            break;

        }

        String email;

        while (true) {

            System.out.print("Enter your email: ");
            email = scanner.next();
            scanner.nextLine();

            if (!isEmailValid(email)) {
                System.out.println("Invalid email format. Try again");
                continue;
            }
            break;

        }


        String password;

        while (true) {

            System.out.print("Enter your password (min 8 chars): ");
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
            CurrentUser.data = user;
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
    static boolean login() {

        // Initialize variables
        String email = "";
        String password = "";


        // Loop until valid email and password are valid
        while (true) {

            // Getting user input
            System.out.print("Enter email: ");
            email = scanner.next();
            scanner.nextLine();
            System.out.print("Enter password: ");
            password = scanner.next();
            scanner.nextLine();


            // validating email and password
            if (!isEmailValid(email) || !isPasswordValid(password)) {
                System.out.println("Email or password is invalid.");

                // if user chooses to try again, loop continues
                if (tryAgain())
                    continue;
                else
                    return false;
            }
            if(!isAccountActive(email)) {
                System.out.println(("This Account is deactivated."));

                System.out.println("Would you like to reactivate your account?");

                // if user chooses to try again, loop continues
                if (tryAgain())
                    continue;
                else
                    return false;
            }
            break;

        }


        // Exception for debugging
        Exception exception = null;
        try {

            // Getting user data from database
            CurrentUser.data = DatabaseIO.getUserFromAuth(email, password);


            // If user data is null, user is not registered
            if (CurrentUser.data == null) {

                System.out.println("Wrong email or password.");

                // if user chooses to try again, function is called again
                if (tryAgain())
                    return login();

                return false;

            }


            // Method to add user_id to current user file
            CurrentUser.addCurrentUserToFile();

        }
        // Invalid email or password
        catch (Exception e) {
            exception = e;

            System.out.println("Email or password is invalid.");

            if (tryAgain())
                return login();

            return false;
        }
        finally {
            if (exception != null)
                exception.printStackTrace();
        }


        try {
            CurrentUser.initUserData();
        }
        catch (Exception e) {
            Log.E("Error initializing user data: " + e.getMessage());
        }
        return true;
    }


    static void openMainMenu() {

        System.out.println("1. Update hobbies");
        System.out.println("2. Update song interests");
        System.out.println("3. Create new match");
        System.out.println("4. Match requests");
        System.out.println("5. Open my profile");
        System.out.println("6. Block / unblock user");
        System.out.println("7. Delete / Deactivate account");
        System.out.println("8. Open chats");
        System.out.println("9. Log out");
        System.out.println("10. Exit");

        System.out.print("Enter your choice: ");

        String choice = scanner.next();
        scanner.nextLine();

        switch (choice) {

            case "1": {

                CurrentUser.updateHobbies();
                openMainMenu();

                break;
            }


            case "2": {

                CurrentUser.updateSongs();
                openMainMenu();

                break;

            }


            case "4": {

                openMatchRequests();
                break;

            }


            case "5": {

                System.out.println("My Profile");

                showProfile(CurrentUser.data);

                // Todo: friends / matches


                System.out.println("1. Edit profile");
                System.out.println("Any other -> Go Back");

                System.out.print("Enter your choice: ");
                String choice2 = scanner.next();
                scanner.nextLine();

                if (choice2.equals("1"))
                    CurrentUser.editProfile();
                else
                    openMainMenu();


                break;
            }


            case "6": {

                System.out.println("Block / unblock user");

                    System.out.println("1. Block");
                    System.out.println("2. Unblock");
                    System.out.println("Any other -> Back");

                    System.out.print("Enter your choice: ");
                    String choice2 = scanner.next();
                    scanner.nextLine();

                switch (choice2) {

                    case "1": {

                        System.out.println("Block User");


                        System.out.println("Enter user's name to block: ");
                        String name = scanner.next();
                        scanner.nextLine();

                        for (User user : User.users)
                            if (user.getName().toLowerCase().contains(name))
                                System.out.println(user);

                        System.out.println("Enter user id to block: ");
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

                        ArrayList<User> blockedUsers;

                        try {
                            blockedUsers = DatabaseIO.getBlockedUsers(CurrentUser.data.userId);
                        }
                        catch (Exception e) {
                            Log.E("Error getting blocked users: " + e.getMessage());
                            break;
                        }

                        if (blockedUsers.isEmpty())
                            break;

                        for (User user : blockedUsers)
                            System.out.println(user);

                        System.out.println("Enter user id to unblock: ");
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


            case "7": {

                System.out.println("Delete / Deactivate account");

                System.out.println("1. Delete account");
                System.out.println("2. Deactivate account");

                System.out.println("Any other -> Cancel");

                System.out.print("Enter your choice: ");

                String choice2 = scanner.next();
                scanner.nextLine();

                switch (choice2) {

                    case "1": {

                        System.out.println("Delete account");

                        System.out.println("Enter your password to confirm: ");
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

                        System.out.println("Enter your password to confirm: ");
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

    static void showProfile(User user) {


        System.out.println("Name: ");
        printTabs(1);
        System.out.print(user.getName());
        printLines(1);

        System.out.println("Bio: ");
        printTabs(1);
        System.out.print(user.getBio());
        printLines(1);

        System.out.println("Gender: ");
        printTabs(1);
        System.out.print(user.getGender());
        printLines(1);

        System.out.println("Age: ");
        printTabs(1);
        System.out.print(user.getAge());
        printLines(1);

        System.out.println("Phone: ");
        printTabs(1);
        System.out.print(user.getPhone());
        printLines(1);

        System.out.println("Email: ");
        printTabs(1);
        System.out.print(user.getEmail());
        printLines(1);

        System.out.println("City: ");
        printTabs(1);
        System.out.print(user.getCity());
        printLines(1);


        try {
            ArrayList<Hobby> hobbies = DatabaseIO.getHobbiesFromUID(user.userId);

            System.out.println("Hobbies: ");
            for (Hobby hobby : hobbies)
                System.out.println("\t" + hobby.getHobbyName());
            printLines(1);


            ArrayList<Song> songs = DatabaseIO.getSongsFromUID(user.userId);

            System.out.println("Song interests: ");
            for (Song song : songs)
                System.out.println("\t" + song);
            printLines(1);

        }
        catch (Exception e) {
            Log.E("Error getting hobbies and Songs: " + e.getMessage());
        }


    }

    static void openMatchRequests() {

        System.out.println("Match requests: ");

        ArrayList<Match> matches;
        try {
            matches = DatabaseIO.getMatchesByUid(CurrentUser.data.userId);
        }
        catch (SQLException e) {
            Log.E("Error getting matches: " + e.getMessage());
            return;
        }

        for (int i = 0; i < matches.size(); i++)
            System.out.println(matches.get(i));


        outer: while (true) {
            System.out.print("Select a match to accept or reject: ");

            String choice2 = scanner.next();
            scanner.nextLine();

            for (Match match : matches) {

                if (choice2.equals(match.senderUserId + "")) {

                    System.out.println("1. View Sender's Profile");
                    System.out.println("2. Accept");
                    System.out.println("3. Reject");
                    System.out.println("Any other -> Back");

                    System.out.print("Enter your choice: ");
                    String choice3 = scanner.next();
                    scanner.nextLine();

                    switch (choice3) {

                        case "1": {

                            System.out.println("Sender's Profile");

                            showProfile(match.sender);

                            openMatchRequests();
                            break;
                        }

                        case "2": {

                            try {
                                DatabaseIO.acceptMatch(match);
                            }
                            catch (SQLException e) {
                                Log.E("Error accepting match: " + e.getMessage());
                            }

                            openMatchRequests();
                            break;
                        }
                        case "3": {

                            try {
                                DatabaseIO.rejectMatch(match);
                            }
                            catch (SQLException e) {
                                Log.E("Error rejecting match: " + e.getMessage());
                            }

                            openMatchRequests();
                            break;
                        }

                        default: {
                            openMatchRequests();
                            break;
                        }

                    }

                    break outer;

                }

            }

            System.out.println("Invalid choice.");

            if (!Utility.tryAgain()) {
                break;
            }


        }

        openMainMenu();
    }


    // Functions to validate email and password
    static boolean isEmailValid(String email) {
        if (email == null || email.isEmpty())
            return false;
        return email.matches("[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}");
    }

    static boolean isPasswordValid(String password) {
        if (password == null)
            return false;
        return password.length() >= 8;
    }

    static boolean isAccountActive(String email)
    {
        boolean isActive = false;
        Exception exception = null;

        try {
            // Assuming DatabaseIO.connection is your open JDBC Connection
            String sql = "SELECT is_active FROM users WHERE email = ?";
            PreparedStatement pst = DatabaseIO.connection.prepareStatement(sql);
            pst.setString(1, email);

            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                isActive = rs.getBoolean("is_active"); // true = active, false = deactivated
            }
            rs.close();
            pst.close();
        } catch (Exception e) {
            exception = e;
            Log.E("Error checking account active status: " + e.getMessage());
        }
        return isActive;

    }


    // Function to print error message when log file is unaccessible
    static void printLogInaccessibleWarning() {
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


    static long getDateFromSQLDate(String dateStr) {

        return Long.parseLong(
                dateStr
                        .replace("-", "")
                        .replace(":", "")
                        .replace(" ", "")
                        .substring(0, 14)
        );

    }


    static String getDateString(long dateLong) {

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
    static boolean tryAgain() {
        System.out.println("Enter 1. -> Try again");
        System.out.println("Any other. -> Go back");

        System.out.print("Enter your choice: ");
        char choice = scanner.next().charAt(0);
        scanner.nextLine();
        return choice == '1';
    }

    static long getNowLong() {
        return Long.parseLong(
                LocalDateTime
                        .now()
                        .format(
                                DateTimeFormatter.ofPattern("yyyyMMddHHmmss")
                        )
        );
    }

}