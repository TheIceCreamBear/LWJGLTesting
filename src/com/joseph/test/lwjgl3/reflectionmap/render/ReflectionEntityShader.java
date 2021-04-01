package com.joseph.test.lwjgl3.reflectionmap.render;

import org.joml.Matrix4f;

import com.joseph.test.lwjgl3.entity.Camera;
import com.joseph.test.lwjgl3.math.MathHelper;
import com.joseph.test.lwjgl3.shaders.ShaderProgram;

public class ReflectionEntityShader extends ShaderProgram {
	private static final String VERTEX_FILE = "/com/joseph/test/lwjgl3/reflectionmap/render/entity.vert";
	private static final String FRAGMENT_FILE = "/com/joseph/test/lwjgl3/reflectionmap/render/entity.frag";
	
	private int transMatLocation;
	private int projMatLocation;
	private int viewMatLocation;
	private int textureLocation;
	private int envMapLocation;
	private int camPosLocaiton;
	
	public ReflectionEntityShader() {
		super(VERTEX_FILE, FRAGMENT_FILE);
	}
	
	@Override
	protected void bindAttributes() {
		super.bindAttribute(0, "position");
		super.bindAttribute(1, "textureCoordinates");
		super.bindAttribute(2, "normal");
	}

	@Override
	protected void getUniformLocations() {
		this.transMatLocation = super.getUniformLocation("transMatrix");
		this.projMatLocation = super.getUniformLocation("projMatrix");
		this.viewMatLocation = super.getUniformLocation("viewMatrix");
		this.textureLocation = super.getUniformLocation("modelTexture");
		this.envMapLocation = super.getUniformLocation("envMap");
		this.camPosLocaiton = super.getUniformLocation("cameraPosition");
	}
	
	public void connectTextureUnits() {
		super.loadInt(textureLocation, 0);
		super.loadInt(envMapLocation, 1);
	}
	
	public void loadTransMat(Matrix4f mat) {
		super.loadMatrix4(transMatLocation, mat);
	}
	
	public void loadViewMat(Camera cam) {
		super.loadMatrix4(viewMatLocation, MathHelper.createViewMatrix(cam));
		super.loadVector(camPosLocaiton, cam.getPosition());
	}
	
	public void loadProjMat(Matrix4f proj) {
		super.loadMatrix4(projMatLocation, proj);
	}
}
