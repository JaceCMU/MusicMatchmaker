
// MediaPlayerUI.java
import javafx.beans.binding.Bindings;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import java.io.File;
import java.net.MalformedURLException;

public class MediaPlayerUI {
    private BorderPane root;
    private MusicPlayer musicPlayer;
    private MediaPlayer mediaPlayer;
    private boolean isPlaying = false;
    private Label timeLabel;
    private Slider seekSlider;
    private Text songTitle;
    private Text artist;
    private ImageView albumArt;

    // Constructor
    public MediaPlayerUI(MusicPlayer musicPlayer) {
        this.musicPlayer = musicPlayer;
        root = new BorderPane();
        root.setStyle("-fx-background-color: rgb(45, 45, 45);");

        initializeUI();
    }

    //This is Method to initialize the UI components and Create UI components for displaying song information
    private void initializeUI() {
        songTitle = new Text("Song Title");
        songTitle.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        artist = new Text("Artist");
        artist.setFont(Font.font("Arial", 18));
        VBox titleArtistBox = new VBox(songTitle, artist);
        titleArtistBox.setAlignment(Pos.CENTER);

        // This displays album art
        Image defaultAlbumArt = new Image("file:///C:/New%20folder%20(2)/CPS240/src/resources/download.jpeg");
        albumArt = new ImageView(defaultAlbumArt);
        albumArt.setFitWidth(200);
        albumArt.setFitHeight(200);
        VBox centerContent = new VBox(albumArt, titleArtistBox);
        centerContent.setSpacing(20);
        centerContent.setAlignment(Pos.CENTER);

        //This creates UI components for controlling playback
        seekSlider = new Slider();
        seekSlider.setPrefWidth(300);
        timeLabel = new Label("0:00 / 0:00");
        timeLabel.setStyle("-fx-text-fill:black;");
        HBox seekBarBox = new HBox(seekSlider, timeLabel);
        seekBarBox.setAlignment(Pos.CENTER);

        //this creates control buttons
        Button playPauseButton = new Button("");
        playPauseButton.setGraphic(new Circle(10, Color.BLACK));
        Button openButton = new Button("Open");
        Button stopButton = new Button("Stop");
        HBox controlButtons = new HBox(openButton, playPauseButton, stopButton);
        controlButtons.setSpacing(20);
        controlButtons.setAlignment(Pos.CENTER);

       
        VBox contentBox = new VBox(centerContent, seekBarBox, controlButtons);
        contentBox.setSpacing(20);
        contentBox.setAlignment(Pos.CENTER);

      
        root.setCenter(contentBox);
        BorderPane.setMargin(contentBox, new Insets(20));

        //event handlers to buttons
        openButton.setOnAction(e -> openFile());
        playPauseButton.setOnAction(e -> togglePlay());
        stopButton.setOnAction(e -> stop());
    }

    // this is the Method to open a music file
    private void openFile() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(new File("MusicFiles"));
        fileChooser.setTitle("Open MP3 File");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("MP3 Files", "*.mp3")
        );
        File selectedFile = fileChooser.showOpenDialog(null);
        if (selectedFile != null) {
            try {
                musicPlayer.loadMedia(selectedFile.toURI().toURL().toString());
                mediaPlayer = musicPlayer.getMediaPlayer();
                updateTitleArtist(selectedFile.getName());
                mediaPlayer.play();
                isPlaying = true;
                bindMediaPlayerToUI();
            } catch (MalformedURLException e) {
                System.out.println("Error loading file: " + e.getMessage());
            }
        }
    }

    //this is the method to bind MediaPlayer properties to UI
    private void bindMediaPlayerToUI() {
        mediaPlayer.currentTimeProperty().addListener((observable, oldValue, newValue) -> {
            if (!seekSlider.isValueChanging()) {
                seekSlider.setValue(newValue.toSeconds());
            }
        });

        mediaPlayer.totalDurationProperty().addListener((observable, oldValue, newValue) -> {
            seekSlider.setMax(newValue.toSeconds());
        });

        timeLabel.textProperty().bind(Bindings.createStringBinding(() ->
                        formatTime(mediaPlayer.getCurrentTime(), mediaPlayer.getTotalDuration()),
                mediaPlayer.currentTimeProperty(), mediaPlayer.totalDurationProperty()));
    }

    // this updates song title and artist
    void updateTitleArtist(String title) {
        songTitle.setText(title);
    }

    // this toggles play/pause
    private void togglePlay() {
        if (mediaPlayer != null) {
            if (isPlaying) {
                mediaPlayer.pause();
                isPlaying = false;
            } else {
                mediaPlayer.play();
                isPlaying = true;
            }
        }
    }

    // This is a method to stop playback
    private void stop() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            isPlaying = false;
        }
    }

    // This is a method to format time duration
    private String formatTime(javafx.util.Duration elapsed, javafx.util.Duration duration) {
        int elapsedSeconds = (int) elapsed.toSeconds();
        int elapsedMinutes = elapsedSeconds / 60;
        elapsedSeconds -= elapsedMinutes * 60;
        int durationSeconds = (int) duration.toSeconds();
        int durationMinutes = durationSeconds / 60;
        durationSeconds -= durationMinutes * 60;
        return String.format("%d:%02d / %d:%02d", elapsedMinutes, elapsedSeconds, durationMinutes, durationSeconds);
    }

    // Getter method for the root layout
    public BorderPane getRoot() {
        return root;
    }
}
