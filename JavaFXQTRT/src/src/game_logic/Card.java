package src.game_logic;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public abstract class Card {
	private String name;
	private Image img;
	private ImageView imgView;
	
	public Card(String name) {
		this.name = name;
		
	}
	public Card(String name, String path) {
		this.name = name;
	    img = new Image(path);
		imgView = new ImageView();
		imgView.setImage(img);
		imgView.setFitWidth(100);
		imgView.setFitHeight(150);
	}
	public ImageView getImageView() {
		return imgView;
	}
	public String getName() {
		return this.name;
	}
	
	@Override
	public String toString() {
		return this.name;
	}
}