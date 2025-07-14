import java.util.*;

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
        System.out.println("2. Register (Don't have an account");
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
                register();

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


        return true;
    }


    // Function to open login menu
    static boolean login() {

        // Initialize variables
        String email = "";
        String password = "";


        // Loop until valid email and password are valid
        while (email.isEmpty() || password.isEmpty()) {

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
                    return false;

            }

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

}