package com.joseph.test.lwjgl3.shadows;

import org.joml.Matrix4f;

import com.joseph.test.lwjgl3.shaders.ShaderProgram;

public class ShadowShader extends ShaderProgram {

	private static final String VERTEX_FILE = "/com/joseph/test/lwjgl3/shadows/shadow.vert";
	private static final String FRAGMENT_FILE = "/com/joseph/test/lwjgl3/shadows/shadow.frag";
	
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
		super.bindAttribute(1, "in_textureCoords");
	}
	
}
