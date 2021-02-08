package com.joseph.test.lwjgl3.textures;

/**
 * semi complex texture class. stores the GL texture id asociated with it,
 * as well as the properties of shideDamping, reflectivity, transparency, and
 * faked lighting
 */
public class Texture {
	private int glTextureID;
	private int normalMapID;
	
	private float shineDamper = 1.0f;
	private float reflectivity = 0.0f;

	private boolean hasTransparency = false;
	private boolean useFakedLighting = false;
	
	private int numRows = 1;
	
	public Texture(int glTextureID) {
		this(glTextureID, 0);
	}
	
	public Texture(int glTextureID, int normalMapID) {
		this.glTextureID = glTextureID;
		this.normalMapID = normalMapID;
	}
	
	public int glTextureID() {
		return this.glTextureID;
	}
	
	public int normalMapID() {
		return this.normalMapID;
	}
	
	public float getReflectivity() {
		return this.reflectivity;
	}
	
	public float getShineDamper() {
		return this.shineDamper;
	}
	
	public boolean hasTransparency() {
		return this.hasTransparency;
	}
	
	public boolean useFakedLighting() {
		return this.useFakedLighting;
	}
	
	public int getNumRows() {
		return this.numRows;
	}
	
	public void setReflectivity(float reflectivity) {
		this.reflectivity = reflectivity;
	}
	
	public void setShineDamper(float shineDamper) {
		this.shineDamper = shineDamper;
	}
	
	public void setHasTransparency(boolean hasTransparency) {
		this.hasTransparency = hasTransparency;
	}
	
	public void setUseFakedLighting(boolean useFakedLighting) {
		this.useFakedLighting = useFakedLighting;
	}
	
	public void setNumRows(int numRows) {
		this.numRows = numRows;
	}
}
