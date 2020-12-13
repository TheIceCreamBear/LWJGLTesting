package com.joseph.test.lwjgl3.util;

/**
 * Class that represents a pair of 2 things, where A maps to B, or A and B are related
 * @author Joseph
 *
 * @param <A> - type of the first parameter
 * @param <B> - type of the second parameter
 */
public class Pair<A, B> {
	private A a;
	private B b;
	
	public Pair(A a, B b) {
		this.a = a;
		this.b = b;
	}
	
	public A getA() {
		return this.a;
	}
	
	public B getB() {
		return this.b;
	}
}
