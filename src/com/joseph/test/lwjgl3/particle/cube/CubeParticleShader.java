package com.joseph.test.lwjgl3.particle.cube;

import org.joml.Matrix4f;

import com.joseph.test.lwjgl3.shaders.ShaderProgram;

public class CubeParticleShader extends ShaderProgram {
	private static final String VERTEX_FILE = "/com/joseph/test/lwjgl3/particle/cube/cube.vert";
	private static final String GEOMETRY_FILE = "/com/joseph/test/lwjgl3/particle/cube/cube.geom";
	private static final String FRAGMENT_FILE = "/com/joseph/test/lwjgl3/particle/cube/cube.frag";
	
	private int projViewMatrix;
	
	public CubeParticleShader() {
		super(VERTEX_FILE, GEOMETRY_FILE, FRAGMENT_FILE);
	}
	
	@Override
	protected void bindAttributes() {
		super.bindAttribute(0, "position");
	}
	
	@Override
	protected void getUniformLocations() {
		this.projViewMatrix = super.getUniformLocation("projectionViewMatrix");
	}
	
	public void loadProjectionViewMatrix(Matrix4f matrix) {
		super.loadMatrix4(projViewMatrix, matrix);
	}
}
