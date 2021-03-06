package com.joseph.test.lwjgl3.renderer.postprocess.gaussianblur;

import com.joseph.test.lwjgl3.shaders.ShaderProgram;

public class VerticalBlurShader extends ShaderProgram {
	private static final String VERTEX_FILE = "/com/joseph/test/lwjgl3/renderer/postprocess/gaussianblur/vblur.vert";
	private static final String FRAGMENT_FILE = "/com/joseph/test/lwjgl3/renderer/postprocess/gaussianblur/blur.frag";
	
	private int targetHeightLocation;
	
	protected VerticalBlurShader() {
		super(VERTEX_FILE, FRAGMENT_FILE);
	}
	
	protected void loadTargetHeight(float height) {
		super.loadFloat(targetHeightLocation, height);
	}
	
	@Override
	protected void getUniformLocations() {
		targetHeightLocation = super.getUniformLocation("targetHeight");
	}
	
	@Override
	protected void bindAttributes() {
		super.bindAttribute(0, "position");
	}
}
