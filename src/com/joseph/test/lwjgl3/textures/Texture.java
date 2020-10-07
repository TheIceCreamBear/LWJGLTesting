package com.joseph.test.lwjgl3.textures;

public class Texture {
	private int glTextureID;
	
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
}
