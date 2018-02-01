package com.example.model;

public interface Condition<T> {
	
	boolean Test(T a, T b);
	
}
