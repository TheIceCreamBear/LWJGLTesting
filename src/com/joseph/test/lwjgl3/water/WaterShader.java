package com.joseph.test.lwjgl3.water;

import org.joml.Matrix4f;

import com.joseph.test.lwjgl3.entity.Camera;
import com.joseph.test.lwjgl3.entity.Light;
import com.joseph.test.lwjgl3.math.MathHelper;
import com.joseph.test.lwjgl3.shaders.ShaderProgram;

public class WaterShader extends ShaderProgram {

	private final static String VERTEX_FILE = "src/com/joseph/test/lwjgl3/water/waterVertex.vert";
	private final static String FRAGMENT_FILE = "src/com/joseph/test/lwjgl3/water/waterFragment.frag";

	private int tMatrixLocation;
	private int projMatrixLocation;
	private int viewMatrixLocation;
	private int reflectionLocation;
	private int refractionLocation;
	private int dudvLocation;
	private int moveFactorLocation;
	private int camPosLocation;
	private int normalMapLocation;
	private int lightPosLocation;
	private int lightColorLocation;

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
		this.reflectionLocation = super.getUniformLocation("reflectionTexture");
		this.refractionLocation = super.getUniformLocation("refractionTexture");
		this.dudvLocation = super.getUniformLocation("dudvMap");
		this.moveFactorLocation = super.getUniformLocation("moveFactor");
		this.camPosLocation = super.getUniformLocation("camPos");
		this.normalMapLocation = super.getUniformLocation("normalMap");
		this.lightColorLocation = super.getUniformLocation("lightColor");
		this.lightPosLocation = super.getUniformLocation("lightPos");
	}
	
	public void connectTextures() {
		super.loadInt(reflectionLocation, 0);
		super.loadInt(refractionLocation, 1);
		super.loadInt(dudvLocation, 2);
		super.loadInt(normalMapLocation, 3);
	}
	
	public void loadLight(Light sky) {
		super.loadVector(lightColorLocation, sky.getColor());
		super.loadVector(lightPosLocation, sky.getPosition());
	}
	
	public void loadMoveFactor(float factor) {
		super.loadFloat(moveFactorLocation, factor);
	}

	public void loadProjectionMatrix(Matrix4f projection) {
		super.loadMatrix4(projMatrixLocation, projection);
	}
	
	public void loadViewMatrix(Camera camera){
		super.loadMatrix4(viewMatrixLocation, MathHelper.createViewMatrix(camera));
		super.loadVector(camPosLocation, camera.getPosition());
	}

	public void loadModelMatrix(Matrix4f modelMatrix){
		super.loadMatrix4(tMatrixLocation, modelMatrix);
	}

}
