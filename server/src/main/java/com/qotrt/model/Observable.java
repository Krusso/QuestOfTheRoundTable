package com.qotrt.model;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public abstract class Observable {

	private PropertyChangeSupport pcs = new PropertyChangeSupport(this);
	
	public void subscribe(PropertyChangeListener pcl) {
		pcs.addPropertyChangeListener(pcl);
	}
	
	public void unsubscribe(PropertyChangeListener pcl) {
		pcs.removePropertyChangeListener(pcl);
	}
	
	public void fireEvent(String propertyName, Object oldValue, Object newValue) {
		pcs.firePropertyChange(propertyName, oldValue, newValue);
	}
	
}
