package com.joseph.test.lwjgl3.particle;

import com.joseph.test.lwjgl3.models.RawModel;

public class ParticleRenderer {
	private static final float[] VERTICIES = {-0.5f, 0.5f, -0.5f, -0.5f, 0.5f, 0.5f, 0.5f, -0.5f};
	
	private RawModel quad;
	private ParticleShader shader;
	
	public void cleanUp() {
		this.shader.cleanUp();
	}
}
