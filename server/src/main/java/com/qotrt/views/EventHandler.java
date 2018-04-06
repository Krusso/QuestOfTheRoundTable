package com.qotrt.views;

import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import java.util.function.Consumer;
import java.util.function.Function;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.qotrt.model.GenericPairTyped;

public class EventHandler {
	final static Logger logger = LogManager.getLogger(EventHandler.class);
	
	protected ArrayList<GenericPairTyped<Function<PropertyChangeEvent, Boolean>, Consumer<PropertyChangeEvent>>> events = 
			new ArrayList<GenericPairTyped<Function<PropertyChangeEvent, Boolean>, Consumer<PropertyChangeEvent>>>();
	
	protected void handlePropertyChangeEvent(PropertyChangeEvent x) {
		logger.info("event got fired: " + x.getPropertyName());
		
		events.stream().filter(i -> {
			return i.key.apply(x);
		}).forEach(i -> {
			logger.info("event got consumed: " + x.getPropertyName() + " lambda: " + i.value);
			i.value.accept(x);
		});
	}
}
