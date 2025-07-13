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

        System.out.println("1. Register");
        System.out.println("2. Login");
        System.out.println("3. Exit");

        int choice = scanner.nextInt();

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

}