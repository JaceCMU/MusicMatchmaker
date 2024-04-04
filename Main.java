import java.util.List;

import javafx.animation.TranslateTransition;
import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Orientation;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ScrollBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Popup;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Duration;

public class Main extends Application{
	boolean toggle = false;
	
	static int currSong = 0;
	static int songThreshold = 25;
	static int currPage = 1;
	static int numPages;
	static List<Song> currSongList;
	static String searchType = "song";
	
	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		//Populate the song list
		Song.populateSongList();
		
		//If the amount of songs isn't divisible by 25 we have to add one extra page to show said songs
		numPages = (Song.songList.size() % 25) == 0 ? Song.songList.size() / 25 : Song.songList.size() / 25 + 1;
		
		//Create the main border pane
		BorderPane mainWindowPane = new BorderPane();
		
		//Create the top control bar
		TopPane topControlBar = new TopPane();
		
		//Center
		CenterPane centerSongPane = new CenterPane();
		
		//Left popout
		LeftPane leftPopoutPane = new LeftPane();
		
		//Variables that need to be used from CenterPane
		Button nextPageButton = centerSongPane.getNextPageButton();
		Button prevPageButton = centerSongPane.getPrevPageButton();
		GridPane songGridPane = centerSongPane.getSongGrid();
		ScrollPane gridScrollPane = centerSongPane.getSongScrollPane();
		Label pageContentLabel = centerSongPane.getPageContentLabel();
		TextField gotoPage = centerSongPane.getGotoPageField();
		
		//Fill song list with songThreshold number of songs
		updateListVariables(Song.songList);
		updateSongList(currSongList, songGridPane, currSong, songThreshold, -1);
	
		//Change page event handlers
		nextPageButton.setOnAction(new EventHandler<ActionEvent>()
			{
				public void handle(ActionEvent e)
				{	
					if (currPage < numPages)
					{
						currPage += 1;
						currSong += 25;
						
						updateSongList(currSongList, songGridPane, currSong, songThreshold, -1);
						
						pageContentLabel.setText("Page " + currPage + " of " + numPages);
						gotoPage.clear();
						gotoPage.setPromptText(String.valueOf(currPage));
						
						songGridPane.setMinSize(gridScrollPane.getWidth(), gridScrollPane.getHeight());
					}
				}
			});
		
		prevPageButton.setOnAction(new EventHandler<ActionEvent>()
			{
				public void handle(ActionEvent e)
				{
					if (currPage > 1)
					{
						currPage -= 1;
						currSong -= 25;
						
						updateSongList(currSongList, songGridPane, currSong, songThreshold, -1);
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
						int gotoPageNum = Integer.parseInt(gotoPage.getText());
						
						if (gotoPageNum < 1 || gotoPageNum > numPages)
						{
							return;
						}
						
						currPage = Integer.parseInt(gotoPage.getText());
						currSong = (currPage - 1) * 25;
						
						updateSongList(currSongList, songGridPane, currSong, songThreshold, Integer.parseInt(gotoPage.getText()));
						gotoPage.clear();
						gotoPage.setPromptText(String.valueOf(currPage));
						
						pageContentLabel.setText("Page " + currPage + " of " + numPages);
						
						songGridPane.setMinSize(gridScrollPane.getWidth(), gridScrollPane.getHeight());
					}
				}
			});
		
		
		//Left
		TranslateTransition popoutAnimation = new TranslateTransition(Duration.millis(500), leftPopoutPane);
		popoutAnimation.setFromX(leftPopoutPane.getTranslateX());
		popoutAnimation.setToX(0);
		
		topControlBar.getSlidingMenuButton().setOnAction(new EventHandler<ActionEvent>()
				{

					@Override
					public void handle(ActionEvent e) {
						if (toggle == false)
						{
							popoutAnimation.setRate(1);
							popoutAnimation.play();
							toggle = true;
						}else
						{
							popoutAnimation.setRate(-1);
							popoutAnimation.play();
							toggle = false;
						}
					}
					
				});
		
		
		//Right
		BorderPane rightPane = new BorderPane();
		rightPane.setPrefSize(400, 300);
		
		Label playlistText = new Label("Playlist");
		ScrollBar playlistScrollBar = new ScrollBar();
		playlistScrollBar.setOrientation(Orientation.VERTICAL);
		
		
		//Add components to border panes
		rightPane.setTop(playlistText);
		rightPane.setRight(playlistScrollBar);
		
		mainWindowPane.setTop(topControlBar);
		mainWindowPane.setLeft(leftPopoutPane);
		mainWindowPane.setCenter(centerSongPane);
		mainWindowPane.setRight(rightPane);
		
		//Setting component styles
		mainWindowPane.getStyleClass().add("pane");
		songGridPane.getStyleClass().add("pane");
		gridScrollPane.getStyleClass().add("pane");
		
		//Setting ids of components to be styled in CSS file
		playlistText.setId("headerText");

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
		RadioButton[] searchTypeButtons = {new RadioButton("Song Name"), new RadioButton("Artist Name"), new RadioButton("Genre")};
		searchTypeButtons[0].setSelected(true);
		for (RadioButton rb : searchTypeButtons)
		{
			rb.setToggleGroup(radioButtonGroup);
		}
		HBox searchTypeBox = new HBox(searchTypeButtons);
		VBox advancedSearchBox = new VBox(searchTypeLabel, searchTypeBox);
		advancedSearchBox.getStyleClass().add("pane");
		
		//Advanced search stage stage creation
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
		topControlBar.getSearchBar().focusedProperty().addListener(new ChangeListener<Boolean>() {
		    @Override
		    public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
		        if (newValue) {
		        	topControlBar.setSearchFieldText("");
		        }
		    }
		});
		
		 //Settings button event
		topControlBar.getSettingsButton().setOnAction(new EventHandler<ActionEvent>()
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
		topControlBar.getSearchButton().setOnAction(new EventHandler<ActionEvent>()
			{
				public void handle(ActionEvent e)
				{
					SongRecommendationSystem recommendationHandler = new SongRecommendationSystem();
					
					switch (searchType)
					{
					case "song":
						updateListVariables(recommendationHandler.recommendBySimilarGenre(topControlBar.getSearchFieldText()));
						updateSongList(currSongList, songGridPane, currSong, songThreshold, 1);
						break;
					case "artist":
						updateListVariables(recommendationHandler.recommendByArtist(topControlBar.getSearchFieldText()));
						
						updateSongList(currSongList, songGridPane, currSong, songThreshold, 1);
						break;
					case "genre":
						updateListVariables(recommendationHandler.recommendByGenre(topControlBar.getSearchFieldText()));
						updateSongList(currSongList, songGridPane, currSong, songThreshold, 1);
						break;
					}
					
					gotoPage.clear();
					gotoPage.setPromptText(String.valueOf(currPage));
					pageContentLabel.setText("Page " + currPage + " of " + numPages);
					
					songGridPane.setMinSize(gridScrollPane.getWidth(), gridScrollPane.getHeight());
				}
			});
		
		topControlBar.getDetailsButton().setOnAction(new EventHandler<ActionEvent>()
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
						topControlBar.setSearchFieldText("Search for song");
					}else if (buttonText.equals("Artist Name"))
					{
						searchType = "artist";
						topControlBar.setSearchFieldText("Search for artist");

					}else if (buttonText.equals("Genre"))
					{
						searchType = "genre";
						topControlBar.setSearchFieldText("Search for genre");
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
	public void updateSongList(List<Song> songList, GridPane songListGrid, int currSongNum, int songThreshold, int gotoPage)
	{		
		//System.out.println(songList);
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
				
		for (int i = currSongNum; i < currSongNum + songThreshold && i < songList.size(); i++)
		{
			Song currentSong = songList.get(i);
			
			String[] songInfo = {currentSong.getTrackName(), currentSong.getArtistName(), currentSong.getAlbumName(), currentSong.getGenre()};
			
			//Add each piece of information to a column
			for (int j = 0; j < 4; j++)
			{
				songListGrid.add(new Label(songInfo[j]), j, (i % 25) + 1);
			}
		}
	}
	
	public void updateListVariables(List<Song> newSongList)
	{
		currSong = 0;
		songThreshold = 25;
		currPage = 1;
		currSongList = newSongList;
		numPages = (newSongList.size() % 25) == 0 ? newSongList.size() / 25 : newSongList.size() / 25 + 1;
	}
}
