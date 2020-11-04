package com.joseph.test.lwjgl3.shaders;

import org.joml.Matrix4f;

import com.joseph.test.lwjgl3.entity.Camera;
import com.joseph.test.lwjgl3.entity.Light;
import com.joseph.test.lwjgl3.math.MathHelper;

/**
 * for now, see {@link StaticShader}
 * @author Joseph
 *
 */
public class TerrainShader extends ShaderProgram {
	private static final String VERTEX_FILE = "/com/joseph/test/lwjgl3/shaders/terrainVertexShader.vert";
	private static final String FRAGMENT_FILE = "/com/joseph/test/lwjgl3/shaders/terrainFragmentShader.frag";
	
	private int tMatrixLocation;
	private int projMatrixLocation;
	private int viewMatrixLocation;
	private int lightPosLocation;
	private int lightColorLocation;
	private int shineDamperLocation;
	private int reflectivityLocation;
	private int ambientLightLocation;
	
	public TerrainShader() {
		super(VERTEX_FILE, FRAGMENT_FILE);
	}

	@Override
	protected void bindAttributes() {
		super.bindAttribute(0, "position");
		super.bindAttribute(1, "textureCoords");
		super.bindAttribute(2, "normal");
	}

	@Override
	protected void getUniformLocations() {
		this.tMatrixLocation = super.getUniformLocation("tMatrix");
		this.projMatrixLocation = super.getUniformLocation("projMatrix");
		this.viewMatrixLocation = super.getUniformLocation("viewMatrix");
		this.lightPosLocation = super.getUniformLocation("lightPos");
		this.lightColorLocation = super.getUniformLocation("lightColor");
		this.shineDamperLocation = super.getUniformLocation("shineDamper");
		this.reflectivityLocation = super.getUniformLocation("reflectivity");
		this.ambientLightLocation = super.getUniformLocation("ambientLight");
	}
	
	public void loadShineDamper(float shine) {
		super.loadFloat(shineDamperLocation, shine);
	}
	
	public void loadReflectivity(float reflectivity) {
		super.loadFloat(reflectivityLocation, reflectivity);
	}
	
	public void loadTransformation(Matrix4f mat) {
		super.loadMatrix4(tMatrixLocation, mat);
	}
	
	public void loadProjection(Matrix4f mat) {
		super.loadMatrix4(projMatrixLocation, mat);
	}
	
	public void loadViewMatrix(Camera cam) {
		super.loadMatrix4(viewMatrixLocation, MathHelper.createViewMatrix(cam));
	}
	
	public void loadLight(Light l) {
		super.loadVector(lightPosLocation, l.getPosition());
		super.loadVector(lightColorLocation, l.getColor());
	}
	
	public void loadAmbientLight(float ambientLight) {
		super.loadFloat(ambientLightLocation, ambientLight);
	}
}
