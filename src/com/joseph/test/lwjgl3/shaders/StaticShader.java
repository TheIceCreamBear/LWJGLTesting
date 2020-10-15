package com.joseph.test.lwjgl3.shaders;

import org.joml.Matrix4f;

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
}
