package com.joseph.test.lwjgl3.particle;

public class ParticleTexture {
	private int id;
	private int numRows;
	
	public ParticleTexture(int id, int numRows) {
		this.id = id;
		this.numRows = numRows;
	}
	
	public int getId() {
		return this.id;
	}
	
	public int getNumRows() {
		return this.numRows;
	}
}
