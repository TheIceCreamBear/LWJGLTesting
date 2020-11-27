package com.joseph.test.lwjgl3.shaders;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;

import com.joseph.test.lwjgl3.entity.Camera;
import com.joseph.test.lwjgl3.entity.Light;
import com.joseph.test.lwjgl3.math.MathHelper;

/**
 * does this even need docs? meh, okay so this is just a static shader 
 * that statically shades the basic rectangle that is at the tutorial thingy
 * and like yea basic
 * @author Joseph
 *
 */
public class StaticShader extends ShaderProgram {
	private static final String VERTEX_FILE = "src/com/joseph/test/lwjgl3/shaders/vertexShader.vert";
	private static final String FRAGMENT_FILE = "src/com/joseph/test/lwjgl3/shaders/fragmentShader.frag";
	
	private int tMatrixLocation;
	private int projMatrixLocation;
	private int viewMatrixLocation;
	private int lightPosLocation;
	private int lightColorLocation;
	private int shineDamperLocation;
	private int reflectivityLocation;
	private int ambientLightLocation;
	private int fakeLightLocation;
	private int skyColorLocation;
	private int numRowsLocation;
	private int offsetLocation;
	
	public StaticShader() {
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
		this.fakeLightLocation = super.getUniformLocation("useFakeLight");
		this.skyColorLocation = super.getUniformLocation("skyColor");
		this.numRowsLocation = super.getUniformLocation("numRows");
		this.offsetLocation = super.getUniformLocation("offset");
	}
	
	public void loadNumRows(int numRows) {
		super.loadFloat(numRowsLocation, numRows);
	}
	
	public void loadOffset(float x, float y) {
		super.loadVector(offsetLocation, new Vector2f(x, y));
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
	
	public void loadFakeLightValue(boolean useFake) {
		super.loadBoolean(fakeLightLocation, useFake);
	}
}
