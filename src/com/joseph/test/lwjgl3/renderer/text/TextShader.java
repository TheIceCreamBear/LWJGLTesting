package com.joseph.test.lwjgl3.renderer.text;

import org.joml.Vector2f;
import org.joml.Vector3f;

import com.joseph.test.lwjgl3.shaders.ShaderProgram;

/**
 * Just a boring old shader, dont mind it
 * @author Joseph
 *
 */
public class TextShader extends ShaderProgram {
	private static final String VERTEX_FILE = "src/com/joseph/test/lwjgl3/renderer/text/text.vert";
	private static final String FRAGMENT_FILE = "src/com/joseph/test/lwjgl3/renderer/text/text.frag";
	
	private int translationLocation;
	private int colorLocation;
	
	public TextShader() {
		super(VERTEX_FILE, FRAGMENT_FILE);
	}

	@Override
	protected void bindAttributes() {
		super.bindAttribute(0, "position");
		super.bindAttribute(1, "textureCoords");
	}
	
	@Override
	protected void getUniformLocations() {
		this.translationLocation = super.getUniformLocation("translation");
		this.colorLocation = super.getUniformLocation("color");
	}
	
	protected void loadColor(Vector3f color) {
		super.loadVector(colorLocation, color);
	}
	
	protected void loadTranslation(Vector2f translation) {
		super.loadVector(translationLocation, translation);
	}
	
}
