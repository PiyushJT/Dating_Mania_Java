package service;

import java.util.*;

import ds.*;
import model.*;
import session.CurrentUser;
import db.DatabaseIO;
import logs.Log;

public class Matchmaking {

    public static PriorityQueue<UserMatch> matchMadeUsingHobby() {
        PriorityQueue<UserMatch> queue = new PriorityQueue<>();

            try {
                int myId = CurrentUser.data.getId();
                char myGender = CurrentUser.data.getGender();
                HobbyLinkedList myHobbiesList = DatabaseIO.getHobbiesFromUID(myId);
                HashSet<Integer> myHobbyIds = new HashSet<>();
                for (Hobby h : myHobbiesList.toArray()) {
                    myHobbyIds.add(h.getHobbyId());
                }

                // Get all users
                ArrayList<User> users = DatabaseIO.getUsers();
                for (User user : users) {
                    // Skip self and same gender or inactive
                    if (user.getId() == myId)
                        continue;
                    if (!user.isActive() || user.getGender() == myGender)
                        continue;
                    if (DatabaseIO.amIBlockedBy(user.getId()))
                        continue;

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
                        queue.add(new UserMatch(user, shared));
                    }
                }
            }
            catch (Exception e) {
                Log.E("Error matching by hobby: " + e.getMessage());
            }

        return queue;
    }


    public static PriorityQueue<UserMatch> matchMadeUsingSong() {
        PriorityQueue<UserMatch> queue = new PriorityQueue<>();

        try {
            int myId = CurrentUser.data.getId();
            char myGender = CurrentUser.data.getGender();
            SongLinkedList mySongsList = DatabaseIO.getSongsFromUID(myId);
            HashSet<Integer> mySongIds = new HashSet<>();
            for (Song s :  mySongsList.toArray())
                mySongIds.add(s.getSongId());

            // Get all users
            ArrayList<User> users = DatabaseIO.getUsers();
            for (User user : users) {
                // Skip self and same gender or inactive
                if (user.getId() == myId)
                    continue;
                if (!user.isActive() || user.getGender() == myGender)
                    continue;

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
                    queue.add(new UserMatch(user, shared));
                }
            }
        } catch (Exception e) {
            Log.E("Error matching by song: " + e.getMessage());
        }

        return queue;
    }

}