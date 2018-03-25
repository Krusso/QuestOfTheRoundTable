package com.qotrt.model;

public class GenericPair2<T,E> {
	public T key;
	public E value;
	
	public GenericPair2() {};
	
	public GenericPair2(T key, E value) {
		this.key = key;
		this.value = value;
	}
	
	@Override
	public String toString() {
		return "GenericPair: [ key: " + key + " , value: " + value + " ]";
	}
}

