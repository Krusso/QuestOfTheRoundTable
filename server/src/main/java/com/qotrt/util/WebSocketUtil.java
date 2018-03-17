package com.qotrt.util;

import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageType;
import org.springframework.util.MimeTypeUtils;

public class WebSocketUtil {

	public static MessageHeaders createHeaders(String sessionId) {
		SimpMessageHeaderAccessor headerAccessor = SimpMessageHeaderAccessor.create(SimpMessageType.MESSAGE);
		headerAccessor.setSessionId(sessionId);
		headerAccessor.setLeaveMutable(true);
		headerAccessor.setContentType(MimeTypeUtils.APPLICATION_JSON);
		return headerAccessor.getMessageHeaders();
	}
	
}
