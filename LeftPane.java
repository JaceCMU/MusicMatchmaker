import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;

public class LeftPane extends Pane{
	final double TRANSLATE_X = -400;
	
	GridPane buttonPane;
	Button musicMap;
	
	public LeftPane()
	{
		buttonPane = new GridPane();
		musicMap = new Button("Music Map");
		musicMap.setStyle("-fx-text-fill: white; -fx-font-size: 16");
		musicMap.setPrefSize(100, 100);
		buttonPane.add(musicMap, 0, 0);
		
		this.setTranslateX(TRANSLATE_X);
		
		this.getChildren().add(buttonPane);
	}
	
	public Button getMapButton()
	{
		return musicMap;
	}
}
