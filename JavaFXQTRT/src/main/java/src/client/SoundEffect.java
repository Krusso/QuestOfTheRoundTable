package src.client;

import java.io.File;

import javafx.scene.media.AudioClip;

public class SoundEffect {
	private AudioClip clip;
	public SoundEffect(String musicFile) {
		clip = new AudioClip(ClassLoader.getSystemResource(musicFile).toExternalForm());
	}
	public void play() {
		clip.play();
	}
	public void stop() {
		clip.stop();
	}

}
