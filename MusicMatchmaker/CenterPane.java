import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class CenterPane extends BorderPane {
	// Song list
	private GridPane songGridPane;
	private ScrollPane gridScrollPane;
	// Song navigation bar
	private ImageView playButton;
	private Button prevPage;
	private TextField gotoPage;
	private Button nextPage;
	private Label pageContentLabel;
	private HBox pageModifier;

	Label songsText;

	public CenterPane() {

		// BorderPane centerPane = new BorderPane();
		this.setPrefSize(600, 300);

		this.songGridPane = new GridPane();
		this.gridScrollPane = new ScrollPane(songGridPane);

		songGridPane.setHgap(40);
		songGridPane.setVgap(10);
		songGridPane.setPadding(new Insets(10));

		// Set up first row of grid pane
		songGridPane.add(new Label("Song name"), 2, 0);
		songGridPane.add(new Label("Artist"), 3, 0);
		songGridPane.add(new Label("Album"), 4, 0);
		songGridPane.add(new Label("Genre"), 5, 0);

		// Creating the page navigation bar under songs
		this.prevPage = new Button("<");
		this.gotoPage = new TextField();
		gotoPage.setPromptText(String.valueOf(Main.currPage));
		gotoPage.setAlignment(Pos.CENTER);
		this.nextPage = new Button(">");
		this.pageContentLabel = new Label("Page 1 of " + Main.numPages);
		this.pageModifier = new HBox(prevPage, gotoPage, nextPage, pageContentLabel);
		pageModifier.setAlignment(Pos.CENTER);

		this.songsText = new Label("Songs");

		this.setTop(songsText);
		this.setCenter(gridScrollPane);
		this.setBottom(pageModifier);

		songsText.setId("headerText");
		prevPage.setId("pageNavigationButtons");
		nextPage.setId("pageNavigationButtons");
	}

	public EventHandler<ActionEvent> getPlayButtonHandler(Scene mediaScene, String songName) { // Open music player when pressed
		EventHandler<ActionEvent> handler = new EventHandler<ActionEvent>() {
			public void handle(ActionEvent event) {
				Stage mediaStage = new Stage();
				mediaStage.setScene(mediaScene);
				mediaStage.show();
			}
		};

		return handler;
	}

	public GridPane getSongGrid() {
		return songGridPane;
	}

	public ScrollPane getSongScrollPane() {
		return gridScrollPane;
	}

	public Button getPrevPageButton() {
		return prevPage;
	}

	public Button getNextPageButton() {
		return nextPage;
	}

	public TextField getGotoPageField() {
		return gotoPage;
	}

	public Label getPageContentLabel() {
		return pageContentLabel;
	}
}
