package src.socket;

public class GameController implements Subscriber  {


	public GameController(GameModel gm) {
		gm.subscribe(this);
		
	}

	@Override
	public void update(int x) {

	}


}
