package com.joseph.test.lwjgl3.renderer.postprocess.gaussianblur;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;

import com.joseph.test.lwjgl3.renderer.postprocess.ImageRenderer;

public class VerticalBlurPostProcess {
	private ImageRenderer renderer;
	private VerticalBlurShader shader;
	
	public VerticalBlurPostProcess(int targetFboWidth, int targetFboHeight) {
		shader = new VerticalBlurShader();
		renderer = new ImageRenderer(targetFboWidth, targetFboHeight);
		shader.start();
		shader.loadTargetHeight(targetFboHeight);
		shader.stop();
	}
	
	public void render(int texture) {
		shader.start();
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture);
		renderer.renderQuad();
		shader.stop();
	}
	
	public int getOutputTexture() {
		return renderer.getOutputTexture();
	}
	
	public void cleanUp() {
		renderer.cleanUp();
		shader.cleanUp();
	}
}
