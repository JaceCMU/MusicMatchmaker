import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Random;

public class DatabaseSort {

	ObservableList<Song> parseDatabaseFile(String filePath) {
		ObservableList<Song> songList = FXCollections.observableArrayList();

		try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
			br.readLine(); // skip the header line
			String line;

			while ((line = br.readLine()) != null) {
				String[] data = line.split(",");
				if (data.length >= 4) {
					String songName = data[0];
					String artistName = data[1];
					String album = data[2];
					String genre = data[3].trim(); // Remove leading/trailing whitespace

					Song song = new Song(songName, artistName, album, genre, null);
					songList.add(song);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		return songList;
	}

	String getRandomSongWithSameGenre(ObservableList<Song> songList, String enteredSongName) {

		String enteredSongGenre = null;
		// method to parse a single song string and extract its genre
		for (Song song : songList) {
			if (song.getTrackName().equalsIgnoreCase(enteredSongName))
				enteredSongGenre = song.getGenre();
		}
		if (enteredSongGenre == null) {
			System.out.println("ERROR: SONG GENRE NOT INITIALIZED");
		}

		// filter songs with the same genre as the searched song
		ObservableList<Song> sameGenreSongs = FXCollections.observableArrayList();

		for (Song song : songList) {
			if (song.getGenre().equalsIgnoreCase(enteredSongGenre)) {
				sameGenreSongs.add(song);
				// System.out.println(song); //testing
			}
		}

		// check if there are songs with the same genre
		if (!sameGenreSongs.isEmpty()) {
			// get a random song from the filtered list
			Random random = new Random();
			Song song = sameGenreSongs.get(random.nextInt(sameGenreSongs.size()));
			return song.getTrackName();
		} else {
			// if no songs with the same genre found, return null or handle as needed
			System.out.println("ERROR: NO SONG WITH SAME GENRE");
		}
		return null;
	}

}
