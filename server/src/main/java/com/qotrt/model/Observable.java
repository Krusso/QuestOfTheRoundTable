package com.qotrt.model;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.concurrent.CountDownLatch;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public abstract class Observable {
	
	final static Logger logger = LogManager.getLogger(Observable.class);

	private PropertyChangeSupport pcs = new PropertyChangeSupport(this);
	
	public void subscribe(PropertyChangeListener pcl) {
		logger.info("pcs: " + pcs + " pcl: " + pcl);
		pcs.addPropertyChangeListener(pcl);
	}
	
	public void unsubscribe(PropertyChangeListener pcl) {
		pcs.removePropertyChangeListener(pcl);
	}
	
	public void fireEvent(String propertyName, Object oldValue, Object newValue) {
		logger.info("firing event: " + propertyName);
		pcs.firePropertyChange(propertyName, oldValue, newValue);
	}
	
	protected void checkIfCanOpenLatch(CountDownLatch cdl, int variable) {
		if(variable == 0) {
			cdl.countDown();
		}
	}
}
