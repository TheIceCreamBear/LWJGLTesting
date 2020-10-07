package com.joseph.test.lwjgl3.models;

import com.joseph.test.lwjgl3.textures.Texture;

public class TexturedModel {
	private RawModel model;
	private Texture tex;
	
	/**
	 * Creates a new textured model with the given raw model as the like geometry and
	 * the given tex as the texture associated with this textured model
	 * @param model
	 * @param tex
	 */
	public TexturedModel(RawModel model, Texture tex) {
		this.model = model;
		this.tex = tex;
	}
	
	public RawModel getModel() {
		return this.model;
	}
	
	public Texture getTex() {
		return this.tex;
	}
}
