package src.game_client;

import java.io.FileInputStream;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Card {
	
	
	Image img;
	ImageView cardImageView;
	public Card(String path) throws Exception {
    	FileInputStream input = new FileInputStream(path);
    	Image img = new Image(input , 100, 150, false, true);
    	cardImageView = new ImageView();
    	cardImageView.setImage(img);
	}
	
	
	public ImageView getCardImageView() {
		return cardImageView;
	}

}
