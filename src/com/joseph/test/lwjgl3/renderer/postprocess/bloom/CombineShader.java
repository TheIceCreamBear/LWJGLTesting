package com.joseph.test.lwjgl3.renderer.postprocess.bloom;

import com.joseph.test.lwjgl3.shaders.ShaderProgram;

public class CombineShader extends ShaderProgram {
	
	private static final String VERTEX_FILE = "/com/joseph/test/lwjgl3/renderer/postprocess/bloom/simple.vert";
	private static final String FRAGMENT_FILE = "/com/joseph/test/lwjgl3/renderer/postprocess/bloom/combine.frag";
	
	private int colorTextureLocation;
	private int highlightTextureLocation;
	
	protected CombineShader() {
		super(VERTEX_FILE, FRAGMENT_FILE);
	}
	
	@Override
	protected void bindAttributes() {
		super.bindAttribute(0, "position");
	}
	
	@Override
	protected void getUniformLocations() {
		colorTextureLocation = super.getUniformLocation("colorTexture");
		highlightTextureLocation = super.getUniformLocation("highlightTexture");
	}
	
	protected void connectTextureUnits() {
		super.loadInt(colorTextureLocation, 0);
		super.loadInt(highlightTextureLocation, 1);
	}	
}
