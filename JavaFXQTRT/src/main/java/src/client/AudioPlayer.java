package src.client;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class AudioPlayer {

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
		}
	}
	
	public void play() {
		mediaPlayer.play();
	}
	public void stop() {
		mediaPlayer.stop();
	}
	

}
