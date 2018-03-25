package com.qotrt.views;

import java.beans.PropertyChangeListener;
import java.util.ArrayList;

import org.springframework.messaging.simp.SimpMessagingTemplate;

import com.qotrt.model.UIPlayer;
import com.qotrt.util.WebSocketUtil;

public abstract class View implements PropertyChangeListener {

	protected View() {}
	
	public View(SimpMessagingTemplate messagingTemplate) {
		this.messagingTemplate = messagingTemplate;
		this.sendList = new ArrayList<UIPlayer>();
	}
	
	private SimpMessagingTemplate messagingTemplate;
	private ArrayList<UIPlayer> sendList;
	
	public void addWebSocket(UIPlayer player) {
		sendList.add(player);
	}
	
	protected void sendMessage(String destination, Object e) {
		System.out.println("sending message: " + e + " to desitination: " + destination);

		sendList.forEach(i -> {
			messagingTemplate.convertAndSendToUser(i.getSessionID(),
					"/queue/response",
					e,
					WebSocketUtil.createHeaders(i.getSessionID()));
		});
	}
	
}
