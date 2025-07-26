import java.io.*;
import java.util.Scanner;

public class Chat {
    private static final String CHAT_FILE = "chat.txt";
    private static final int POLL_INTERVAL_MS = 1000;
    private String username;
    private static final Object lock = new Object();

    public Chat(String username) {
        this.username = username;
    }

    public void start() {
        System.out.println("\n--- Chat Mode ---");
        System.out.println("Type your message and press Enter. Type '/exit' to leave chat.\n");
        ReaderThread readerThread = new ReaderThread(this);
        readerThread.start();

        try (Scanner scanner = new Scanner(System.in)) {

            while (true) {
                String msg = scanner.nextLine();
                if (msg.trim().equalsIgnoreCase("/exit")) {
                    System.out.println("Exiting chat...");
                    break;
                }
                sendMessage(msg);
            }

        }

    }

    private void sendMessage(String message) {

        String line = String.format("[%s]: %s", username, message);


        try (FileWriter fw = new FileWriter(CHAT_FILE, true)) {
            fw.write(line + System.lineSeparator());

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    void readMessagesLoop() {

        long lastLength = 0;

        while (true) {

            try {

                File file = new File(CHAT_FILE);

                if (!file.exists()) {
                    Thread.sleep(POLL_INTERVAL_MS);
                    continue;
                }

                long fileLength = file.length();

                if (fileLength > lastLength) {

                    try (RandomAccessFile raf = new RandomAccessFile(file, "r")) {

                        raf.seek(lastLength);

                        String line;
                        while ((line = raf.readLine()) != null) {
                            System.out.println(line);
                        }

                    }

                    lastLength = fileLength;

                }

                Thread.sleep(POLL_INTERVAL_MS);

            }

            catch (Exception e) {
                System.err.println("Error reading chat: " + e.getMessage());
            }

        }
    }

    // Example usage
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter your username: ");
        String username = scanner.nextLine();
        Chat chat = new Chat(username);
        chat.start();
    }



}

class ReaderThread extends Thread {

    Chat chat;

    public ReaderThread(Chat chat) {
        this.chat = chat;
    }

    public void run() {
        chat.readMessagesLoop();
    }

}