package com.joseph.test.lwjgl3.util;

/**
 * Class that represents two things with the same type, usually a point in 2D space
 * @author Joseph
 *
 * @param <T> - the type of the points variables
 */
public class Point<T> {
	private T x;
	private T y;
	
	public Point(T x, T y) {
		this.x = x;
		this.y = y;
	}
	
	public T getX() {
		return this.x;
	}
	
	public T getY() {
		return this.y;
	}
}
