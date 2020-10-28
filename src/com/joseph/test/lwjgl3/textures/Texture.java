package com.joseph.test.lwjgl3.textures;

/**
 * semi complex texture class. stores the GL texture id asociated with it,
 * as well as the properties of shideDamping, reflectivity, transparency, and
 * faked lighting
 */
public class Texture {
	private int glTextureID;
	
	private float shineDamper = 1.0f;
	private float reflectivity = 0.0f;

	private boolean hasTransparency = false;
	private boolean useFakedLighting = false;
	
	public Texture(int glTextureID) {
		this.glTextureID = glTextureID;
	}
	
	public int glTextureID() {
		return this.glTextureID;
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
}
