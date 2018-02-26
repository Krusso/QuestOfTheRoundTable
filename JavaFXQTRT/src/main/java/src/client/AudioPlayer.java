package src.client;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

public class AudioPlayer {

	final static Logger logger = LogManager.getLogger(AudioPlayer.class);
	Media sound;
	MediaPlayer mediaPlayer;
	public AudioPlayer(String musicFile) {
		setNewMusic(musicFile, true);
	}
	public AudioPlayer(String musicFile, boolean isLoop) {
		setNewMusic(musicFile, isLoop);
	}
	
	public void setNewMusic(String musicFile, boolean isLoop) {
		if(mediaPlayer != null) {
			mediaPlayer.stop();
		}
		
		sound = new Media(ClassLoader.getSystemResource(musicFile).toExternalForm());
		mediaPlayer= new MediaPlayer(sound);
		if(isLoop) {
			mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
			mediaPlayer.setAutoPlay(true);
			logger.info("Set " + musicFile + " audio to indefinite loop");
		}
	}
	
	public void play() {
		stop();
		mediaPlayer.play();
		logger.info("Playing audio: " + sound.toString());
	}
	public void stop() {
		mediaPlayer.stop();
		logger.info("Stopped audio: " + sound.toString());
	}
	public void setVolume(double value) {
		mediaPlayer.setVolume(value);
		logger.info("Set audio volume  [" + mediaPlayer.toString() + "] to:" +  mediaPlayer.getVolume() );
	}
	

}
