package com.qotrt.confirmation;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.function.Consumer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.qotrt.gameplayer.Player;
import com.qotrt.model.Observable;

public abstract class Confirmation extends Observable implements PropertyChangeListener {
	
	final static Logger logger = LogManager.getLogger(Confirmation.class);
	
	protected CountDownLatch cdl = new CountDownLatch(1);
	protected String eventName;
	protected String acceptEventName;
	protected String declineEventName;
	protected int backingInt;
	protected Mode racer;
	protected List<Player> accepted = new ArrayList<Player>();

	
	@Override
	//propagating events up
	public void propertyChange(PropertyChangeEvent arg0) {
		fireEvent(arg0.getPropertyName(), arg0.getOldValue(), arg0.getNewValue());
	}
	
	public Confirmation(String eventName, 
			String acceptEventName, 
			String declineEventName,
			boolean racing) {
		this.eventName = eventName;
		this.acceptEventName = acceptEventName;
		this.declineEventName = declineEventName;
		if(racing) {
			racer = new Racer();
		} else {
			racer = new HotSeat();
		}
		racer.subscribe(this);
	}

	public void start(List<Player> toAsk) {
		cdl = new CountDownLatch(1);
		accepted = new ArrayList<Player>();
		backingInt = toAsk.size();
		racer.start(toAsk, eventName);
	}

	public void start(List<Player> toAsk, Object obj) {
		cdl = new CountDownLatch(1);
		accepted = new ArrayList<Player>();
		backingInt = toAsk.size();
		racer.start(toAsk, obj, eventName);
	}
	
	public abstract boolean accept(Player player, String attempt, String success, String failure);

	public void decline(Player player, String attempt, String success, String failure) {
		logger.info(attempt);
		if(backingInt > 0) {
			backingInt--;
			logger.info(success);
			if(declineEventName != null) {
				fireEvent(declineEventName, null, player);
			}
			racer.nextPlayerToAsk();
			checkIfCanOpenLatch(cdl, backingInt);
		} else {
			logger.info(failure);
		}
	}
	
	public void forAllPlayers(Consumer<Player> c) {
		racer.forAllPlayers(c);
	}

	public boolean can() {
		return backingInt > 0;
	}

	public List<Player> get(){
		backingInt = -1;
		return this.accepted;
	}

	public CountDownLatch getCountDownLatch() {
		return this.cdl;
	}
}
