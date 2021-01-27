package com.joseph.test.lwjgl3.water;

import org.joml.Matrix4f;

import com.joseph.test.lwjgl3.entity.Camera;
import com.joseph.test.lwjgl3.math.MathHelper;
import com.joseph.test.lwjgl3.shaders.ShaderProgram;

public class WaterShader extends ShaderProgram {

	private final static String VERTEX_FILE = "src/com/joseph/test/lwjgl3/water/waterVertex.vert";
	private final static String FRAGMENT_FILE = "src/com/joseph/test/lwjgl3/water/waterFragment.frag";

	private int tMatrixLocation;
	private int projMatrixLocation;
	private int viewMatrixLocation;

	public WaterShader() {
		super(VERTEX_FILE, FRAGMENT_FILE);
	}

	@Override
	protected void bindAttributes() {
		super.bindAttribute(0, "position");
	}

	@Override
	protected void getUniformLocations() {
		this.tMatrixLocation = super.getUniformLocation("tMatrix");
		this.projMatrixLocation = super.getUniformLocation("projMatrix");
		this.viewMatrixLocation = super.getUniformLocation("viewMatrix");
	}

	public void loadProjectionMatrix(Matrix4f projection) {
		super.loadMatrix4(projMatrixLocation, projection);
	}
	
	public void loadViewMatrix(Camera camera){
		super.loadMatrix4(viewMatrixLocation, MathHelper.createViewMatrix(camera));
	}

	public void loadModelMatrix(Matrix4f modelMatrix){
		super.loadMatrix4(tMatrixLocation, modelMatrix);
	}

}
