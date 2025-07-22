import java.util.ArrayList;

public class Profile {
    public static void display(User user, ArrayList<Hobby> hobbies, ArrayList<Song> songs) {
        System.out.println("===============================================");
        System.out.println("         Profile of " + user.getName());
        System.out.println("===============================================");
        System.out.println("Bio: ");
        System.out.println("  " + user.getBio());
        System.out.println("Gender: " + user.getGender() + "  |  Age: " + user.getAge());
//        System.out.println("Phone: " + user.getPhone());
//        System.out.println("Email: " + user.getEmail());
        System.out.println("City: " + user.getCity());
        System.out.println();
        System.out.println("Hobbies:");
        for (Hobby h : hobbies) {
            System.out.println("  - " + h.getHobbyName());
        }
        System.out.println();
        System.out.println("Song interests:");
        for (Song s : songs) {
            System.out.println("  - " +s.songName+" BY "+s.artistName);
        }
        System.out.println("===============================================");
    }

    public static void display(User user, ArrayList<Hobby> theirHobbies)
    {
        System.out.println("===============================================");
        System.out.println("         Profile of " + user.getName());
        System.out.println("===============================================");
        System.out.println("Bio: ");
        System.out.println("  " + user.getBio());
        System.out.println("Gender: " + user.getGender() + "  |  Age: " + user.getAge());
//        System.out.println("Phone: " + user.getPhone());
//        System.out.println("Email: " + user.getEmail());
        System.out.println("City: " + user.getCity());
        System.out.println();
        System.out.println("Hobbies:");
        for (Hobby h : theirHobbies) {
            System.out.println("  - " + h.getHobbyName());
        }
        System.out.println();

    }
}
