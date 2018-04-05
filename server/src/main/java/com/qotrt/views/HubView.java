package com.qotrt.views;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.function.Consumer;
import java.util.function.Function;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import com.qotrt.messages.game.GameJoinServer;
import com.qotrt.messages.game.GameStartServer;
import com.qotrt.model.GenericPair;
import com.qotrt.model.GenericPairTyped;
import com.qotrt.model.UIPlayer;

public class HubView extends Observer implements PropertyChangeListener {

	final static Logger logger = LogManager.getLogger(HubView.class);

	public HubView(SimpMessagingTemplate messagingTemplate) {
		super(messagingTemplate, new ArrayList<UIPlayer>());

		Function<PropertyChangeEvent, Boolean> funcF = x -> x.getPropertyName().equals("players");
		Consumer<PropertyChangeEvent> funcC = x -> playerJoinedGame(mapper.convertValue(x.getNewValue(), UIPlayer[].class));

		Function<PropertyChangeEvent, Boolean> funcF1 = x -> x.getPropertyName().equals("gameStart");
		Consumer<PropertyChangeEvent> funcC1 = x -> startGame();

		events.add(new GenericPairTyped<>(funcF, funcC));
		events.add(new GenericPairTyped<>(funcF1, funcC1));
	}

	public void playerJoinedGame(UIPlayer[] UIPlayers) {
		logger.info("sending message: " + Arrays.toString(UIPlayers));
		GameJoinServer gjs = new GameJoinServer();
		gjs.setPlayers(Arrays.stream(UIPlayers).
				map(i -> new GenericPair(i.getPlayerName(), i.getShieldNumber())).
				toArray(GenericPair[]::new));

		sendMessage(gjs);
	}

	public void propertyChange(PropertyChangeEvent evt) {
		handleEvent(evt);
	}

	private void startGame() {
		sendMessage(new GameStartServer());
	}

}