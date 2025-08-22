package service;

import java.util.*;

import ds.*;
import model.*;
import session.CurrentUser;
import db.DatabaseIO;
import logs.Log;

public class Matchmaking {


    public static BST matchMadeUsingHobby() {

        BST queue = new BST();

            try {

                int myId = CurrentUser.data.getId();
                String myGender = CurrentUser.data.getGender();

                HobbyLinkedList myHobbiesList = DatabaseIO.getHobbiesFromUID(myId);

                HashSet<Integer> myHobbyIds = new HashSet<>();

                for (Hobby h : myHobbiesList.toArray()) {
                    myHobbyIds.add(h.getHobbyId());
                }

                // Get all users
                UserLinkedList users = DatabaseIO.getUsers();
                for (User user : users.toArray()) {
                    // Skip self and same gender or inactive
                    if (user.getId() == myId)
                        continue;
                    if (!user.isActive() || user.getGender().equalsIgnoreCase(myGender))
                        continue;
                    if (DatabaseIO.amIBlockedBy(user.getId()))
                        continue;

                    // Skip if already matched
                    if (DatabaseIO.hasExistingMatch(myId, user.getId()))
                        continue;


                    // todo: if already sent / received -> continue


                    // Get hobbies for this user
                    HobbyLinkedList theirHobbies = DatabaseIO.getHobbiesFromUID(user.getId());
                    HashSet<Integer> theirHobbyIds = new HashSet<>();
                    for (Hobby h : theirHobbies.toArray())
                        theirHobbyIds.add(h.getHobbyId());

                    // Calculate number of shared hobbies
                    HashSet<Integer> common = new HashSet<>(myHobbyIds);
                    common.retainAll(theirHobbyIds);
                    int shared = common.size();

                    if (shared > 0) {
                        queue.insert(user, shared);
                    }

                }
            }
            catch (Exception e) {
                Log.E("Error matching by hobby: " + e.getMessage());
            }

        return queue;
    }


    public static BST matchMadeUsingSong() {
        BST queue = new BST();

        try {
            int myId = CurrentUser.data.getId();
            String myGender = CurrentUser.data.getGender();
            SongLinkedList mySongsList = DatabaseIO.getSongsFromUID(myId);
            HashSet<Integer> mySongIds = new HashSet<>();
            for (Song s :  mySongsList.toArray())
                mySongIds.add(s.getSongId());

            // Get all users
            UserLinkedList users = DatabaseIO.getUsers();
            for (User user : users.toArray()) {
                // Skip self and same gender or inactive
                if (user.getId() == myId)
                    continue;
                if (!user.isActive() || user.getGender().equalsIgnoreCase(myGender))
                    continue;
                if (DatabaseIO.amIBlockedBy(user.getId()))
                    continue;

                // Skip if already matched
                if (DatabaseIO.hasExistingMatch(myId, user.getId()))
                    continue;

                // todo: if already sent / received -> continue

                // Get songs for this user
                SongLinkedList theirSong = DatabaseIO.getSongsFromUID(user.getId());
                HashSet<Integer> theirSongIds = new HashSet<>();

                for (Song s : theirSong.toArray())
                    theirSongIds.add(s.getSongId());

                // Calculate number of shared songs
                HashSet<Integer> common = new HashSet<>(mySongIds);
                common.retainAll(theirSongIds);
                int shared = common.size();

                if (shared > 0) {
                    queue.insert(user, shared);
                }
            }
        } catch (Exception e) {
            Log.E("Error matching by song: " + e.getMessage());
        }

        return queue;
    }

}