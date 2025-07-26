import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Song {

    static ArrayList<Song> songs = new ArrayList<>();
    static Map<Integer, Song> song = new HashMap<>();

    static {
        songs.add(new Song(1, "Blinding Lights", "https://www.youtube.com/watch?v=4NRXx6U8ABQ", "The Weeknd", "Pop"));
        songs.add(new Song(2, "Shape of You", "https://www.youtube.com/watch?v=JGwWNGJdvx8", "Ed Sheeran", "Pop"));
        songs.add(new Song(3, "Anti-Hero", "https://www.youtube.com/watch?v=b1kbLwvqugk", "Taylor Swift", "Pop"));
        songs.add(new Song(4, "As It Was", "https://www.youtube.com/watch?v=H5v3kku4y6Q", "Harry Styles", "Pop"));
        songs.add(new Song(5, "Levitating", "https://www.youtube.com/watch?v=TUVcZfQe-Kw", "Dua Lipa", "Pop"));

        songs.add(new Song(6, "Bohemian Rhapsody", "https://www.youtube.com/watch?v=fJ9rUzIMcZQ", "Queen", "Rock"));
        songs.add(new Song(7, "Stairway to Heaven", "https://www.youtube.com/watch?v=QkF3oxziUI4", "Led Zeppelin", "Rock"));
        songs.add(new Song(8, "Hotel California", "https://www.youtube.com/watch?v=BciS5krYL80", "Eagles", "Rock"));
        songs.add(new Song(9, "Sweet Child O Mine", "https://www.youtube.com/watch?v=1w7OgIMMRc4", "Guns N Roses", "Rock"));
        songs.add(new Song(10, "Smells Like Teen Spirit", "https://www.youtube.com/watch?v=hTWKbfoikeg", "Nirvana", "Rock"));

        songs.add(new Song(11, "God's Plan", "https://www.youtube.com/watch?v=xpYcfZOzFfM", "Drake", "Hip Hop"));
        songs.add(new Song(12, "HUMBLE.", "https://www.youtube.com/watch?v=tvTRZJ-4EyI", "Kendrick Lamar", "Hip Hop"));
        songs.add(new Song(13, "Lose Yourself", "https://www.youtube.com/watch?v=_Yhyp-_hX2s", "Eminem", "Hip Hop"));
        songs.add(new Song(14, "Sicko Mode", "https://www.youtube.com/watch?v=60NR7rH3MdK", "Travis Scott", "Hip Hop"));
        songs.add(new Song(15, "Old Town Road", "https://www.youtube.com/watch?v=7oyqOFaFGQk", "Lil Nas X", "Hip Hop"));

        songs.add(new Song(16, "Titanium", "https://www.youtube.com/watch?v=JRfuAukYTKg", "David Guetta ft. Sia", "Electronic/EDM"));
        songs.add(new Song(17, "Levels", "https://www.youtube.com/watch?v=_ovdm2yX4MA", "Avicii", "Electronic/EDM"));
        songs.add(new Song(18, "Bangarang", "https://www.youtube.com/watch?v=YJVmu6yttiw", "Skrillex", "Electronic/EDM"));
        songs.add(new Song(19, "One More Time", "https://www.youtube.com/watch?v=FGBhQbmPwH8", "Daft Punk", "Electronic/EDM"));
        songs.add(new Song(20, "Clarity", "https://www.youtube.com/watch?v=IxxstCcJlsc", "Zedd ft. Foxes", "Electronic/EDM"));

        songs.add(new Song(21, "Mr. Brightside", "https://www.youtube.com/watch?v=gGdGFtwCNBE", "The Killers", "Indie"));
        songs.add(new Song(22, "Somebody That I Used to Know", "https://www.youtube.com/watch?v=8UVNT4wvIGY", "Gotye", "Indie"));
        songs.add(new Song(23, "Pumped Up Kicks", "https://www.youtube.com/watch?v=SDTZ7iX4vTQ", "Foster the People", "Indie"));
        songs.add(new Song(24, "Take Me Out", "https://www.youtube.com/watch?v=Ijk4j-r7qPA", "Franz Ferdinand", "Indie"));
        songs.add(new Song(25, "Seven Nation Army", "https://www.youtube.com/watch?v=0J2QdDbelmY", "The White Stripes", "Indie"));

        songs.add(new Song(26, "Fly Me to the Moon", "https://www.youtube.com/watch?v=6bixhblJOE5E", "Frank Sinatra", "Jazz"));
        songs.add(new Song(27, "What a Wonderful World", "https://www.youtube.com/watch?v=VqhCQZaH4Vs", "Louis Armstrong", "Jazz"));
        songs.add(new Song(28, "The Girl from Ipanema", "https://www.youtube.com/watch?v=UJkxFhFRFDA", "Stan Getz & Astrud Gilberto", "Jazz"));
        songs.add(new Song(29, "Feeling Good", "https://www.youtube.com/watch?v=D5Y11hwjMNs", "Nina Simone", "Jazz"));
        songs.add(new Song(30, "Summertime", "https://www.youtube.com/watch?v=MIDOmSQJanU", "Ella Fitzgerald", "Jazz"));
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

    public static ArrayList<Song> songQuiz() {

        ArrayList<Integer> songInd = new ArrayList<>();
        ArrayList<Song> songs = new ArrayList<>();

        while(songInd.size()<15) {

            int ind = (int) (Math.random() * 15);

            if (!songInd.contains(ind))
                songInd.add(ind);

        }

        for (int i : songInd)
            songs.add(song.get(i));


        return songs;
    }


    @Override
    public String toString() {
        return String.format("%-2d %-28s %-27s %-50s", songId, songName, artistName, songUrl);
    }


}