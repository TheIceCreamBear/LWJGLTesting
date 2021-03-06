package com.joseph.test.lwjgl3.renderer.postprocess;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;

import com.joseph.test.lwjgl3.renderer.postprocess.contrast.ContrastShader;

public class ContrastPostProcess {
	private ImageRenderer renderer;
	private ContrastShader shader;
	
	public ContrastPostProcess() {
		this.shader = new ContrastShader();
		this.renderer = new ImageRenderer();
	}
	
	public void render(int texture) {
		shader.start();
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture);
		renderer.renderQuad();
		shader.stop();
	}
	
	public void cleanUp() {
		renderer.cleanUp();
		shader.cleanUp();
	}
}
