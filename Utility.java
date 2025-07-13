import java.sql.Connection;
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

        System.out.println("1. Register (Don't have an account");
        System.out.println("2. Login (Have an account)");
        System.out.println("3. Exit");
        System.out.print("Enter your choice: ");

        char choice = scanner.next().charAt(0);
        scanner.nextLine();

        switch (choice) {
            case '1':
                register();
                break;
            case '2':
                login();
                break;
            case '3':
                System.out.println("Exiting...");
                System.exit(0);
                break;
        }


    }


    static boolean register() {


        return true;
    }


    static boolean login() {

        String email = "";
        String password = "";

        while (email.isEmpty() || password.isEmpty()) {
            System.out.print("Enter email: ");
            email = scanner.next();
            scanner.nextLine();
            System.out.print("Enter password: ");
            password = scanner.next();
            scanner.nextLine();

            if (!isEmailValid(email) || !isPasswordValid(password)) {
                System.out.println("Email or password is invalid.");

                System.out.println("Enter 1. -> Try again");
                System.out.println("Any other. -> Go back");

                System.out.print("Enter your choice: ");
                char choice = scanner.next().charAt(0);
                scanner.nextLine();

                if (choice != '1')
                    return false;

            }

        }

        Exception exception = null;
        try {

            CurrentUser.data = DatabaseIO.getUserFromAuth(email, password);

            // Todo: add current user uid to file

        }
        catch (Exception e) {
            exception = e;

            System.out.println("Email or password is invalid.");

            System.out.println("Enter 1. -> Try again");
            System.out.println("Any other. -> Go back");

            System.out.print("Enter your choice: ");
            char choice = scanner.next().charAt(0);
            scanner.nextLine();

            if (choice == '1')
                return login();

            return false;
        }
        finally {
            if (exception != null)
                exception.printStackTrace();
        }

        return true;
    }



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

}