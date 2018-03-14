package com.qotrt.model;

public class GenericPair {
	public Object key;
	public Object value;
	
	public GenericPair() {};
	
	public GenericPair(Object key, Object value) {
		this.key = key;
		this.value = value;
	}
	
	@Override
	public String toString() {
		return "GenericPair: [ key: " + key + " , value: " + value + " ]";
	}
}

