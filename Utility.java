import java.security.spec.RSAOtherPrimeInfo;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.function.DoubleBinaryOperator;

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
        System.out.println("10. Log out");
        System.out.println("11. Exit");

        System.out.print("Enter your choice: ");

        String choice = scanner.next();
        scanner.nextLine();

        switch (choice) {
            case "1":
                System.out.println("Update hobbies");

                if (CurrentUser.hobbies.isEmpty()) {
                    System.out.println("You have no hobbies.");
                }
                else {

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

                    outer: while (true) {

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

                            }
                            catch (NumberFormatException e) {
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

                openMainMenu();

                break;


            case "2":
                System.out.println("Update song interests");

                if (CurrentUser.songs.isEmpty()) {
                    System.out.println("You have no song Interests.");
                }
                else {

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

                    outer: while (true) {

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

                            }
                            catch (NumberFormatException e) {
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

                openMainMenu();

                break;


            case "3":

                for (Song song : CurrentUser.songs)
                    System.out.println(song);

                for (Hobby hobby : CurrentUser.hobbies)
                    System.out.println(hobby);

                openMainMenu();

                break;

            case "11":
                System.out.println("Exiting...");
                System.exit(0);

            default:
                System.out.println("Invalid choice.");
                openMainMenu();
                break;

        }

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