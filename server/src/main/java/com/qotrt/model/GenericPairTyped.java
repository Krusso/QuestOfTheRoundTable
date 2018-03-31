package com.qotrt.model;

public class GenericPairTyped<T,E> {
	public T key;
	public E value;
	
	public GenericPairTyped() {};
	
	public GenericPairTyped(T key, E value) {
		this.key = key;
		this.value = value;
	}
	
	@Override
	public String toString() {
		return "GenericPair: [ key: " + key + " , value: " + value + " ]";
	}
}

