package com.joseph.test.lwjgl3.shaders;

import org.joml.Matrix4f;
import org.joml.Vector3f;

import com.joseph.test.lwjgl3.entity.Camera;
import com.joseph.test.lwjgl3.entity.Light;
import com.joseph.test.lwjgl3.math.MathHelper;

/**
 * for now, see {@link StaticShader}
 * @author Joseph
 *
 */
public class TerrainShader extends ShaderProgram {
	private static final String VERTEX_FILE = "src/com/joseph/test/lwjgl3/shaders/terrainVertexShader.vert";
	private static final String FRAGMENT_FILE = "src/com/joseph/test/lwjgl3/shaders/terrainFragmentShader.frag";
	
	private int tMatrixLocation;
	private int projMatrixLocation;
	private int viewMatrixLocation;
	private int lightPosLocation;
	private int lightColorLocation;
	private int shineDamperLocation;
	private int reflectivityLocation;
	private int ambientLightLocation;
	private int skyColorLocation;
	private int blendMapLocation;
	private int baseTexLocation;
	private int rTexLocation;
	private int gTexLocation;
	private int bTexLocation;
	
	
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
		this.skyColorLocation = super.getUniformLocation("skyColor");
		this.blendMapLocation = super.getUniformLocation("blendMap");
		this.baseTexLocation = super.getUniformLocation("baseTex");
		this.rTexLocation = super.getUniformLocation("rTex");
		this.gTexLocation = super.getUniformLocation("gTex");
		this.bTexLocation = super.getUniformLocation("bTex");
	}
	
	public void setupTextures() {
		super.loadInt(blendMapLocation, 0);
		super.loadInt(baseTexLocation, 1);
		super.loadInt(rTexLocation, 2);
		super.loadInt(gTexLocation, 3);
		super.loadInt(bTexLocation, 4);
	}
	
	public void loadSkyColor(float r, float g, float b) {
		super.loadVector(skyColorLocation, new Vector3f(r, g, b));
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
