package util;

import ds.*;
import model.*;

public class Profile {

    public static void display(User user, HobbyLinkedList hobbies, SongLinkedList songs) {

        Utility.printLines(2);

        Utility.println("===============================================", 8);
        Utility.println(user.getName().toUpperCase() +" || User ID : "+ user.getUserId(),6);
        Utility.println("===============================================", 8);

        Utility.println("Bio: ", 6);
        Utility.println("  " + user.getBio(), 6);
        Utility.println("Gender: " + user.getGender() + "  |  🎂 Age: " + user.getAge(), 6);
        Utility.println("City: " + user.getCity(), 6);

        Utility.printLines(1);

        Utility.println("Hobbies:", 6);
        for (Hobby h : hobbies.toArray())
            Utility.println("  - " + h.getHobbyName(), 6);

        Utility.printLines(1);

        Utility.println("Song interests:", 6);

        for (Song s : songs.toArray())
            Utility.println("  - " + s.getSongName() + " BY " + s.getArtistName(), 6);


        Utility.println("===============================================", 8);


    }

        public static void display(User user, HobbyLinkedList hobbies) {

            Utility.printLines(2);

            Utility.println("===============================================", 8);
            Utility.println(user.getName().toUpperCase(), 6);
            Utility.println("===============================================", 8);

            Utility.println("Bio: ", 6);
            Utility.println("  " + user.getBio(), 6);
            Utility.println("Gender: " + user.getGender() + "  |  🎂 Age: " + user.getAge(), 6);
            Utility.println("City: " + user.getCity(), 6);

            Utility.printLines(1);

            Utility.println("Hobbies:", 6);
            for (Hobby h : hobbies.toArray())
                Utility.println("  - " + h.getHobbyName(), 6);


            Utility.println("===============================================", 8);

        }


    public static void display(User user, SongLinkedList songs)
    {

        Utility.printLines(2);

        Utility.println("===============================================", 8);
        Utility.println(user.getName().toUpperCase(), 6);
        Utility.println("===============================================", 8);

        Utility.println("Bio: ", 6);
        Utility.println("  " + user.getBio(), 6);
        Utility.println("Gender: " + user.getGender() + "  |  🎂 Age: " + user.getAge(), 6);
        Utility.println("City: " + user.getCity(), 6);

        Utility.printLines(1);

        Utility.println("Song interests:", 6);

        for (Song s : songs.toArray())
            Utility.println("  - " + s.getSongName() + " BY " + s.getArtistName(), 6);


        Utility.println("===============================================", 8);


    }


}
