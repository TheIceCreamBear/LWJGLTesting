package com.joseph.test.lwjgl3.renderer.postprocess.contrast;

import com.joseph.test.lwjgl3.shaders.ShaderProgram;

public class ContrastShader extends ShaderProgram {
	
	private static final String VERTEX_FILE = "/com/joseph/test/lwjgl3/renderer/postprocess/contrast/contrast.vert";
	private static final String FRAGMENT_FILE = "/com/joseph/test/lwjgl3/renderer/postprocess/contrast/contrast.frag";
	
	public ContrastShader() {
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
