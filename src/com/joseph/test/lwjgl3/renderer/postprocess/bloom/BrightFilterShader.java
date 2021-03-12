package com.joseph.test.lwjgl3.renderer.postprocess.bloom;

import com.joseph.test.lwjgl3.shaders.ShaderProgram;

public class BrightFilterShader extends ShaderProgram {
	
	private static final String VERTEX_FILE = "/com/joseph/test/lwjgl3/renderer/postprocess/bloom/simple.vert";
	private static final String FRAGMENT_FILE = "/com/joseph/test/lwjgl3/renderer/postprocess/bloom/brightFilter.frag";
	
	public BrightFilterShader() {
		super(VERTEX_FILE, FRAGMENT_FILE);
	}
	
	@Override
	protected void bindAttributes() {
		super.bindAttribute(0, "position");
	}
	
	@Override
	protected void getUniformLocations() {
	}
}
