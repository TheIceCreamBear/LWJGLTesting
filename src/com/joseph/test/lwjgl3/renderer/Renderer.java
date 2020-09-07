package com.joseph.test.lwjgl3.renderer;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import com.joseph.test.lwjgl3.models.RawModel;

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
	public void render(RawModel model) {
		// okay so this takes the vao that is unique to this model and it binds it as the active vao
		// which is needed so that GL knows which VAO we are gonna work on, which makes sense ya know?
		// bind it then use it
		GL30.glBindVertexArray(model.getVaoID());
		// honestly NOT sure what exactly this does, it was in the tutorial tho so its still here
		// theory, chooses which attributes array inside the VAO to use to get the vertices from
		GL20.glEnableVertexAttribArray(0);
		// this actually draws everything, it uses the bound VAO to get the vertices and it starts a the 0th vert
		// and draws model.getVertexCount() vertices using triangles
		GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, model.getVertexCount());
		// opposite of the enable vertex attrib array call, probably just unbinds it
		GL20.glDisableVertexAttribArray(0);
		// unbinds the currently bound VAO by passing in zero, which is the value of NULL in C/C++
		GL30.glBindVertexArray(0);
	}
}
