package com.joseph.test.lwjgl3.renderer.postprocess;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import com.joseph.test.lwjgl3.GLFWHandler;
import com.joseph.test.lwjgl3.models.ModelLoader;
import com.joseph.test.lwjgl3.models.RawModel;
import com.joseph.test.lwjgl3.renderer.postprocess.bloom.BrightFilter;
import com.joseph.test.lwjgl3.renderer.postprocess.bloom.CombineFilter;
import com.joseph.test.lwjgl3.renderer.postprocess.contrast.ContrastPostProcess;
import com.joseph.test.lwjgl3.renderer.postprocess.gaussianblur.HorizontalBlurPostProcess;
import com.joseph.test.lwjgl3.renderer.postprocess.gaussianblur.VerticalBlurPostProcess;

public class PostProcessing {
	private static final float[] POSITIONS = { -1, 1, -1, -1, 1, 1, 1, -1 };
	private static RawModel quad;
	private static ContrastPostProcess contrast;
	private static HorizontalBlurPostProcess hBlur;
	private static VerticalBlurPostProcess vBlur;
	private static BrightFilter brightf;
	private static CombineFilter combinef;
	
	public static void init() {
		quad = ModelLoader.instance.loadToVAO(POSITIONS, 2);
		int width = GLFWHandler.SCREEN_WIDTH;
		int height = GLFWHandler.SCREEN_HEIGHT;
		
		contrast = new ContrastPostProcess();
		
		// represents how much to down sample the output by, makes it more blury but will introduce flikering
		// solution to flickering would be to down sample by less and have more blur passes
		int downSample = 5;
		hBlur = new HorizontalBlurPostProcess(width / downSample, height / downSample);
		vBlur = new VerticalBlurPostProcess(width / downSample, height / downSample);
		brightf = new BrightFilter(width / 2, height / 2);
		combinef = new CombineFilter();
	}
	
	public static void doPostProcessing(int colorTex, int brightTex) {
		start();
//		brightf.render(colorTex);
		hBlur.render(brightTex);
		vBlur.render(hBlur.getOutputTexture());
		combinef.render(colorTex, vBlur.getOutputTexture());
		end();
	}
	
	public static void cleanUp() {
		contrast.cleanUp();
		hBlur.cleanUp();
		vBlur.cleanUp();
		brightf.cleanUp();
		combinef.cleanUp();
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
