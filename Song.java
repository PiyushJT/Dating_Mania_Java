import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Song {

    static ArrayList<Song> songs;

    static {

        try {
            songs = DatabaseIO.getAllSongs();
        }
        catch (SQLException e) {
            songs = new ArrayList<>();
        }

    }


    int songId;
    String songName;
    String songUrl;
    String artistName;
    String type;

    public Song(
            int songId, String songName,
            String songUrl, String artistName,
            String type
    ) {
        this.songId = songId;
        this.songName = songName;
        this.songUrl = songUrl;
        this.artistName = artistName;
        this.type = type;
    }

    public static Song fromDB(ResultSet rs) throws SQLException {

        int songID = rs.getInt("song_id");
        String songName = rs.getString("song_name");
        String songUrl = rs.getString("song_url");
        String artistName = rs.getString("artist_name");
        String type = rs.getString("type_name");

        return new Song(songID, songName, songUrl, artistName, type);

    }



    @Override
    public String toString() {
        return String.format("%-2d %-28s %-27s %-50s", songId, songName, artistName, songUrl);
    }


}