package com.joseph.test.lwjgl3.models;

public class RawModel {
	private int vaoID;
	private int vertexCount;
	
	/**
	 * creates a new raw model, which knows the id of the vao that holds it's vertices
	 * and knows how many there are, which is basically all a raw model is, so there 
	 * will not be anymore documentation of this class because LOL why would i 
	 * put comments on a getter
	 * @param vaoID
	 * @param vertexCount
	 */
	public RawModel(int vaoID, int vertexCount) {
		this.vaoID = vaoID;
		this.vertexCount = vertexCount;
	}
	
	public int getVaoID() {
		return this.vaoID;
	}
	
	public int getVertexCount() {
		return this.vertexCount;
	}
}
