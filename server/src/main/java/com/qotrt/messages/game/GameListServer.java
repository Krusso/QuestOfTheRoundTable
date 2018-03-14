package com.qotrt.messages.game;

import java.util.ArrayList;

import com.qotrt.game.Game;
import com.qotrt.messages.Message;
import com.qotrt.views.GameDisplay;

// Server
public class GameListServer extends Message  {

	private GameDisplay[] games;

	public GameDisplay[] getGames() {
		return games;
	}

	public void setGames(GameDisplay[] games) {
		this.games = games;
	}
	
	public void setGamesWithGameList(ArrayList<Game> gamelist) {
		games = new GameDisplay[gamelist.size()];
		for(int i = 0; i < gamelist.size(); i++) {
			games[i] = new GameDisplay(gamelist.get(i).getUUID(),
					gamelist.get(i).getPlayerCount(),
					gamelist.get(i).getPlayerCapacity());
		}
	}

	@Override
	public void setMessage() {
		this.message = MESSAGETYPES.LISTSERVER;
	}
}