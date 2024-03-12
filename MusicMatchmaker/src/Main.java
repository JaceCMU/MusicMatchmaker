import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ScrollBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class Main extends Application{
	int currSong = 0;
	int songThreshold = 25;
	int currPage = 1;
	int numPages;
	String searchType = "song";
	
	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		Song.populateSongList();
		
		BorderPane mainWindowPane = new BorderPane();
		
		//Images
		ImageView searchButtonImg = new ImageView(new Image("Images\\searchButton.png", 20, 20, false, false));
		ImageView slidingMenuToggleImg = new ImageView(new Image("Images\\3LinesButton.png", 30, 30, false, false));
		ImageView settingsButtonImg = new ImageView(new Image("Images\\settingsButton.png", 30, 30, false, false));
		ImageView moreDetailsButtonImg = new ImageView(new Image("Images\\3DotsButton.png", 20, 20, false, false));
		
		
		//Top
		Button moreDetailsButton = new Button("", moreDetailsButtonImg);
		TextField searchSongField = new TextField("Search for similar songs");
		searchSongField.setPrefWidth(400);
		searchSongField.setPrefHeight(30);
		Button searchButton = new Button("", searchButtonImg);
		
		Button slidingMenuToggleButton = new Button("", slidingMenuToggleImg);
		Button settingsButton = new Button("", settingsButtonImg);
		
		//HBox for the three search bar components
		HBox searchBar = new HBox(moreDetailsButton, searchSongField, searchButton);
		searchBar.setAlignment(Pos.CENTER);
		
		//HBox for the entire top area
		HBox topBar = new HBox(30, slidingMenuToggleButton, searchBar, settingsButton);
		HBox.setHgrow(searchBar, Priority.ALWAYS);
		
		
		//Center
		BorderPane centerPane = new BorderPane();
		centerPane.setPrefSize(600, 300);
		
		GridPane songGridPane = new GridPane();
		ScrollPane gridScrollPane = new ScrollPane(songGridPane);
		songGridPane.setMinWidth(gridScrollPane.getWidth());

		songGridPane.setHgap(40);
		songGridPane.setVgap(10);
		songGridPane.setPadding(new Insets(10));
		
		//Set up first row of grid pane
		songGridPane.add(new Label("Song name"), 0, 0);
		songGridPane.add(new Label("Artist"), 1, 0);
		songGridPane.add(new Label("Album"), 2, 0);
		songGridPane.add(new Label("Genre"), 3, 0);
		
		//If the amount of songs isn't divisible by 25 we have to add one extra page to show said songs
		numPages = (Song.songList.size() % 25) == 0 ? Song.songList.size() / 25 : Song.songList.size() / 25 + 1;
		
		//Fill song list with songThreshold number of songs
		updateSongList(songGridPane, currSong, songThreshold, -1);
		
		//Creating the page navigation bar under songs
		Button prevPage = new Button("<");
		TextField gotoPage = new TextField();
		gotoPage.setPromptText(String.valueOf(currPage));
		gotoPage.setAlignment(Pos.CENTER);
		Button nextPage = new Button(">");
		Label pageContentLabel = new Label("Page 1 of " + numPages);
		HBox pageModifier = new HBox(prevPage, gotoPage, nextPage, pageContentLabel);
		pageModifier.setAlignment(Pos.CENTER);
		
		
		//Change page event handlers
		nextPage.setOnAction(new EventHandler<ActionEvent>()
			{
				public void handle(ActionEvent e)
				{
					if (currPage < numPages + 1)
					{
						currPage += 1;
						currSong += 25;
						
						updateSongList(songGridPane, currSong, songThreshold, -1);
						
						pageContentLabel.setText("Page " + currPage + " of " + numPages);
						gotoPage.clear();
						gotoPage.setPromptText(String.valueOf(currPage));
						
						songGridPane.setMinSize(gridScrollPane.getWidth(), gridScrollPane.getHeight());
					}
				}
			});
		
		prevPage.setOnAction(new EventHandler<ActionEvent>()
			{
				public void handle(ActionEvent e)
				{
					if (currPage > 1)
					{
						currPage -= 1;
						currSong -= 25;
						
						updateSongList(songGridPane, currSong, songThreshold, -1);
						gotoPage.clear();
						gotoPage.setPromptText(String.valueOf(currPage));
						
						pageContentLabel.setText("Page " + currPage + " of " + numPages);
						
						songGridPane.setMinSize(gridScrollPane.getWidth(), gridScrollPane.getHeight());
					}
				}
			});
		
		gotoPage.setOnKeyPressed(new EventHandler<KeyEvent>()
			{
				public void handle(KeyEvent e)
				{
					if (e.getCode() == KeyCode.ENTER)
					{
						currPage = Integer.parseInt(gotoPage.getText());
						currSong = (currPage - 1) * 25;
						
						updateSongList(songGridPane, currSong, songThreshold, Integer.parseInt(gotoPage.getText()));
						gotoPage.clear();
						gotoPage.setPromptText(String.valueOf(currPage));
						
						pageContentLabel.setText("Page " + currPage + " of " + numPages);
						
						songGridPane.setMinSize(gridScrollPane.getWidth(), gridScrollPane.getHeight());
					}
				}
			});
		
		Label songsText = new Label("Songs");
		
		//Right
		BorderPane rightPane = new BorderPane();
		rightPane.setPrefSize(400, 300);
		
		Label playlistText = new Label("Playlist");
		ScrollBar playlistScrollBar = new ScrollBar();
		playlistScrollBar.setOrientation(Orientation.VERTICAL);
		
		
		//Add components to border panes
		mainWindowPane.setTop(topBar);
		
		centerPane.setTop(songsText);
		centerPane.setCenter(gridScrollPane);
		centerPane.setBottom(pageModifier);
		
		rightPane.setTop(playlistText);
		rightPane.setRight(playlistScrollBar);
		
		mainWindowPane.setCenter(centerPane);
		mainWindowPane.setRight(rightPane);
		
		
		//Setting component styles
		mainWindowPane.getStyleClass().add("pane");
		songGridPane.getStyleClass().add("pane");
		gridScrollPane.getStyleClass().add("pane");
		
		//Rounding sides of the buttons next to search bar and adding a border to the top
		moreDetailsButton.setStyle("-fx-border-width: 2; -fx-border-color: black;-fx-border-radius: 20px 0 0 20px;");
		searchButton.setStyle("-fx-border-width: 2;-fx-border-color: black;-fx-border-radius: 0 20px 20px 0;");
		topBar.setStyle("-fx-border-color: black; -fx-border-width: 0 0 2 0;");
		
		//Setting ids of components to be styled in CSS file
		playlistText.setId("headerText");
		songsText.setId("headerText");
		prevPage.setId("pageNavigationButtons");
		nextPage.setId("pageNavigationButtons");


		//Settings stage creation		
		Label themeText = new Label("Theme");
		ChoiceBox<String> themeDropdown = new ChoiceBox<String>();
		themeDropdown.getItems().addAll("Dark Theme", "Theme 2", "Theme 3", "Theme 4", "Theme 5");
		themeDropdown.setValue("Dark Theme");
		VBox themeBox = new VBox(themeText, themeDropdown);
		GridPane settingsGrid = new GridPane();
		settingsGrid.getStyleClass().add("pane");
		settingsGrid.setPrefSize(600, 400);
		
		settingsGrid.add(themeBox, 0, 0);
		
		Scene settingsScene = new Scene(settingsGrid);
		Stage settingsStage = new Stage();
		settingsStage.setTitle("Settings");
		settingsStage.setScene(settingsScene);
		settingsStage.initModality(Modality.APPLICATION_MODAL);
		settingsStage.initOwner(primaryStage);
		
		//Advanced search stage nodes creation
		Label searchTypeLabel = new Label("Search by: ");
		searchTypeLabel.setId("black-text");
		ToggleGroup radioButtonGroup = new ToggleGroup();
		//RadioButton array for search types
		RadioButton[] searchTypeButtons = {new RadioButton("Song Name"), new RadioButton("Artist Name"), new RadioButton("Album Name"), new RadioButton("Genre")};
		searchTypeButtons[0].setSelected(true);
		for (RadioButton rb : searchTypeButtons)
		{
			rb.setToggleGroup(radioButtonGroup);
		}
		HBox searchTypeBox = new HBox(searchTypeButtons);
		VBox advancedSearchBox = new VBox(searchTypeLabel, searchTypeBox);
		advancedSearchBox.getStyleClass().add("pane");
		
		//Advanced search stage creation
		Scene advancedSearchScene = new Scene(advancedSearchBox);
		Stage advancedSearchStage = new Stage();
		advancedSearchStage.setTitle("Advanced Search");
		advancedSearchStage.setScene(advancedSearchScene);
		advancedSearchStage.initModality(Modality.APPLICATION_MODAL);
		advancedSearchStage.initOwner(primaryStage);
		
		//Set up main stage
		primaryStage.setTitle("Music Matchmaker");
		Scene mainScene = new Scene(mainWindowPane);
		
		//Event handling
		 //Settings button event
		settingsButton.setOnAction(new EventHandler<ActionEvent>()
			{
				public void handle(ActionEvent e)
				{
					settingsStage.show();
				}
			});
		
		 //Settings theme dropdown
		themeDropdown.setOnAction(new EventHandler<ActionEvent>()
				{

					@Override
					public void handle(ActionEvent event) {
						if (themeDropdown.getValue().equals("Dark Theme"))
						{
							mainScene.getStylesheets().add("DarkTheme.css");
							settingsScene.getStylesheets().add("DarkTheme.css");
							advancedSearchScene.getStylesheets().add("DarkTheme.css");
						}
					}
			
				});
		
		 //Search button event
		searchButton.setOnAction(new EventHandler<ActionEvent>()
			{
				public void handle(ActionEvent e)
				{
					
				}
			});
		
		moreDetailsButton.setOnAction(new EventHandler<ActionEvent>()
			{
				public void handle(ActionEvent event)
				{
					advancedSearchStage.show();
				}
			});
		
		advancedSearchStage.setOnCloseRequest(new EventHandler<WindowEvent>()
			{
				@Override
				public void handle(WindowEvent event)
				{
					RadioButton selectedButton = (RadioButton)radioButtonGroup.getSelectedToggle();
					String buttonText = selectedButton.getText();
					
					if (buttonText.equals("Song Name"))
					{
						searchType = "song";
						searchSongField.setText("Search for similar song");
					}else if (buttonText.equals("Artist Name"))
					{
						searchType = "artist";
						searchSongField.setText("Search for similar artist");

					}else if (buttonText.equals("Album Name"))
					{
						searchType = "album";
						searchSongField.setText("Search for similar album");

					}else if (buttonText.equals("Genre"))
					{
						searchType = "genre";
						searchSongField.setText("Search for similar genre");
					}
				}
			});
		
		
		//Add CSS styling
		mainScene.getStylesheets().add("DarkTheme.css");
		settingsScene.getStylesheets().add("DarkTheme.css");
		advancedSearchScene.getStylesheets().add("DarkTheme.css");
		
		//Add scene and show stage
		primaryStage.setScene(mainScene);
		primaryStage.show();
	}
	
	//Function to update the songGridPane, if gotoPage is not -1 it is invoked by the TextField
	public void updateSongList(GridPane songListGrid, int currSongNum, int songThreshold, int gotoPage)
	{
		//Clear the grid of previous elements
		songListGrid.getChildren().clear();
		
		//Populate the grid
		songListGrid.add(new Label("Song name"), 0, 0);
		songListGrid.add(new Label("Artist"), 1, 0);
		songListGrid.add(new Label("Album"), 2, 0);
		songListGrid.add(new Label("Genre"), 3, 0);
		
		if (gotoPage > 0 && gotoPage <= numPages)
		{
			currSongNum = (gotoPage - 1) * 25;
		}
				
		for (int i = currSongNum; i < currSongNum + songThreshold && i < Song.songList.size(); i++)
		{
			Song currentSong = Song.songList.get(i);
			
			String[] songInfo = {currentSong.getTrackName(), currentSong.getArtistName(), currentSong.getAlbumName(), currentSong.getGenre()};
			
			//Add each piece of information to a column
			for (int j = 0; j < 4; j++)
			{
				songListGrid.add(new Label(songInfo[j]), j, (i % 25) + 1);
			}
		}
	}
}
