package src.client;

import java.io.File;

import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class AudioPlayer {
	private static File resourceDirectory = new File("src/main/resources/audio");
	private String musicFile;
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
		this.musicFile = musicFile;
		sound = new Media(new File(resourceDirectory + "/" + musicFile).toURI().toString());
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
