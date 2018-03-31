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
		ArrayList<GameDisplay> gameTemp = new ArrayList<GameDisplay>();
		for(int i = 0; i < gamelist.size(); i++) {
			if(gamelist.get(i).getPlayerCount() + gamelist.get(i).getAICount() < gamelist.get(i).getPlayerCapacity()) {
				gameTemp.add(new GameDisplay(gamelist.get(i).getUUID(),
											 gamelist.get(i).getPlayerCount(),
											 gamelist.get(i).getGameName(),
											 gamelist.get(i).getPlayerCapacity(),
											 gamelist.get(i).getAICount()));
			}
		}
		games = new GameDisplay[gameTemp.size()];
		games = gameTemp.toArray(games);
	}

	@Override
	public void setMessage() {
		this.messageType = MESSAGETYPES.LISTSERVER;
	}
}