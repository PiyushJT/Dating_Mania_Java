import java.util.*;
public class Matchmaking
{
    static PriorityQueue<UserMatch> matchMadeUsingHobby() {
        PriorityQueue<UserMatch> queue = new PriorityQueue<>();

            try {
                int myId = CurrentUser.data.getId();
                char myGender = CurrentUser.data.getGender();
                ArrayList<Hobby> myHobbiesList = DatabaseIO.getHobbiesFromUID(myId);
                HashSet<Integer> myHobbyIds = new HashSet<>();
                for (Hobby h : myHobbiesList) {
                    myHobbyIds.add(h.hobbyId);
                }

                // Get all users
                ArrayList<User> users = DatabaseIO.getUsers();
                for (User user : users) {
                    // Skip self and same gender or inactive
                    if (user.getId() == myId)
                        continue;
                    if (!user.isActive || user.getGender() == myGender)
                        continue;
                    if (DatabaseIO.amIBlockedBy(user.getId()))
                        continue;

                    // Get hobbies for this user
                    ArrayList<Hobby> theirHobbies = DatabaseIO.getHobbiesFromUID(user.getId());
                    HashSet<Integer> theirHobbyIds = new HashSet<>();
                    for (Hobby h : theirHobbies) {
                        theirHobbyIds.add(h.hobbyId);
                    }

                    // Calculate number of shared hobbies
                    HashSet<Integer> common = new HashSet<>(myHobbyIds);
                    common.retainAll(theirHobbyIds);
                    int shared = common.size();

                    if (shared > 0) {
                        queue.add(new UserMatch(user, shared));
                    }
                }
            }
            catch (Exception e) {
                Log.E("Error matching by hobby: " + e.getMessage());
            }

        return queue;
    }


    static PriorityQueue<UserMatch> matchMadeUsingSong() {
        PriorityQueue<UserMatch> queue = new PriorityQueue<>();

        try {
            int myId = CurrentUser.data.getId();
            char myGender = CurrentUser.data.getGender();
            ArrayList<Song> mySongsList = DatabaseIO.getSongsFromUID(myId);
            HashSet<Integer> mySongIds = new HashSet<>();
            for (Song s : mySongsList) {
                mySongIds.add(s.songId);
            }

            // Get all users
            ArrayList<User> users = DatabaseIO.getUsers();
            for (User user : users) {
                // Skip self and same gender or inactive
                if (user.getId() == myId)
                    continue;
                if (!user.isActive || user.getGender() == myGender)
                    continue;

                // Get songs for this user
                ArrayList<Song> theirSong = DatabaseIO.getSongsFromUID(user.getId());
                HashSet<Integer> theirSongIds = new HashSet<>();
                for (Song s : theirSong) {
                    theirSongIds.add(s.songId);
                }

                // Calculate number of shared songs
                HashSet<Integer> common = new HashSet<>(mySongIds);
                common.retainAll(theirSongIds);
                int shared = common.size();

                if (shared > 0) {
                    queue.add(new UserMatch(user, shared));
                }
            }
        } catch (Exception e) {
            Log.E("Error matching by song: " + e.getMessage());
        }

        return queue;
    }

}