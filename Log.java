import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Log {

    // log file
    static File file = new File("Logs/system_history.log");;
    static BufferedWriter writer;


    // Error log
    public static void E(String msg) {
        push("Error", msg);
    }

    // Database log
    public static void DB(String msg) {
        push("Database", msg);
    }

    // System log
    public static void S(String msg) {
        push("System", msg);
    }



    // function to push log to file
    static void push(String type, String msg) {


        // create the file if it doesn't exist. And open it in append mode.
        try {
            if (!file.exists())
                file.createNewFile();

            writer = new BufferedWriter(
                    new FileWriter(file, true)
            );
        }
        catch (IOException e) {
            Utility.printLogInaccessibleError();
            return;
        }


        // get current timestamp
        String timestamp = LocalDateTime.now().format(
                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        );

        // format log
        String log = String.format("%-20s %-9s %-50s", timestamp, type, msg);

        // add log to file
        try {
            writer.write(log);
            writer.newLine();
            writer.close();
        }
        catch (IOException e) {
            Utility.printLogInaccessibleError();
        }

    }


}