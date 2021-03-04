package com.joseph.test.lwjgl3.renderer.nm;

import java.util.List;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import com.joseph.test.lwjgl3.entity.Light;
import com.joseph.test.lwjgl3.shaders.ShaderProgram;

public class NormalMapShader extends ShaderProgram {
	
	private static final int MAX_LIGHTS = 4;
	
	private static final String VERTEX_FILE = "src/com/joseph/test/lwjgl3/renderer/nm/normalMap.vert";
	private static final String FRAGMENT_FILE = "src/com/joseph/test/lwjgl3/renderer/nm/normalMap.frag";
	
	private int tMatrixLocation;
	private int projMatrixLocation;
	private int viewMatrixLocation;
	private int[] lightPosEyeSpaceLocation;
	private int[] lightColorLocation;
	private int[] attenuationLocation;
	private int shineDamperLocation;
	private int reflectivityLocation;
	private int ambientLightLocation;
	private int skyColorLocation;
	private int numRowsLocation;
	private int offsetLocation;
	private int clipPlaneLocaiton;
	private int textureLocation;
	private int normalMapLocation;
	private int shadowMapSpaceLocation;
	private int entityShadowMapLocation;
	private int terrainShadowMapLocation;
	// TODO is light source hack on this one?
	
	public NormalMapShader() {
		super(VERTEX_FILE, FRAGMENT_FILE);
	}
	
	@Override
	protected void bindAttributes() {
		super.bindAttribute(0, "position");
		super.bindAttribute(1, "textureCoordinates");
		super.bindAttribute(2, "normal");
		super.bindAttribute(3, "tangent");
	}
	
	@Override
	protected void getUniformLocations() {
		this.tMatrixLocation = super.getUniformLocation("tMatrix");
		this.projMatrixLocation = super.getUniformLocation("projMatrix");
		this.viewMatrixLocation = super.getUniformLocation("viewMatrix");
		this.shineDamperLocation = super.getUniformLocation("shineDamper");
		this.reflectivityLocation = super.getUniformLocation("reflectivity");
		this.ambientLightLocation = super.getUniformLocation("ambientLight");
		this.skyColorLocation = super.getUniformLocation("skyColor");
		this.numRowsLocation = super.getUniformLocation("numberOfRows");
		this.offsetLocation = super.getUniformLocation("offset");
		this.clipPlaneLocaiton = super.getUniformLocation("plane");
		this.textureLocation = super.getUniformLocation("modelTexture");
		this.normalMapLocation = super.getUniformLocation("normalMap");
		this.shadowMapSpaceLocation = super.getUniformLocation("shadowMapSpace");
		this.entityShadowMapLocation = super.getUniformLocation("entityShadowMap");
		this.terrainShadowMapLocation = super.getUniformLocation("terrainShadowMap");
		
		this.lightPosEyeSpaceLocation = new int[MAX_LIGHTS];
		this.lightColorLocation = new int[MAX_LIGHTS];
		this.attenuationLocation = new int[MAX_LIGHTS];
		for (int i = 0; i < MAX_LIGHTS; i++) {
			this.lightPosEyeSpaceLocation[i] = super.getUniformLocation("lightPositionEyeSpace[" + i + "]");
			this.lightColorLocation[i] = super.getUniformLocation("lightColor[" + i + "]");
			this.attenuationLocation[i] = super.getUniformLocation("attenuation[" + i + "]");
		}
	}
	
	protected void connectTextureUnits() {
		super.loadInt(textureLocation, 0);
		super.loadInt(normalMapLocation, 1);
		super.loadInt(entityShadowMapLocation, 5);
		super.loadInt(terrainShadowMapLocation, 6);
	}
	
	protected void loadClipPlane(Vector4f plane) {
		super.loadVector(clipPlaneLocaiton, plane);
	}
	
	protected void loadNumberOfRows(int numberOfRows) {
		super.loadFloat(numRowsLocation, numberOfRows);
	}
	
	protected void loadOffset(float x, float y) {
		super.loadVector(offsetLocation, new Vector2f(x, y));
	}
	
	protected void loadSkyColor(float r, float g, float b) {
		super.loadVector(skyColorLocation, new Vector3f(r, g, b));
	}

	protected void loadShineDamper(float shine) {
		super.loadFloat(shineDamperLocation, shine);
	}
	
	protected void loadReflectivity(float reflectivity) {
		super.loadFloat(reflectivityLocation, reflectivity);
	}
	
	protected void loadTransformation(Matrix4f matrix) {
		super.loadMatrix4(tMatrixLocation, matrix);
	}
	
	protected void loadLights(List<Light> lights, Matrix4f viewMatrix) {
		for (int i = 0; i < MAX_LIGHTS; i++) {
			if (i < lights.size()) {
				super.loadVector(lightPosEyeSpaceLocation[i], getEyeSpacePosition(lights.get(i), viewMatrix));
				super.loadVector(lightColorLocation[i], lights.get(i).getColor());
				super.loadVector(attenuationLocation[i], lights.get(i).getAttenuation());
			} else {
				super.loadVector(lightPosEyeSpaceLocation[i], new Vector3f(0, 0, 0));
				super.loadVector(lightColorLocation[i], new Vector3f(0, 0, 0));
				super.loadVector(attenuationLocation[i], new Vector3f(1, 0, 0));
			}
		}
	}
	
	public void loadAmbientLight(float ambientLight) {
		super.loadFloat(ambientLightLocation, ambientLight);
	}
	
	protected void loadViewMatrix(Matrix4f viewMatrix) {
		super.loadMatrix4(viewMatrixLocation, viewMatrix);
	}
	
	protected void loadProjectionMatrix(Matrix4f projection) {
		super.loadMatrix4(projMatrixLocation, projection);
	}
	
	private Vector3f getEyeSpacePosition(Light light, Matrix4f viewMatrix) {
		Vector3f position = light.getPosition();
		Vector4f eyeSpacePos = new Vector4f(position.x, position.y, position.z, 1f);
		return eyeSpacePos.mulProject(viewMatrix, new Vector3f());
	}
	
	public void loadShadowMapSpace(Matrix4f shadowMapConversion) {
		super.loadMatrix4(shadowMapSpaceLocation, shadowMapConversion);
	}
}
