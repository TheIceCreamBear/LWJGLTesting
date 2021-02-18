package com.joseph.test.lwjgl3.particle;

import org.joml.Matrix4f;
import org.joml.Vector2f;

import com.joseph.test.lwjgl3.shaders.ShaderProgram;

public class ParticleShader extends ShaderProgram {
	private static final String VERTEX_SHADER = "src/com/joseph/test/lwjgl3/particle/vertex.vert";
	private static final String FRAGMENT_SHADER = "src/com/joseph/test/lwjgl3/particle/fragment.frag";
	
	private int projMatrixLocation;
	private int numRowsLocation;
	
	public ParticleShader() {
		super(VERTEX_SHADER, FRAGMENT_SHADER);
	}
	
	@Override
	protected void bindAttributes() {
		super.bindAttribute(0, "position");
		super.bindAttribute(1, "modelViewMatrix");
		super.bindAttribute(5, "texOffsets");
		super.bindAttribute(6, "blendFactor");
	}
	
	@Override
	protected void getUniformLocations() {
		this.projMatrixLocation = super.getUniformLocation("projMatrix");
		this.numRowsLocation = super.getUniformLocation("numRows");
	}
	
	public void loadProjection(Matrix4f mat) {
		super.loadMatrix4(projMatrixLocation, mat);
	}
	
	public void loadNumRows(float numRows) {
		super.loadFloat(numRowsLocation, numRows);
	}
}
