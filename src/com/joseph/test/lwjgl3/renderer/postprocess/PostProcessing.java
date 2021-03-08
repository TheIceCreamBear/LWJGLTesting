package com.joseph.test.lwjgl3.renderer.postprocess;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import com.joseph.test.lwjgl3.GLFWHandler;
import com.joseph.test.lwjgl3.models.ModelLoader;
import com.joseph.test.lwjgl3.models.RawModel;
import com.joseph.test.lwjgl3.renderer.postprocess.contrast.ContrastPostProcess;
import com.joseph.test.lwjgl3.renderer.postprocess.gaussianblur.HorizontalBlurPostProcess;
import com.joseph.test.lwjgl3.renderer.postprocess.gaussianblur.VerticalBlurPostProcess;

public class PostProcessing {
	private static final float[] POSITIONS = { -1, 1, -1, -1, 1, 1, 1, -1 };
	private static RawModel quad;
	private static ContrastPostProcess contrast;
	private static HorizontalBlurPostProcess hBlur;
	private static VerticalBlurPostProcess vBlur;
	private static HorizontalBlurPostProcess hBlur2;
	private static VerticalBlurPostProcess vBlur2;
	
	public static void init() {
		quad = ModelLoader.instance.loadToVAO(POSITIONS, 2);
		contrast = new ContrastPostProcess();
		// represents how much to down sample the output by, makes it more blury but will introduce flikering
		// solution to flickering would be to down sample by less and have more blur passes
		int downSample = 8;
		hBlur = new HorizontalBlurPostProcess(GLFWHandler.SCREEN_WIDTH / downSample, GLFWHandler.SCREEN_HEIGHT / downSample);
		vBlur = new VerticalBlurPostProcess(GLFWHandler.SCREEN_WIDTH / downSample, GLFWHandler.SCREEN_HEIGHT / downSample);
		hBlur2 = new HorizontalBlurPostProcess(GLFWHandler.SCREEN_WIDTH / 2, GLFWHandler.SCREEN_HEIGHT / 2);
		vBlur2 = new VerticalBlurPostProcess(GLFWHandler.SCREEN_WIDTH / 2, GLFWHandler.SCREEN_HEIGHT / 2);
	}
	
	public static void doPostProcessing(int tex) {
		start();
//		hBlur2.render(tex);
//		vBlur2.render(hBlur2.getOutputTexture());
//		hBlur.render(vBlur2.getOutputTexture());
//		vBlur.render(hBlur.getOutputTexture());
		contrast.render(tex);
		end();
	}
	
	public static void cleanUp() {
		contrast.cleanUp();
		hBlur.cleanUp();
		vBlur.cleanUp();
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
