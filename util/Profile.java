package util;

import ds.*;
import model.Hobby;
import model.Song;
import model.User;

public class Profile {

    public static void display(User user, HobbyLinkedList hobbies, SongLinkedList songs) {
        System.out.println("===============================================");
        System.out.println("👤 Profile of " + user.getName());
        System.out.println("===============================================");
        System.out.println("📖 Bio: ");
        System.out.println("  " + user.getBio());
        System.out.println("⚧ Gender: " + user.getGender() + "  |  🎂 Age: " + user.getAge());
        System.out.println("🏙️ City: " + user.getCity());
        System.out.println();
        System.out.println("🎨 Hobbies:");
        for (Hobby h : hobbies.toArray()) {
            System.out.println("  - " + h.getHobbyName());
        }
        System.out.println();
        System.out.println("🎶 Song interests:");
        for (Song s : songs.toArray()) {
            System.out.println("  - " + s.getSongName() + " BY " + s.getArtistName());
        }
        System.out.println("===============================================");
    }

        public static void displayHobbies(User user, HobbyLinkedList hobbies) {

            System.out.println("===============================================");
            System.out.println("👤 Profile of " + user.getName());
            System.out.println("===============================================");
            System.out.println("📖 Bio: ");
            System.out.println("  " + user.getBio());
            System.out.println("⚧ Gender: " + user.getGender() + "  |  🎂 Age: " + user.getAge());
            System.out.println("🏙️ City: " + user.getCity());
            System.out.println();
            System.out.println("🎨 Hobbies:");
            for (Hobby h : hobbies.toArray()) {
                System.out.println("  - " + h.getHobbyName());
            }
            System.out.println();

        }


    public static void displaySongs(User user, SongLinkedList songs)
    {

        System.out.println("===============================================");
        System.out.println("👤 Profile of " + user.getName());
        System.out.println("===============================================");
        System.out.println("📖 Bio: ");
        System.out.println("  " + user.getBio());
        System.out.println("⚧ Gender: " + user.getGender() + "  |  🎂 Age: " + user.getAge());
        System.out.println("🏙️ City: " + user.getCity());
        System.out.println();
        System.out.println("🎶 Songs:");
        for (Song s : songs.toArray()) {
            System.out.println("  - " + s.getSongName());
        }
        System.out.println();



    }


}
