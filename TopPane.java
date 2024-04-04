import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;

public class TopPane extends HBox{
	private ImageView searchButtonImg;
	private ImageView slidingMenuToggleImg;
	private ImageView settingsButtonImg;
	private ImageView moreDetailsButtonImg;
	
	private Button moreDetailsButton;
	private TextField searchSongField;
	private Button searchButton;
	
	private Button slidingMenuToggleButton;
	private Button settingsButton;
	
	private HBox searchBar;
	
	public TopPane()
	{
		searchButtonImg = new ImageView(new Image("Images\\searchButton.png", 20, 20, false, false));
		slidingMenuToggleImg = new ImageView(new Image("Images\\3LinesButton.png", 30, 30, false, false));
		settingsButtonImg = new ImageView(new Image("Images\\settingsButton.png", 30, 30, false, false));
		moreDetailsButtonImg = new ImageView(new Image("Images\\3DotsButton.png", 20, 20, false, false));
		
		
		//Top
		this.moreDetailsButton = new Button("", moreDetailsButtonImg);
		this.searchSongField = new TextField("Search for similar songs");
		searchSongField.setPrefWidth(400);
		searchSongField.setPrefHeight(30);
		this.searchButton = new Button("", searchButtonImg);
		
		this.slidingMenuToggleButton = new Button("", slidingMenuToggleImg);
		this.settingsButton = new Button("", settingsButtonImg);
		
		//HBox for the three search bar components
		this.searchBar = new HBox(moreDetailsButton, searchSongField, searchButton);
		searchBar.setAlignment(Pos.CENTER);
		
		//HBox for the entire top area
		this.setSpacing(30);
		this.getChildren().addAll(slidingMenuToggleButton, searchBar, settingsButton);
		HBox.setHgrow(searchBar, Priority.ALWAYS);
		
		//Round search bar buttons
		moreDetailsButton.setStyle("-fx-border-width: 2; -fx-border-color: black;-fx-border-radius: 20px 0 0 20px;");
		searchButton.setStyle("-fx-border-width: 2;-fx-border-color: black;-fx-border-radius: 0 20px 20px 0;");
		this.setStyle("-fx-border-color: black; -fx-border-width: 0 0 2 0;");
	}
	
	public Button getDetailsButton()
	{
		return moreDetailsButton;
	}
	
	public Button getSettingsButton()
	{
		return settingsButton;
	}
	
	public Button getSearchButton()
	{
		return searchButton;
	}
	
	public Button getSlidingMenuButton()
	{
		return slidingMenuToggleButton;
	}
	
	public void setSearchFieldText(String text)
	{
		searchSongField.setText(text);
	}
	
	public String getSearchFieldText()
	{
		return searchSongField.getText();
	}
	
	public TextField getSearchBar()
	{
		return searchSongField;
	}
}
