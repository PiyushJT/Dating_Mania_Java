import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Log {

    static File file = new File("Logs/system_history.log");;
    static BufferedWriter writer;


    public static void E(String msg) {
        push("Error", msg);
    }

    public static void DB(String msg) {
        push("Database", msg);
    }

    public static void S(String msg) {
        push("System", msg);
    }




    static void push(String type, String msg) {

        try {
            if (!file.exists())
                file.createNewFile();

            writer = new BufferedWriter(
                    new FileWriter(file, true)
            );
        }
        catch (IOException e) {
            System.out.println("Log cannot be added as file cannot be opened.");
            return;
        }



        String timestamp = LocalDateTime.now().format(
                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        );

        String log = String.format("%-20s %-9s %-50s", timestamp, type, msg);

        try {
            writer.write(log);
            writer.newLine();
            writer.close();
        }
        catch (IOException e) {
            System.out.println("Log cannot be added as file cannot be opened.");
        }

    }


}