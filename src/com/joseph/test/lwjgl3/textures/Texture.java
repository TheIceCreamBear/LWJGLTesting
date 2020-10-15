package com.joseph.test.lwjgl3.textures;

public class Texture {
	private int glTextureID;
	
	private float shineDamper = 1.0f;
	private float reflectivity = 0.0f;
	
	/**
	 * this is a basic class rn and it just stores the texture id that 
	 * is associated with this texture because rn there isnt much need for 
	 * anything else
	 * @param glTextureID
	 */
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
	
	public void setReflectivity(float reflectivity) {
		this.reflectivity = reflectivity;
	}
	
	public void setShineDamper(float shineDamper) {
		this.shineDamper = shineDamper;
	}
}
