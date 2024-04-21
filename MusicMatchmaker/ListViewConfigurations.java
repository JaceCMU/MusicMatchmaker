import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.StackPane;
import javafx.util.Callback;

public class ListViewConfigurations {
	private ListView<Song> listView;
	private DatabaseSort databaseSort;
	private ObservableList<Song> songList;

	private FilteredList<Song> filteredSongs; // Specify type parameter <Song>

	ListView<Song> initializeListView() {

		listView = new ListView<>();

		databaseSort = new DatabaseSort();

		songList = databaseSort.parseDatabaseFile("songs.txt");

		// create a FilteredList with the original songList
		filteredSongs = new FilteredList<>(songList, p -> true);

		// create a SortedList to keep the filtered data sorted
		SortedList<Song> sortedSongs = new SortedList<>(filteredSongs);

		listView.setItems(sortedSongs);

		listView.setMaxSize(270, 130);

		// set custom CellFactory to display Song objects properly
		listView.setCellFactory(new Callback<ListView<Song>, ListCell<Song>>() {
			@Override
			public ListCell<Song> call(ListView<Song> param) {
				return new ListCell<Song>() {
					@Override
					protected void updateItem(Song song, boolean empty) {
						super.updateItem(song, empty);
						if (empty || song == null) {
							setText(null);
						} else {
							setText(formatSongDetails(song));
						}
					}
				};
			}
		});

		return listView;
	}

	void addListViewSelectionListener(ListView<Song> listView, TextField musicSearch, StackPane stackPane) {
		listView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
			if (!listView.getItems().isEmpty()) {
				int selectedIndex = listView.getSelectionModel().getSelectedIndex();
				if (selectedIndex >= 0 && selectedIndex < listView.getItems().size()) {
					musicSearch.setText(formatSongDetails(newValue));
					listView.setVisible(false);
				}
			} else {
				System.out.println("error");
			}
		});
	}

	private String formatSongDetails(Song song) {
		return String.format(song.getTrackName());
	}

	// sets when the list should be visible
	void configureListViewVisibility(TextField musicSearch, ListView<Song> listView, StackPane stackPane) {
		// updates visibility based on focus and non-empty text
		musicSearch.textProperty().addListener((obs, oldVal, newVal) -> listView.setVisible(musicSearch.isFocused()));
		// close the ListView when clicked outside it
		stackPane.setOnMouseClicked(event -> {
			if (event.getButton() == MouseButton.PRIMARY
					&& !listView.getBoundsInParent().contains(event.getX(), event.getY())) {
				listView.setVisible(false);
				musicSearch.getParent().requestFocus();
			}
		});
	}

	void addSearchListener(TextField musicSearch) {
		musicSearch.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				updateFilterPredicate(newValue);
			}
		});
	}

	private void updateFilterPredicate(String newValue) {
		filteredSongs.setPredicate(song -> {
			if (newValue == null || newValue.isEmpty()) {
				return true; // shows songs when txt box is empty
			}
			String lowerCaseFilter = newValue.toLowerCase();
			// filter songs based on their properties (you can modify this based on your
			// Song class)
			return song.getTrackName().toLowerCase().contains(lowerCaseFilter);
		});
	}

}
