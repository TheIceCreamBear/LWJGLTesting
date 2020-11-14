package com.joseph.test.lwjgl3.textures;

/**
 * literally just stores 4 Terrain Textures in one class each representing a different idea,
 * where the base tex will always be shown, and the r tex will show if the blend map says so,
 * and same with the g tex and b tex
 */
public class TerrainTexturePack {
	private TerrainTexture baseTex;
	private TerrainTexture rTex;
	private TerrainTexture gTex;
	private TerrainTexture bTex;
	
	public TerrainTexturePack(TerrainTexture baseTex, TerrainTexture rTex, TerrainTexture gTex, TerrainTexture bTex) {
		this.baseTex = baseTex;
		this.rTex = rTex;
		this.gTex = gTex;
		this.bTex = bTex;
	}
	
	public TerrainTexture getBaseTex() {
		return this.baseTex;
	}
	
	public TerrainTexture getRTex() {
		return this.rTex;
	}
	
	public TerrainTexture getGTex() {
		return this.gTex;
	}
	
	public TerrainTexture getBTex() {
		return this.bTex;
	}
}
