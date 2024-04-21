
// MusicPlayer.java
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import java.net.MalformedURLException;
import java.net.URL;

public class MusicPlayer {
    private MediaPlayer mediaPlayer;

    // This loads media from a file path
    public void loadMedia(String filePath) throws MalformedURLException {
        Media media = new Media(new URL(filePath).toExternalForm());
        mediaPlayer = new MediaPlayer(media);
    }

    // Getter method for MediaPlayer instance
    public MediaPlayer getMediaPlayer() {
        return mediaPlayer;
    }

    // Method to start playback
    public void play() {
        if (mediaPlayer != null) {
            mediaPlayer.play();
        }
    }

    // Method to pause playback
    public void pause() {
        if (mediaPlayer != null) {
            mediaPlayer.pause();
        }
    }

    // Method to stop playback
    public void stop() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
        }
    }
}
