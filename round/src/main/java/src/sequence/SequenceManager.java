package src.sequence;

import java.util.concurrent.LinkedBlockingQueue;

import src.game_logic.BoardModel;
import src.player.PlayerManager;

public abstract class SequenceManager {

	public abstract void start(LinkedBlockingQueue<String> actions, PlayerManager pm, BoardModel bm);
}