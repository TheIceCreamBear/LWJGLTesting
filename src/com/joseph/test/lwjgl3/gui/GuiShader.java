package com.joseph.test.lwjgl3.gui;

import org.joml.Matrix4f;

import com.joseph.test.lwjgl3.shaders.ShaderProgram;

/**
 * throws shade on those guis
 */
public class GuiShader extends ShaderProgram {
	private static final String VERTEX_FILE = "src/com/joseph/test/lwjgl3/gui/gui.vert";
	private static final String FRAGMENT_FILE = "src/com/joseph/test/lwjgl3/gui/gui.frag";
	
	private int tMatrixLocation;
	
	public GuiShader() {
		super(VERTEX_FILE, FRAGMENT_FILE);
	}
	
	@Override
	protected void getUniformLocations() {
		this.tMatrixLocation = super.getUniformLocation("tMatrix");
	}
	
	@Override
	protected void bindAttributes() {
		super.bindAttribute(0, "position");
	}

	public void loadTransformation(Matrix4f mat) {
		super.loadMatrix4(tMatrixLocation, mat);
	}
}
