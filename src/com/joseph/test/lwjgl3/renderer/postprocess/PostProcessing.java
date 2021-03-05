package com.joseph.test.lwjgl3.renderer.postprocess;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import com.joseph.test.lwjgl3.models.ModelLoader;
import com.joseph.test.lwjgl3.models.RawModel;

public class PostProcessing {
	
	private static final float[] POSITIONS = { -1, 1, -1, -1, 1, 1, 1, -1 };
	private static RawModel quad;
	private static ContrastPostProcess contrast;
	
	public static void init() {
		quad = ModelLoader.instance.loadToVAO(POSITIONS, 2);
		contrast = new ContrastPostProcess();
	}
	
	public static void doPostProcessing(int colourTexture) {
		start();
		contrast.render(colourTexture);
		end();
	}
	
	public static void cleanUp() {
		contrast.cleanUp();
	}
	
	private static void start() {
		GL30.glBindVertexArray(quad.getVaoID());
		GL20.glEnableVertexAttribArray(0);
		GL11.glDisable(GL11.GL_DEPTH_TEST);
	}
	
	private static void end() {
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL20.glDisableVertexAttribArray(0);
		GL30.glBindVertexArray(0);
	}
	
}
