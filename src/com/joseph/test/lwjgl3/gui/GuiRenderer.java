package com.joseph.test.lwjgl3.gui;

import java.util.List;

import org.joml.Matrix4f;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import com.joseph.test.lwjgl3.math.MathHelper;
import com.joseph.test.lwjgl3.models.ModelLoader;
import com.joseph.test.lwjgl3.models.RawModel;

public class GuiRenderer {
	private final RawModel quad;
	private GuiShader shader;
	
	/**
	 * like zoinks creates the renderer
	 * @param loader - yup, dont like that we are passing this as an object when we could easily just make it
	 * 			static
	 */
	public GuiRenderer(ModelLoader loader) {
		float[] quadPositions = { -1, 1, -1, -1, 1, 1, 1, -1 };
		this.quad = loader.loadToVAO(quadPositions, 2);
		this.shader = new GuiShader();
	}
	
	public void render(List<GuiTexture> guis) {
		// start your engines
		shader.start();
		// tell GL which VAO and VBO we are going to use because if we dont it doesnt know what to brain
		GL30.glBindVertexArray(quad.getVaoID());
		GL20.glEnableVertexAttribArray(0);
		
		// enables GUI textures to contain transparent pixels by doing some alpha male type blending stuff
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		
		// disable the depth testing so the gui elements with transparency dont try to do some alpha male stuff
		// and render on top of another gui
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		
		// render
		for (GuiTexture gui : guis) {
			GL13.glActiveTexture(GL13.GL_TEXTURE0);
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, gui.getGlTextureID());
			Matrix4f matrix = MathHelper.createTransformationMatrix(gui.getPos(), gui.getScale());
			shader.loadTransformation(matrix);
			GL11.glDrawArrays(GL11.GL_TRIANGLE_STRIP, 0, quad.getVertexCount());
		}
		
		// re enable the disabled testing of depth
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		
		// disable that gui tex alpha male blending so that it doesnt happen else where
		GL11.glDisable(GL11.GL_BLEND);

		// tell GL that we are done with that stuff and it can pretent nothing ever happened
		GL20.glDisableVertexAttribArray(0);
		GL30.glBindVertexArray(0);
		// skrrrrrrrrrrrrrt go the tires of the car stopping the shader thing
		shader.stop();
	}
	
	public void cleanUp() {
		// clean up, clean up, everybody, everywhere, clean up, clean up, ive done this joke already havent i
		shader.cleanUp();
	}
}
