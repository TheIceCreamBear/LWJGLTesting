package com.joseph.test.lwjgl3.renderer.postprocess.gaussianblur;

import com.joseph.test.lwjgl3.shaders.ShaderProgram;

public class HorizontalBlurShader extends ShaderProgram {
	private static final String VERTEX_FILE = "/com/joseph/test/lwjgl3/renderer/postprocess/gaussianblur/hblur.vert";
	private static final String FRAGMENT_FILE = "/com/joseph/test/lwjgl3/renderer/postprocess/gaussianblur/blur.frag";
	
	private int targetWidthLocation;
	
	protected HorizontalBlurShader() {
		super(VERTEX_FILE, FRAGMENT_FILE);
	}
	
	protected void loadTargetWidth(float width) {
		super.loadFloat(targetWidthLocation, width);
	}
	
	@Override
	protected void getUniformLocations() {
		targetWidthLocation = super.getUniformLocation("targetWidth");
	}
	
	@Override
	protected void bindAttributes() {
		super.bindAttribute(0, "position");
	}
	
}
