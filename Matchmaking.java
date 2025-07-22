import java.sql.SQLException;
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
                if (user.getId() == myId) continue;
                if (!user.isActive || user.getGender() == myGender) continue;

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
        } catch (Exception e) {
            Log.E("Error matching by hobby: " + e.getMessage());
        }
        return queue;
    }

    public static void sendMatchRequest(int senderUserId, int receiverUserId) throws SQLException {
        // Insert a record in a match_requests or matches table as "pending"
        // Example SQL: INSERT INTO match_requests (sender_id, receiver_id, status) VALUES (?, ?, 'pending');
    }

}