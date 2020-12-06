package com.joseph.test.lwjgl3.gui;

import org.joml.Vector2f;

/**
 * bruh gui tex class
 */
public class GuiTexture {
	private int glTextureID;
	private Vector2f pos;
	private Vector2f scale;
	
	public GuiTexture(int id, Vector2f pos, Vector2f scale) {
		this.glTextureID = id;
		this.pos = pos;
		this.scale = scale;
	}
	
	public int getGlTextureID() {
		return this.glTextureID;
	}
	
	public Vector2f getPos() {
		return this.pos;
	}
	
	public Vector2f getScale() {
		return this.scale;
	}
}
