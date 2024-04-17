import java.util.List;
import javafx.animation.ParallelTransition;
import javafx.animation.TranslateTransition;
import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ScrollBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Popup;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Duration;

public class Main extends Application{
	//Variables for the main GUI
	boolean toggle = false;
	static int currSong = 0;
	static int songThreshold = 25;
	static int currPage = 1;
	static int numPages;
	static List<Song> currSongList;
	static String searchType = "song";
	
	//Variables for music map
	private ImageView ivStar1, ivStar2, ivStar3, ivStar4, ivStar5, ivStar6, ivStar7;
	private static final String FONT_FAMILY = "Helvetica";// creates a fond that will be used for everything
	private static final Insets PADDING_ZERO = new Insets(0);// sets a default of zero padding
	private StackPane stackPane, starPane;
	private ListView<Song> listView;
	private TextField musicSearch;
	private GridPane textFieldGridPane;
	private Button searchButton, resetButton;
	private Label enterLabel;
	private HBox searchBox;
	protected VBox mainBox;
	private BackgroundImg background;
	private Text txtStar1, txtStar2, txtStar3, txtStar4, txtStar5, txtStar6, txtStar7;
	private DatabaseSort database;
	private ObservableList<Song> songList;
	
	//Scenes
	private Scene mainScene;
	private Scene musicMapScene;
	
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
		
		gridScrollPane.widthProperty().addListener((obs, oldVal, newVal) -> {
			songGridPane.setMinWidth((double) newVal);
		});
		
		gridScrollPane.heightProperty().addListener((obs, oldVal, newVal) -> {
			songGridPane.setMinHeight((double) newVal);
		});
		
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
		
		leftPopoutPane.getMapButton().setOnAction(new EventHandler<ActionEvent>()
				{
					@Override
					public void handle(ActionEvent e)
					{
						if (primaryStage.getScene() != musicMapScene)
						{
							primaryStage.setScene(musicMapScene);
						}
					}
				});
		
		EventHandler<KeyEvent> changeScenes = new EventHandler<>() {
		    public void handle(KeyEvent e) {
		        if (e.getCode() == KeyCode.ESCAPE) {
		            if (primaryStage.getScene() == musicMapScene) {
		            	primaryStage.setScene(mainScene);
		            }

		            e.consume();
		        }
		    }
		};
		
		
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
		mainScene = new Scene(mainWindowPane);
		
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
		
		//Code for Music Map
		ListViewConfigurations lvc = new ListViewConfigurations();

		stackPane = new StackPane();
		starPane = new StackPane();
		// CONFIGURING ITEMS FOR MAINBOX
		listView = lvc.initializeListView();
		listView.setVisible(false);
		musicSearch = createTextField();
		textFieldGridPane = new GridPane();
		textFieldGridPane.add(musicSearch, 0, 0);
		textFieldGridPane.setAlignment(Pos.CENTER);
		searchButton = createSearchButton();
		enterLabel = createLabel();
		searchBox = createSearchBox(textFieldGridPane, searchButton);
		resetButton = createResetButton();

		mainBox = createMainBox(enterLabel, searchBox, listView);
		stackPane.getChildren().addAll(mainBox, resetButton);
		background = new BackgroundImg();
		background.setBackground(starPane);

		initializeStars();// adding stars
		// METHODS SETTING PARAMETERS FOR LISTVIEW
		lvc.addListViewSelectionListener(listView, musicSearch, stackPane);
		lvc.configureListViewVisibility(musicSearch, listView, stackPane);
		lvc.addSearchListener(musicSearch);

		musicMapScene = new Scene(new StackPane(starPane, stackPane));
		resetStage();
		
		musicMapScene.addEventFilter(KeyEvent.KEY_PRESSED, changeScenes);
		
		//Add scene and show stage
		primaryStage.setScene(mainScene);
		primaryStage.show();
	}
	
	//Main GUI methods
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
	
	//Music map methods
	private void initializeStars() {

		ivStar1 = new ImageView(new Image("images/stars.png"));
		txtStar1 = new Text();
		txtStar1.setFont(Font.font("Helvetica", FontWeight.NORMAL, .10));
		txtStar1.setFill(javafx.scene.paint.Color.WHITE);
		ivStar2 = new ImageView(new Image("images/stars.png"));
		txtStar2 = new Text();
		txtStar2.setFont(Font.font("Helvetica", FontWeight.NORMAL, .10));
		txtStar2.setFill(javafx.scene.paint.Color.WHITE);
		ivStar3 = new ImageView(new Image("images/stars.png"));
		txtStar3 = new Text();
		txtStar3.setFont(Font.font("Helvetica", FontWeight.NORMAL, .10));
		txtStar3.setFill(javafx.scene.paint.Color.WHITE);
		ivStar4 = new ImageView(new Image("images/stars.png"));
		txtStar4 = new Text();
		txtStar4.setFont(Font.font("Helvetica", FontWeight.NORMAL, .10));
		txtStar4.setFill(javafx.scene.paint.Color.WHITE);
		ivStar5 = new ImageView(new Image("images/stars.png"));
		txtStar5 = new Text();
		txtStar5.setFont(Font.font("Helvetica", FontWeight.NORMAL, .10));
		txtStar5.setFill(javafx.scene.paint.Color.WHITE);
		ivStar6 = new ImageView(new Image("images/stars.png"));
		txtStar6 = new Text();
		txtStar6.setFont(Font.font("Helvetica", FontWeight.NORMAL, .10));
		txtStar6.setFill(javafx.scene.paint.Color.WHITE);
		ivStar7 = new ImageView(new Image("images/stars.png"));
		txtStar7 = new Text();
		txtStar7.setFont(Font.font("Helvetica", FontWeight.NORMAL, .10));
		txtStar7.setFill(javafx.scene.paint.Color.WHITE);

		ivStar1.setTranslateX(0);
		ivStar1.setTranslateY(-200);
		ivStar1.setFitWidth(0.1);
		ivStar1.setFitHeight(0.1);
		txtStar1.setTranslateX(0);
		txtStar1.setTranslateY(-170);

		ivStar2.setTranslateX(400);
		ivStar2.setTranslateY(300);
		ivStar2.setFitWidth(0.1);
		ivStar2.setFitHeight(0.1);
		txtStar2.setTranslateX(400);
		txtStar2.setTranslateY(330);

		ivStar3.setTranslateX(300);
		ivStar3.setTranslateY(00);
		ivStar3.setFitWidth(0.1);
		ivStar3.setFitHeight(0.1);
		txtStar3.setTranslateX(300);
		txtStar3.setTranslateY(30);

		ivStar4.setTranslateX(500);
		ivStar4.setTranslateY(-300);
		ivStar4.setFitWidth(0.1);
		ivStar4.setFitHeight(0.1);
		txtStar4.setTranslateX(500);
		txtStar4.setTranslateY(-270);

		ivStar5.setTranslateX(-400);
		ivStar5.setTranslateY(300);
		ivStar5.setFitWidth(0.1);
		ivStar5.setFitHeight(0.1);
		txtStar5.setTranslateX(-400);
		txtStar5.setTranslateY(330);

		ivStar6.setTranslateX(-300);
		ivStar6.setTranslateY(0);
		ivStar6.setFitWidth(0.1);
		ivStar6.setFitHeight(0.1);
		txtStar6.setTranslateX(-300);
		txtStar6.setTranslateY(30);

		ivStar7.setTranslateX(-500);
		ivStar7.setTranslateY(-300);
		ivStar7.setFitWidth(0.1);
		ivStar7.setFitHeight(0.1);
		txtStar7.setTranslateX(-500);
		txtStar7.setTranslateY(-270);

		// Add the images to the StackPane
		starPane.getChildren().addAll(txtStar1, ivStar1, txtStar2, ivStar2, txtStar3, ivStar3, txtStar4, ivStar4,
				txtStar5, ivStar5, txtStar6, ivStar6, txtStar7, ivStar7);

		
		
		// SETTING UP ANIMATIONS
		ParallelTransition pt = new ParallelTransition();

		searchButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {

				searchButton.setDisable(true);// disallows the search button to be pressed again before reset

				final String song = musicSearch.getText();
				System.out.println(song);

				database = new DatabaseSort();
				songList = database.parseDatabaseFile("songs.txt");
				txtStar1.setText(song);
				txtStar2.setText(database.getRandomSongWithSameGenre(songList, song));
				txtStar3.setText(database.getRandomSongWithSameGenre(songList, song));
				txtStar4.setText(database.getRandomSongWithSameGenre(songList, song));
				txtStar5.setText(database.getRandomSongWithSameGenre(songList, song));
				txtStar6.setText(database.getRandomSongWithSameGenre(songList, song));
				txtStar7.setText(database.getRandomSongWithSameGenre(songList, song));

				Animations animate = new Animations();

				animate.scaleDown(mainBox);
				animate.moveUp(mainBox);
				animate.fadeIn(resetButton);// fades a reset button in
				resetButton.setVisible(true);

				pt.getChildren().addAll(animate.scaleUp(ivStar1), animate.floats(ivStar1), animate.scaleUp(txtStar1),
						animate.floats4(txtStar1), animate.scaleUp(ivStar2), animate.floats2(ivStar2),
						animate.scaleUp(txtStar2), animate.floats3(txtStar2), animate.scaleUp(ivStar3),
						animate.floats3(ivStar3), animate.scaleUp(txtStar3), animate.floats4(txtStar3),
						animate.scaleUp(ivStar4), animate.floats4(ivStar4), animate.scaleUp(txtStar4),
						animate.floats2(txtStar4), animate.scaleUp(ivStar5), animate.floats2(ivStar5),
						animate.scaleUp(txtStar5), animate.floats3(txtStar5), animate.scaleUp(ivStar6),
						animate.floats3(ivStar6), animate.scaleUp(txtStar6), animate.floats2(txtStar6),
						animate.scaleUp(ivStar7), animate.floats2(ivStar7), animate.scaleUp(txtStar7),
						animate.floats3(txtStar7));

				pt.play();
				

				resetButton.setDisable(false);
			}
			
		});		  
	}

	private TextField createTextField() {
		TextField musicSearch = new TextField();
		musicSearch.setFont(Font.font("FONT_FAMILY", FontWeight.NORMAL, 30));
		musicSearch.setStyle("-fx-background-radius: 15px; -fx-background-color: white;");
		musicSearch.setMaxWidth(300);
		return musicSearch;
	}
	
	// binds an image to a button allowing it to function as a working search button
	// function
	private Button createResetButton() {
		Button resetButton = new Button("Reset");
		resetButton.setPrefSize(150, 50);
		resetButton.setFont(Font.font(FONT_FAMILY, FontWeight.NORMAL, 30));
		resetButton.setStyle("-fx-background-color: white; -fx-background-radius: 15px;-fx-font-style: italic;");
		resetButton.setCursor(Cursor.HAND);
		resetButton.setTranslateX(0);
		resetButton.setTranslateY(150);
		resetButton.setVisible(false);
		resetButton.setDisable(true);
		return resetButton;
	}
	
	private Button createSearchButton() {
		Button searchButton = new Button();
		Image searchImage = new Image("images/search.jpg");
		ImageView imageView = new ImageView(searchImage);
		imageView.setFitWidth(30);// setting size of image
		imageView.setFitHeight(30);
		searchButton.setGraphic(imageView);// sets the image to button
		searchButton.setPrefSize(55, 55);// setting size of button
		searchButton.setStyle("-fx-background-color: white; -fx-background-radius: 15px;");// formatting button
		searchButton.setCursor(Cursor.HAND);// changing type of cursor when hovering over button
		return searchButton;
	}
	
	

	// creates a label to prompt user to enter a song
	private Label createLabel() {
		Label enterLabel = new Label("Find similar songs:");
		enterLabel.setFont(Font.font(FONT_FAMILY, FontWeight.NORMAL, 30));
		enterLabel.setStyle("-fx-font-style: italic;");
		enterLabel.setTextFill(javafx.scene.paint.Color.WHITE);
		return enterLabel;
	}

	// creates a HBox that creates a functional search bar
	private HBox createSearchBox(GridPane textFieldGridPane, Button searchButton) {
		HBox searchBox = new HBox(10);
		searchBox.setAlignment(Pos.CENTER);
		searchBox.getChildren().addAll(textFieldGridPane, searchButton);
		searchBox.setPadding(new Insets(0, 0, 0, 65));
		return searchBox;
	}
	
	private VBox createMainBox(Label enterLabel, HBox searchBox, ListView<Song> listView) {

		VBox mainBox = new VBox();// creates a VBOX that contains all elements
		mainBox.setAlignment(Pos.CENTER);
		mainBox.getChildren().addAll(enterLabel, searchBox, listView);// adds to mainBox
		mainBox.setPadding(PADDING_ZERO);
		return mainBox;
	}
	
	private void resetStage() {
		Animations animate = new Animations();
		ParallelTransition pt = new ParallelTransition();

		resetButton.setOnAction(e -> {

			resetButton.setDisable(true);

			pt.getChildren().addAll(animate.scaleDown(ivStar1), animate.scaleDown(txtStar1), animate.scaleDown(ivStar2),
					animate.scaleDown(txtStar2), animate.scaleDown(ivStar3), animate.scaleDown(txtStar3),
					animate.scaleDown(ivStar4), animate.scaleDown(txtStar4), animate.scaleDown(ivStar5),
					animate.scaleDown(txtStar5), animate.scaleDown(ivStar6), animate.scaleDown(txtStar6),
					animate.scaleDown(ivStar7), animate.scaleDown(txtStar7), animate.fadeOut(resetButton),
					animate.scaleUp(mainBox), animate.moveDown(mainBox), animate.fadeIn(mainBox));
			pt.play();
		});
		pt.setOnFinished(event -> {
		    searchButton.setDisable(false);
		});

		

	}
}
