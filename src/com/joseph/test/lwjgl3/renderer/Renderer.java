package com.joseph.test.lwjgl3.renderer;

import org.joml.Matrix4f;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import com.joseph.test.lwjgl3.entity.Entity;
import com.joseph.test.lwjgl3.math.MathHelper;
import com.joseph.test.lwjgl3.models.RawModel;
import com.joseph.test.lwjgl3.models.TexturedModel;
import com.joseph.test.lwjgl3.shaders.StaticShader;

public class Renderer {
	/**
	 * This method contains things that need to happen at the beginning of every frame i think,
	 * essentially preparing for the next frame to be drawn
	 */
	public void prepare() {
		// this will clear the current frame buffer of its contents and set the pixels to the 
		// pixel color specified in the clearColor funciton call above
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
	}
	
	/**
	 * This method renders a raw model (which is just raw verticies data) to the screen
	 * @param model - the model to render
	 */
	public void render(Entity entity, StaticShader shader) {
		TexturedModel texModel = entity.getModel();
		RawModel model = texModel.getModel();
		// okay so this takes the vao that is unique to this model and it binds it as the active vao
		// which is needed so that GL knows which VAO we are gonna work on, which makes sense ya know?
		// bind it then use it
		GL30.glBindVertexArray(model.getVaoID());
		// honestly NOT sure what exactly this does, it was in the tutorial tho so its still here
		// theory, chooses which attributes array inside the VAO to use to get the vertices from
		// scratch that, i know what it does. It enables an attribute array inside the vao to be
		// used when rendering, with out this that attrib array is unusable
		GL20.glEnableVertexAttribArray(0);
		GL20.glEnableVertexAttribArray(1);
		// this line is ridicilous but like here is how we make a trans matrix
		Matrix4f transMatrix = MathHelper.createTransformationMatrix(entity.getPos(), entity.getRotx(), entity.getRoty(), entity.getRotz(), entity.getScale());
		// USE UNIFORM TO MAKE THE TRANS MATRIX BE A PART OF THE RENDER WOOOOOOOOOOO
		shader.loadTransformation(transMatrix);
		// sets where the current texture is going to be stored in the texture banks
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		// bind the texture of the model as the active current texture to use because like hello how
		// else are we gonna know which texture to use
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, texModel.getTex().glTextureID());
		// this actually draws everything, it uses the bound VAO to get the vertices and it starts a the 0th vert
		// and draws model.getVertexCount() vertices using triangles
		GL11.glDrawElements(GL11.GL_TRIANGLES, model.getVertexCount(), GL11.GL_UNSIGNED_INT, 0);
		// opposite of the enable vertex attrib array call, probably just unbinds it
		// so yea this is the opposite of the enable call, it essentally says that this will not be used anymore
		GL20.glDisableVertexAttribArray(0);
		GL20.glDisableVertexAttribArray(1);
		// unbinds the currently bound VAO by passing in zero, which is the value of NULL in C/C++
		GL30.glBindVertexArray(0);
	}
}
