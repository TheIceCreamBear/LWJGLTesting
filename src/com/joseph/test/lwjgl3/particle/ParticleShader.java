package com.joseph.test.lwjgl3.particle;

import org.joml.Matrix4f;

import com.joseph.test.lwjgl3.entity.Camera;
import com.joseph.test.lwjgl3.math.MathHelper;
import com.joseph.test.lwjgl3.shaders.ShaderProgram;

public class ParticleShader extends ShaderProgram {
	private static final String VERTEX_SHADER = "src/com/joseph/test/lwjgl3/particle/vertex.vert";
	private static final String FRAGMENT_SHADER = "src/com/joseph/test/lwjgl3/particle/fragment.frag";
	
	private int projMatrixLocation;
	private int modelViewMatrixLocation;
	
	public ParticleShader() {
		super(VERTEX_SHADER, FRAGMENT_SHADER);
	}
	
	@Override
	protected void bindAttributes() {
		super.bindAttribute(0, "position");
	}
	
	@Override
	protected void getUniformLocations() {
		this.projMatrixLocation = super.getUniformLocation("projMatrix");
		this.modelViewMatrixLocation = super.getUniformLocation("modelViewMatrix");
	}
	
	public void loadProjection(Matrix4f mat) {
		super.loadMatrix4(projMatrixLocation, mat);
	}
	
	public void loadModelViewMatrix(Matrix4f mat) {
		super.loadMatrix4(modelViewMatrixLocation, mat);
	}
}
