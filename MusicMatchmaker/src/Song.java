import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Song {
	static List<Song> songList = new ArrayList<Song>();
	
	private String trackName;
	private String artistName;
	private String albumName;
	private String genre;
	private String isrc;
	
	public Song(String trackName, String artistName, String albumName, String genre, String isrc)
	{
		this.trackName = trackName;
		this.artistName = artistName;
		this.albumName = albumName;
		this.genre = genre;
		this.isrc = isrc;
	}
	
	//Return true if method was successful otherwise return false
	static boolean populateSongList()
	{
		//If the list has already been populated don't add songs
		if (!songList.isEmpty())
		{
			return false;
		}
		
		File file;
		Scanner scan;
		
		try
		{
			file = new File("songs.txt");
			scan = new Scanner(file);
		}catch (FileNotFoundException e)
		{
			return false;
		}
		
		scan.nextLine();
		int iter = 0;
		
		while (scan.hasNextLine())
		{
			String[] data = scan.nextLine().split(",");
			iter += 1;
			
			try
			{
				songList.add(new Song(data[0], data[1], data[2], data[3], data[5]));
			}catch (ArrayIndexOutOfBoundsException e)
			{
				System.out.print("Error: " + iter + ": ");
				
				for (int i = 0; i < data.length; i++)
				{
					System.out.print(data[i]);
				}
				
				System.out.println();
			}
			
		}
		
		scan.close();
		
		return true;
	}

	public String getTrackName() {
		return trackName;
	}

	public void setTrackName(String trackName) {
		this.trackName = trackName;
	}

	public String getArtistName() {
		return artistName;
	}

	public void setArtistName(String artistName) {
		this.artistName = artistName;
	}

	public String getAlbumName() {
		return albumName;
	}

	public void setAlbumName(String albumName) {
		this.albumName = albumName;
	}

	public String getGenre() {
		return genre;
	}

	public void setGenre(String genre) {
		this.genre = genre;
	}

	public String getIsrc() {
		return isrc;
	}

	public void setIsrc(String isrc) {
		this.isrc = isrc;
	}
}
