import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

class SongRecommendationSystem {
    public SongRecommendationSystem() {
    }

    public List<Song> recommendByGenre(String genre) {
        List<Song> recommendedSongs = new ArrayList<>();
        for (Song song : Song.songList) {
            if (song.getGenre().equalsIgnoreCase(genre)) {
                recommendedSongs.add(song);
            }
        }
        return recommendedSongs;
    }

    public List<Song> recommendByArtist(String artistName) {
        List<Song> artistSongs = new ArrayList<>();
        for (Song song : Song.songList) {
            if (song.getArtistName().equalsIgnoreCase(artistName)) {
                artistSongs.add(song);
            }
        }
        return artistSongs;
    }

    public List<Song> recommendBySimilarGenre(String songName) {
        String genre = getGenreBySongName(songName);
        List<Song> similarGenreSongs = new ArrayList<>();
        for (Song song : Song.songList) {
            if (song.getGenre().equalsIgnoreCase(genre) && !song.getTrackName().equalsIgnoreCase(songName)) {
                similarGenreSongs.add(song);
            }
        }
        return similarGenreSongs;
    }

    private String getGenreBySongName(String songName) {
        for (Song song : Song.songList) {
            if (song.getTrackName().equalsIgnoreCase(songName)) {
                return song.getGenre();
            }
        }
        return null;
    }

    public List<Song> recommendRandom() {
        List<Song> randomSongs = new ArrayList<>(Song.songList);
        Collections.shuffle(randomSongs);
        return randomSongs.subList(0, Math.min(25, randomSongs.size()));
    }
}