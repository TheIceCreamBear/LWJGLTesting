package com.joseph.test.lwjgl3.reflectionmap.cubemap;

import org.joml.Matrix4f;

import com.joseph.test.lwjgl3.shaders.ShaderProgram;

public class CubeMapShader extends ShaderProgram {
	private static final String VERTEX_FILE = "/com/joseph/test/lwjgl3/reflectionmap/cubemap/cube.vert";
	private static final String FRAGMENT_FILE = "/com/joseph/test/lwjgl3/reflectionmap/cubemap/cube.frag";

	private int projViewMatLocation;
	
	public CubeMapShader() {
		super(VERTEX_FILE, FRAGMENT_FILE);
	}
	
	@Override
	protected void bindAttributes() {
		super.bindAttribute(0, "position");
	}

	@Override
	protected void getUniformLocations() {
		this.projViewMatLocation = super.getUniformLocation("projViewMat");
	}
	
	public void loadProjViewMat(Matrix4f mat) {
		super.loadMatrix4(projViewMatLocation, mat);
	}
}
