package com.qotrt.controller;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.ApplicationListener;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.messaging.SessionConnectEvent;

@Service
public class STOMPConnectEventListener implements ApplicationListener<SessionConnectEvent> {

	final static Logger logger = LogManager.getLogger(STOMPConnectEventListener.class);
	
	@Override
	public void onApplicationEvent(SessionConnectEvent event) {
		StompHeaderAccessor sha = StompHeaderAccessor.wrap(event.getMessage());

		
		String sessionId = sha.getSessionId();

		logger.info("Session id: " + sessionId);

	}
}