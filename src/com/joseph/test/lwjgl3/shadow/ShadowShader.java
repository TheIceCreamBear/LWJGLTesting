package com.joseph.test.lwjgl3.shadow;

import org.joml.Matrix4f;

import com.joseph.test.lwjgl3.shaders.ShaderProgram;

public class ShadowShader extends ShaderProgram {
	
	private static final String VERTEX_FILE = "src/com/joseph/test/lwjgl3/shadow/shadow.vert";
	private static final String FRAGMENT_FILE = "src/com/joseph/test/lwjgl3/shadow/shadow.frag";
	
	private int mvpMatrixLocation;
	
	protected ShadowShader() {
		super(VERTEX_FILE, FRAGMENT_FILE);
	}
	
	@Override
	protected void getUniformLocations() {
		mvpMatrixLocation = super.getUniformLocation("mvpMatrix");
		
	}
	
	protected void loadMvpMatrix(Matrix4f mvpMatrix) {
		super.loadMatrix4(mvpMatrixLocation, mvpMatrix);
	}
	
	@Override
	protected void bindAttributes() {
		super.bindAttribute(0, "in_position");
	}
	
}
