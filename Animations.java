import javafx.animation.Animation;
import javafx.animation.FadeTransition;
import javafx.animation.Interpolator;
import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.util.Duration;

public class Animations {
	
	  
	    
	    public ScaleTransition scaleDown(VBox mainBox) {
	    
	        // Scale down animation
	        ScaleTransition scaleDownTransition = new ScaleTransition(Duration.seconds(2), mainBox);
	        scaleDownTransition.setByX(-1);
	        scaleDownTransition.setByY(-1);
	        scaleDownTransition.setAutoReverse(true);
	        scaleDownTransition.play();
	        return scaleDownTransition;
	    }

	    public ScaleTransition scaleUp(VBox mainBox) {
	        // scale up animation
	    	 ScaleTransition scaleUpTransition = new ScaleTransition(Duration.seconds(2), mainBox);
	    	    scaleUpTransition.setToX(1); // Set to original scale X value
	    	    scaleUpTransition.setToY(1); // Set to original scale Y value
	    	    scaleUpTransition.setAutoReverse(true);
	    	    scaleUpTransition.play();
	    	    return scaleUpTransition;
	    }
	


	protected TranslateTransition moveUp(VBox mainBox) {
	    // TranslateTransition for moving the main box up
	    TranslateTransition moveUpTransition = new TranslateTransition(Duration.seconds(2), mainBox);
	    moveUpTransition.setToY(-200);
	    moveUpTransition.play();
	    return moveUpTransition;
	}

	protected TranslateTransition moveDown(VBox mainBox) {
	    // TranslateTransition for moving the main box down
	    TranslateTransition moveDownTransition = new TranslateTransition(Duration.seconds(0), mainBox);
	    moveDownTransition.setToY(0);
	    moveDownTransition.play();
	    return moveDownTransition;
	}

	protected FadeTransition fadeIn(VBox mainBox) {
		FadeTransition fadeTransition = new FadeTransition(Duration.seconds(4), mainBox);
		 fadeTransition.setFromValue(0); // Starting opacity
	        fadeTransition.setToValue(1);   // Ending opacity
	        fadeTransition.play();
		return fadeTransition;
	}
	
	
	//FOR STAR ANIMATIONS
	
	protected ScaleTransition scaleUp(ImageView ivStars) {
		ScaleTransition scaleTransition = new ScaleTransition(Duration.seconds(3), ivStars);
		scaleTransition.setByY(500);
		scaleTransition.setByX(500);
		scaleTransition.setAutoReverse(false);
		scaleTransition.play();
		return scaleTransition;
	}

	protected ScaleTransition scaleUp(Text txtStars) {
		ScaleTransition scaleTransition = new ScaleTransition(Duration.seconds(3), txtStars);
		scaleTransition.setByY(300);
		scaleTransition.setByX(300);
		scaleTransition.setAutoReverse(false);
		scaleTransition.play();
		return scaleTransition;
	}
	protected ScaleTransition scaleDown(ImageView ivStars) {
		ScaleTransition scaleTransition = new ScaleTransition(Duration.seconds(3), ivStars);
		scaleTransition.setByY(-500);
		scaleTransition.setByX(-500);
		scaleTransition.setAutoReverse(false);
		scaleTransition.play();
		return scaleTransition;
	}

	protected ScaleTransition scaleDown(Text txtStars) {
		ScaleTransition scaleTransition = new ScaleTransition(Duration.seconds(3), txtStars);
		scaleTransition.setByY(-300);
		scaleTransition.setByX(-300);
		scaleTransition.setAutoReverse(false);
		scaleTransition.play();
		return scaleTransition;
	}
	
	//FOR RESET BUTTON ANIMATION
	protected FadeTransition fadeIn(Button button) {
		FadeTransition fadeTransition = new FadeTransition(Duration.seconds(3), button);
		 fadeTransition.setFromValue(0); // Starting opacity
	        fadeTransition.setToValue(1);   // Ending opacity
	        fadeTransition.play();
		return fadeTransition;
	}
	protected FadeTransition fadeOut(Button button) {
		FadeTransition fadeTransition = new FadeTransition(Duration.seconds(3), button);
		 fadeTransition.setFromValue(1); // Starting opacity
	        fadeTransition.setToValue(0);   // Ending opacity
	        fadeTransition.play();
		return fadeTransition;
	}

	
	
	
	// FLOATING ANIMATIONS
	protected TranslateTransition floats(ImageView ivStars) {
		Duration duration = Duration.seconds(4); // duration for one cycle of floating animation

		// create a translate transition for circular motion
		TranslateTransition translateTransition = new TranslateTransition(duration, ivStars);

		// duration of animation
		translateTransition.setCycleCount(TranslateTransition.INDEFINITE);

		// define the circular motion using a listener
		translateTransition.currentTimeProperty().addListener((obs, oldTime, newTime) -> {
			double angle = newTime.toSeconds() * 360 / duration.toSeconds(); // calculate angle
			double x = Math.cos(Math.toRadians(angle)) * 0.1; // calculate new x position
			double y = Math.sin(Math.toRadians(angle)) * 0.1; // calculate new y position
			ivStars.setTranslateX(ivStars.getTranslateX() + x);
			ivStars.setTranslateY(ivStars.getTranslateY() + y);
		});
		translateTransition.play();

		return translateTransition;
	}

	protected TranslateTransition floats(Text txtStars) {
		Duration duration = Duration.seconds(4); // duration for one cycle of floating animation

		// create a translate transition for circular motion
		TranslateTransition translateTransition = new TranslateTransition(duration, txtStars);

		// duration of animation
		translateTransition.setCycleCount(TranslateTransition.INDEFINITE);

		// define the circular motion using a listener
		translateTransition.currentTimeProperty().addListener((obs, oldTime, newTime) -> {
			double angle = newTime.toSeconds() * 360 / duration.toSeconds(); // calculate angle
			double x = Math.cos(Math.toRadians(angle)) * 0.1; // calculate new x position
			double y = Math.sin(Math.toRadians(angle)) * 0.1; // calculate new y position
			txtStars.setTranslateX(txtStars.getTranslateX() + x);
			txtStars.setTranslateY(txtStars.getTranslateY() + y);
		});
		translateTransition.play();

		return translateTransition;
	}

	protected TranslateTransition floats2(ImageView ivStars) {
		TranslateTransition translateTransition = new TranslateTransition(Duration.seconds(2), ivStars);
		translateTransition.setByY(-5); // Change this value to adjust the floating height
		translateTransition.setByX(5);
		translateTransition.setCycleCount(Animation.INDEFINITE);
		translateTransition.setAutoReverse(true);
		translateTransition.setInterpolator(Interpolator.EASE_BOTH);
		translateTransition.play();
		return translateTransition;
	}

	protected TranslateTransition floats2(Text txtStars) {
		TranslateTransition translateTransition = new TranslateTransition(Duration.seconds(2), txtStars);
		translateTransition.setByY(-5); // Change this value to adjust the floating height
		translateTransition.setByX(5);
		translateTransition.setCycleCount(Animation.INDEFINITE);
		translateTransition.setAutoReverse(true);
		translateTransition.setInterpolator(Interpolator.EASE_BOTH);
		translateTransition.play();
		return translateTransition;
	}

	protected TranslateTransition floats3(ImageView ivStars) {
		TranslateTransition translateTransition = new TranslateTransition(Duration.seconds(2), ivStars);
		translateTransition.setByY(-5); // Change this value to adjust the floating height
		translateTransition.setByX(-5);
		translateTransition.setCycleCount(Animation.INDEFINITE);
		translateTransition.setAutoReverse(true);
		translateTransition.setInterpolator(Interpolator.EASE_BOTH);
		translateTransition.play();
		return translateTransition;
	}

	protected TranslateTransition floats3(Text txtStars) {
		TranslateTransition translateTransition = new TranslateTransition(Duration.seconds(2), txtStars);
		translateTransition.setByY(-5); // Change this value to adjust the floating height
		translateTransition.setByX(-5);
		translateTransition.setCycleCount(Animation.INDEFINITE);
		translateTransition.setAutoReverse(true);
		translateTransition.setInterpolator(Interpolator.EASE_BOTH);
		translateTransition.play();
		return translateTransition;
	}

	protected TranslateTransition floats4(ImageView ivStars) {
		TranslateTransition translateTransition = new TranslateTransition(Duration.seconds(2), ivStars);
		translateTransition.setByY(5); // Change this value to adjust the floating height
		translateTransition.setByX(-5);
		translateTransition.setCycleCount(Animation.INDEFINITE);
		translateTransition.setAutoReverse(true);
		translateTransition.setInterpolator(Interpolator.EASE_BOTH);
		translateTransition.play();
		return translateTransition;
	}

	protected TranslateTransition floats4(Text txtStars) {
		TranslateTransition translateTransition = new TranslateTransition(Duration.seconds(2), txtStars);
		translateTransition.setByY(5); // Change this value to adjust the floating height
		translateTransition.setByX(-5);
		translateTransition.setCycleCount(Animation.INDEFINITE);
		translateTransition.setAutoReverse(true);
		translateTransition.setInterpolator(Interpolator.EASE_BOTH);
		translateTransition.play();
		return translateTransition;
	}

	

}
