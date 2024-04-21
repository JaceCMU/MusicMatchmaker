import java.util.ArrayList;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;

public class RightPane extends BorderPane {

	private ImageView plusButtonImg;
	private boolean showingPlaylist = false;
	private String showingPlaylistName;

	// Playlist list variables
	private BorderPane topPane;
	private ScrollPane scrollPane;
	private GridPane playlistList;
	private Label playlistText;
	private Button playlistAddButton;

	// Song list variables
	private Label playlistTitle;
	private ImageView backButtonImg;
	private Button backButton;

	public RightPane() {
		this.setPrefSize(400, 300);

		this.playlistText = new Label("Playlists");
		this.playlistText.setId("headerText");
		this.plusButtonImg = new ImageView(new Image("Images\\plusButton.png", 20, 20, false, false));
		this.playlistAddButton = new Button("", plusButtonImg);
		this.topPane = new BorderPane();
		topPane.setLeft(playlistText);
		topPane.setRight(playlistAddButton);

		// Set up panes
		playlistList = new GridPane();
		playlistList.add(new Label("Playlist Name"), 0, 0);
		playlistList.add(new Label("Songs"), 1, 0);
		scrollPane = new ScrollPane(playlistList);
		this.setTop(topPane);
		this.setCenter(scrollPane);

		// Pane margins
		playlistList.setHgap(40);
		playlistList.setVgap(10);
		playlistList.setPadding(new Insets(10));
	}

	public void changeList(String name) {
		if (showingPlaylist == false) {
			playlistList.getChildren().clear();
			backButtonImg = new ImageView(new Image("Images\\backButton.png", 20, 20, false, false));
			backButton = new Button("", backButtonImg);
			topPane.setLeft(backButton);
			playlistTitle = new Label(name);
			topPane.setCenter(playlistTitle);
			topPane.setRight(null);
			showingPlaylist = true;
		} else {
			playlistList.getChildren().clear();
			topPane.setLeft(playlistText);
			topPane.setCenter(null);
			playlistList.add(new Label("Playlist Name"), 0, 0);
			playlistList.add(new Label("Songs"), 1, 0);
			topPane.setRight(playlistAddButton);
			showingPlaylist = false;
		}
	}

	public void populateList(String playlistName) { // Populate the grid
		playlistList.getChildren().clear();

		if (showingPlaylist == false && playlistName == null) { // Populate the grid with playlists
			topPane.setLeft(playlistText);
			topPane.setCenter(null);
			topPane.setRight(playlistAddButton);

			playlistList.add(new Label("Playlist Name"), 0, 0);
			playlistList.add(new Label("Songs"), 1, 0);

			for (int i = 0; i < Playlist.playlists.size(); i++) {
				Button plButton = new Button(Playlist.playlists.get(i).getName());
				plButton.setStyle("-fx-text-fill:white;");
				playlistList.add(plButton, 0, i + 1);
				playlistList.add(new Label(String.valueOf(Playlist.playlists.get(i).getNumSongs())), 1, i + 1);

				plButton.setOnAction(new EventHandler<ActionEvent>() {
					@Override
					public void handle(ActionEvent event) {
						showingPlaylist = true;
						showingPlaylistName = plButton.getText();
						populateList(showingPlaylistName);
					}
				});
			}
		} else if (showingPlaylist == true) { // Populate the grid with songs from a playlist
			backButton = new Button("", new ImageView(new Image("Images\\backButton.png", 20, 20, false, false)));
			topPane.setLeft(backButton);
			topPane.setCenter(new Label(playlistName));
			topPane.setRight(null);

			backButton.setOnAction(new EventHandler<ActionEvent>() {
				public void handle(ActionEvent event) {
					showingPlaylist = false;
					populateList(null);
				}
			});

			playlistList.add(new Label("Song name"), 2, 0);
			playlistList.add(new Label("Artist"), 3, 0);
			playlistList.add(new Label("Album"), 4, 0);
			playlistList.add(new Label("Genre"), 5, 0);

			int indexOfPlaylist = -1;

			for (int i = 0; i < Playlist.playlists.size(); i++) // Find where the playlist is so we can get the song
																// list
			{
				if (Playlist.playlists.get(i).getName().equals(playlistName)) {
					indexOfPlaylist = i;
					break;
				}
			}

			ArrayList<Song> songList = Playlist.playlists.get(indexOfPlaylist).getSongs();

			for (int i = 0; i < songList.size(); i++) {
				Song currentSong = songList.get(i);
				String[] songInfo = { currentSong.getTrackName(), currentSong.getArtistName(),
						currentSong.getAlbumName(), currentSong.getGenre() };

				// Add each piece of information to a column
				for (int j = 0; j < 4; j++) {
					if (j < 1) {
						ImageView detailsButton = new ImageView(
								new Image("Images\\3DotsButton.png", 20, 20, false, false));
						playlistList.add(new Button("", detailsButton), j, i + 1);
					} else if (j < 2) {
						ImageView playButton = new ImageView(new Image("Images\\playButton.png", 20, 20, false, false));
						playlistList.add(new Button("", playButton), j, i + 1);
					}

					playlistList.add(new Label(songInfo[j]), j + 2, i + 1);
				}
			}
		}
	}

	public Button getAddButton() {
		return playlistAddButton;
	}

	public Button getBackButton() {
		return backButton;
	}

	public GridPane getPlaylistGrid() {
		return playlistList;
	}

	public ScrollPane getPlaylistPane() {
		return scrollPane;
	}
}
