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
import com.joseph.test.lwjgl3.textures.Texture;

public class Renderer {
	// bruv, what if le user wants to change this (although i dont like that idea so)
	private static final float FOV = 70;
	private static final float NEAR_PLANE = 0.1f;
	private static final float FAR_PLANE = 1000f;
	
	private Matrix4f projMatrix;
	
	/**
	 * make a renderer this shouldnt be an instance but meh maybe
	 * @param shader
	 */
	public Renderer(StaticShader shader) {
		// enable the like renderer api to like, decide to not render faces that we cant see so like performance gains
		GL11.glEnable(GL11.GL_CULL_FACE);
		GL11.glCullFace(GL11.GL_BACK);
		// create the projection matrix
		this.createProjectionMatrix();
		// start it so we can save it because like we have to save it to start it wot
		shader.start();
		// save le proj mat
		shader.loadProjection(projMatrix);
		// stsop it because we arent rendering anything rn like hello
		shader.stop();
	}
	
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
		GL20.glEnableVertexAttribArray(2);
		// this line is ridicilous but like here is how we make a trans matrix
		Matrix4f transMatrix = MathHelper.createTransformationMatrix(entity.getPos(), entity.getRotx(), entity.getRoty(), entity.getRotz(), entity.getScale());
		// USE UNIFORM TO MAKE THE TRANS MATRIX BE A PART OF THE RENDER WOOOOOOOOOOO
		shader.loadTransformation(transMatrix);
		// temp store le texture
		Texture tex = texModel.getTex();
		// bruv (simple)
		shader.loadReflectivity(tex.getReflectivity());
		// also bruv (simple)
		shader.loadShineDamper(tex.getShineDamper());
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
		GL20.glDisableVertexAttribArray(2);
		// unbinds the currently bound VAO by passing in zero, which is the value of NULL in C/C++
		GL30.glBindVertexArray(0);
	}
	
	/**
	 * Creates a projection matrix to project the z axis and stuff
	 */
	private void createProjectionMatrix() {
		// TODO these should not be constant
		float width = 1600;
		float height = 900;
		float aspectRatio = width / height;
		float yScale = (float) ((1f / Math.tan(Math.toRadians(FOV / 2f))) * aspectRatio);
		float xScale = yScale / aspectRatio;
		float frustumLength = FAR_PLANE - NEAR_PLANE;
		
		projMatrix = new Matrix4f();
		projMatrix.m00(xScale);
		projMatrix.m11(yScale);
		projMatrix.m22(-(FAR_PLANE + NEAR_PLANE) / frustumLength);
		projMatrix.m23(-1.0f);
		projMatrix.m32(-(2 * NEAR_PLANE * FAR_PLANE) / frustumLength);
		projMatrix.m33(0.0f);
	}
	
	private void createProjectionMatrixMC() {
		// TODO these should not be constant
		// also does not work
		// IGNORE THIS thanks
		float width = 1600;
		float height = 900;
		projMatrix = new Matrix4f();
		projMatrix.m00(2.0f / width);
		projMatrix.m11(2.0f / height);
		float frustumLength = FAR_PLANE - NEAR_PLANE;
		projMatrix.m22(-2.0f / frustumLength);
		projMatrix.m33(1.0f);
		projMatrix.m03(-1.0f);
		projMatrix.m13(-1.0f);
		projMatrix.m23(-(FAR_PLANE + NEAR_PLANE) / frustumLength);
	}
}
