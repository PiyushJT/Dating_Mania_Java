package util;

import java.util.ArrayList;

import model.Hobby;
import model.Song;
import model.User;

public class Profile {

    public static void display(User user, ArrayList<Hobby> hobbies, ArrayList<Song> songs) {
        System.out.println("===============================================");
        System.out.println("         util.Profile of " + user.getName());
        System.out.println("===============================================");
        System.out.println("Bio: ");
        System.out.println("  " + user.getBio());
        System.out.println("Gender: " + user.getGender() + "  |  Age: " + user.getAge());
        System.out.println("City: " + user.getCity());
        System.out.println();
        System.out.println("Hobbies:");
        for (Hobby h : hobbies) {
            System.out.println("  - " + h.getHobbyName());
        }
        System.out.println();
        System.out.println("model.Song interests:");
        for (Song s : songs) {
            System.out.println("  - " +s.getSongName()+" BY "+s.getArtistName());
        }
        System.out.println("===============================================");
    }

    public static void displayHobbies(User user, ArrayList<Hobby> theirHobbies)
    {
        System.out.println("===============================================");
        System.out.println("         util.Profile of " + user.getName());
        System.out.println("===============================================");
        System.out.println("Bio: ");
        System.out.println("  " + user.getBio());
        System.out.println("Gender: " + user.getGender() + "  |  Age: " + user.getAge());
        System.out.println("City: " + user.getCity());
        System.out.println();
        System.out.println("Hobbies:");
        for (Hobby h : theirHobbies) {
            System.out.println("  - " + h.getHobbyName());
        }
        System.out.println();

    }

    public static void displaySongs(User user, ArrayList<Song> theirSongs)
    {
        System.out.println("===============================================");
        System.out.println("         util.Profile of " + user.getName());
        System.out.println("===============================================");
        System.out.println("Bio: ");
        System.out.println("  " + user.getBio());
        System.out.println("Gender: " + user.getGender() + "  |  Age: " + user.getAge());
        System.out.println("City: " + user.getCity());
        System.out.println();
        System.out.println("Songs :");
        for (Song s : theirSongs) {
            System.out.println("  - " + s.getSongName());
        }
        System.out.println();

    }

}
