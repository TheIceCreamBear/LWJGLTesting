package com.joseph.test.lwjgl3.textures;

/**
 * very basic texture class, kind really only exists so that we dont have all the 
 * other overhead of a regular texture, but maybe there is a better way to do that
 */
public class TerrainTexture {
	private int glTextureID;

	public TerrainTexture(int glTextureID) {
		this.glTextureID = glTextureID;
	}
	
	public TerrainTexture(Texture tex) {
		this.glTextureID = tex.glTextureID();
	}
	
	public int glTextureID() {
		return this.glTextureID;
	}	
}
