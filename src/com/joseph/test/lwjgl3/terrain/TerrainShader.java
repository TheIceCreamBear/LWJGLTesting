package com.joseph.test.lwjgl3.terrain;

import java.util.List;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import com.joseph.test.lwjgl3.entity.Camera;
import com.joseph.test.lwjgl3.entity.Light;
import com.joseph.test.lwjgl3.math.MathHelper;
import com.joseph.test.lwjgl3.shaders.ShaderProgram;
import com.joseph.test.lwjgl3.shaders.StaticShader;

/**
 * for now, see {@link StaticShader}
 * @author Joseph
 *
 */
public class TerrainShader extends ShaderProgram {
	private static final int MAX_LIGHTS = 4;
	
	private static final String VERTEX_FILE = "src/com/joseph/test/lwjgl3/terrain/terrain.vert";
	private static final String FRAGMENT_FILE = "src/com/joseph/test/lwjgl3/terrain/terrain.frag";
	
	private int tMatrixLocation;
	private int projMatrixLocation;
	private int viewMatrixLocation;
	private int[] lightPosLocation;
	private int[] lightColorLocation;
	private int[] attenuationLocation;
	private int shineDamperLocation;
	private int reflectivityLocation;
	private int ambientLightLocation;
	private int skyColorLocation;
	private int blendMapLocation;
	private int baseTexLocation;
	private int rTexLocation;
	private int gTexLocation;
	private int bTexLocation;
	private int clipPlaneLocaiton;
	
	
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
		this.shineDamperLocation = super.getUniformLocation("shineDamper");
		this.reflectivityLocation = super.getUniformLocation("reflectivity");
		this.ambientLightLocation = super.getUniformLocation("ambientLight");
		this.skyColorLocation = super.getUniformLocation("skyColor");
		this.blendMapLocation = super.getUniformLocation("blendMap");
		this.baseTexLocation = super.getUniformLocation("baseTex");
		this.rTexLocation = super.getUniformLocation("rTex");
		this.gTexLocation = super.getUniformLocation("gTex");
		this.bTexLocation = super.getUniformLocation("bTex");
		this.clipPlaneLocaiton = super.getUniformLocation("clipPlane");

		this.lightPosLocation = new int[MAX_LIGHTS];
		this.lightColorLocation = new int[MAX_LIGHTS];
		this.attenuationLocation = new int[MAX_LIGHTS];
		for (int i = 0; i < MAX_LIGHTS; i++) {
			this.lightPosLocation[i] = super.getUniformLocation("lightPos[" + i + "]");
			this.lightColorLocation[i] = super.getUniformLocation("lightColor[" + i + "]");
			this.attenuationLocation[i] = super.getUniformLocation("attenuation[" + i + "]");
		}
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

	public void loadLights(List<Light> lights) {
		for (int i = 0; i < MAX_LIGHTS; i++) {
			if (i < lights.size()) {
				super.loadVector(lightPosLocation[i], lights.get(i).getPosition());
				super.loadVector(lightColorLocation[i], lights.get(i).getColor());
				super.loadVector(attenuationLocation[i], lights.get(i).getAttenuation());
			} else {
				super.loadVector(lightPosLocation[i], new Vector3f(0.0f, 0.0f, 0.0f));
				super.loadVector(lightColorLocation[i], new Vector3f(0.0f, 0.0f, 0.0f));
				super.loadVector(attenuationLocation[i], new Vector3f(1.0f, 0.0f, 0.0f));
			}
		}
	}
	
	public void loadAmbientLight(float ambientLight) {
		super.loadFloat(ambientLightLocation, ambientLight);
	}

	public void loadClipPlane(Vector4f plane) {
		super.loadVector(clipPlaneLocaiton, plane);
	}
}
