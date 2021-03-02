package com.joseph.test.lwjgl3.skybox;

import org.joml.Matrix4f;
import org.joml.Vector3f;

import com.joseph.test.lwjgl3.entity.Camera;
import com.joseph.test.lwjgl3.math.MathHelper;
import com.joseph.test.lwjgl3.shaders.ShaderProgram;

public class SkyboxShader extends ShaderProgram {
	private static final String VERTEX_FILE = "src/com/joseph/test/lwjgl3/skybox/skybox.vert";
	private static final String FRAGMENT_FILE = "src/com/joseph/test/lwjgl3/skybox/skybox.frag";
	
	private static final float ROTATE_SPEED = 1.0f;

	private int projMatrixLocation;
	private int viewMatrixLocation;
	private int fogColorLocation;
	private int cubeMapLocation;
	private int cubeMap2Location;
	private int blendFactorLocaiton;
	
	private float rotation = 0;
	
	public SkyboxShader() {
		super(VERTEX_FILE, FRAGMENT_FILE);
	}

	@Override
	protected void bindAttributes() {
		super.bindAttribute(0, "position");
	}

	@Override
	protected void getUniformLocations() {
		this.projMatrixLocation = super.getUniformLocation("projMatrix");
		this.viewMatrixLocation = super.getUniformLocation("viewMatrix");
		this.fogColorLocation = super.getUniformLocation("fogColor");
		this.cubeMapLocation = super.getUniformLocation("cubeMap");
		this.cubeMap2Location = super.getUniformLocation("cubeMap2");
		this.blendFactorLocaiton = super.getUniformLocation("blendFactor");
	}
	
	public void loadBlendFactor(float blend) {
		super.loadFloat(blendFactorLocaiton, blend);
	}
	
	public void loadFogColor(float r, float g, float b) {
		super.loadVector(fogColorLocation, new Vector3f(r, g, b));
	}

	public void loadProjection(Matrix4f mat) {
		super.loadMatrix4(projMatrixLocation, mat);
	}
	
	public void loadViewMatrix(Camera cam, float delta) {
		Matrix4f m = MathHelper.createViewMatrix(cam);
		m.m30(0);
		m.m31(0);
		m.m32(0);
		rotation += ROTATE_SPEED * delta;
		m.rotateY((float) Math.toRadians(rotation));
		super.loadMatrix4(viewMatrixLocation, m);
	}
	
	public void connectTexUnits() {
		super.loadInt(cubeMapLocation, 0);
		super.loadInt(cubeMap2Location, 1);
	}
}
