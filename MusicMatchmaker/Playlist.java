import java.util.ArrayList;

class Playlist {
    	static ArrayList<Playlist> playlists = new ArrayList<Playlist>();
        private String name;
        private ArrayList<Song> songs;

        static Playlist createPlaylist(String name) //Don't create a new playlist if the name isn't unique
        {
        	if (isPlaylistNameUnique(name))
        	{
        		Playlist newPlaylist = new Playlist(name);
        		return newPlaylist;
        	}
        	
        	return null;
        }
        
        private Playlist(String name) {
            this.name = name;
            this.songs = new ArrayList<>();
            playlists.add(this);
        }

        public String getName() {
            return name;
        }
        
        public int getNumSongs()
        {
        	return songs.size();
        }

        public void addSong(Song songToAdd) {
        	if (isSongInPlaylistUnique(this, songToAdd)) //Only add if the song isn't in the playlist already
        		songs.add(songToAdd);
        }
        
        static boolean isPlaylistNameUnique(String playlistName) //See if this playlist is already in the list of all playlists
        {
        	for (int i = 0; i < playlists.size(); i++) 
            {
            	if (playlists.get(i).getName().equals(playlistName))
            	{
            		return false;
            	}
            }
        	return true;
        }
        
        static boolean isSongInPlaylistUnique(Playlist playlist, Song song) //Make sure song isn't already in the playlist
        {
        	ArrayList<Song> songList = playlist.getSongs();
        	
        	for (int i = 0; i < songList.size(); i++) 
            {
            	if (songList.get(i).getTrackName().equals(song.getTrackName()))
            	{
            		return false;
            	}
            }
        	
        	return true;
        }

        public ArrayList<Song> getSongs() {
            return songs;
        }
    }