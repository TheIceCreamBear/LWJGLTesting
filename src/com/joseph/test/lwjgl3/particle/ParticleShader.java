package com.joseph.test.lwjgl3.particle;

import org.joml.Matrix4f;
import org.joml.Vector2f;

import com.joseph.test.lwjgl3.shaders.ShaderProgram;

public class ParticleShader extends ShaderProgram {
	private static final String VERTEX_SHADER = "src/com/joseph/test/lwjgl3/particle/vertex.vert";
	private static final String FRAGMENT_SHADER = "src/com/joseph/test/lwjgl3/particle/fragment.frag";
	
	private int projMatrixLocation;
	private int modelViewMatrixLocation;
	private int texOffsetCurLocation;
	private int texOffsetNextLocation;
	private int texCoordInfoLocation;
	
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
		this.texOffsetCurLocation = super.getUniformLocation("texOffsetCur");
		this.texOffsetNextLocation = super.getUniformLocation("texOffsetNext");
		this.texCoordInfoLocation = super.getUniformLocation("texCoordInfo");
	}
	
	public void loadProjection(Matrix4f mat) {
		super.loadMatrix4(projMatrixLocation, mat);
	}
	
	public void loadModelViewMatrix(Matrix4f mat) {
		super.loadMatrix4(modelViewMatrixLocation, mat);
	}
	
	public void loadTextureInfo(Vector2f curOffset, Vector2f nextOffset, int numRows, float blend) {
		super.loadVector(texOffsetCurLocation, curOffset);
		super.loadVector(texOffsetNextLocation, nextOffset);
		super.loadVector(texCoordInfoLocation, new Vector2f(numRows, blend));
	}
}
