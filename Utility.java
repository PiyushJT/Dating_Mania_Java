import java.util.*;

public class Utility {

    static Scanner sc = new Scanner(System.in);

    static void printLines(int lines) {

        for (int i = 1; i <= lines; i++)
            System.out.println();

    }

    static void printTabs(int tabs) {

        for (int i = 1; i <= tabs; i++)
            System.out.print("\t");

    }

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

    static void openLoginMenu() {

        System.out.println("1. Register");
        System.out.println("2. Login");
        System.out.println("3. Exit");

        int choice = sc.nextInt();

    }

}