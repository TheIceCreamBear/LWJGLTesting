package com.joseph.test.lwjgl3.renderer;

import java.util.HashMap;
import java.util.List;

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

public class EntityRenderer {
	
	private StaticShader shader;
	
	/**
	 * make a renderer this shouldnt be an instance but meh maybe
	 * @param shader
	 */
	public EntityRenderer(StaticShader shader, Matrix4f projMatrix) {
		this.shader = shader;
		// start it so we can save it because like we have to save it to start it wot
		shader.start();
		// save le proj mat
		shader.loadProjection(projMatrix);
		// setup the shadowMap texutre uniform
		shader.connectTextureUnits();
		// stop it because we arent rendering anything rn like hello
		shader.stop();
	}
	
	/**
	 * new version of render that takes in a horrible idea of a think (kinda maybe) and 
	 * for each texmodel renders all the instances of that textured model into the view
	 * 
	 * basically uses batch rendering but like yea basically that 
	 * (this should be done a different way)
	 * @param entities
	 */
	public void render(HashMap<TexturedModel, List<Entity>> entities, Matrix4f toShadowSpace) {
		shader.loadShadowMapSpace(toShadowSpace);
		// get each textured model
		for (TexturedModel texMod : entities.keySet()) {
			// prep it
			this.prepareTexturedModel(texMod);
			// get the batch of entities
			List<Entity> batch = entities.get(texMod);
			// loop that (bet you did not see that coming)
			for (Entity e : batch) {
				// prep the E
				this.prepareInstance(e);
				// draw call
				GL11.glDrawElements(GL11.GL_TRIANGLES, texMod.getModel().getVertexCount(), GL11.GL_UNSIGNED_INT, 0);
			}
			
			// unbind the current one
			this.unbindTexturedModel();
		}
	}
	
	/**
	 * sets up the current textured model to be like, ready for a render call to be rendered, by 
	 * binding the vao and enabling the vbo, and doing some fun texture stuff like loading the 
	 * reflectivity property and binding the texture to a bank
	 * @param model
	 */
	private void prepareTexturedModel(TexturedModel model) {
		RawModel mod = model.getModel();
		// okay so this takes the vao that is unique to this model and it binds it as the active vao
		// which is needed so that GL knows which VAO we are gonna work on, which makes sense ya know?
		// bind it then use it
		GL30.glBindVertexArray(mod.getVaoID());
		// honestly NOT sure what exactly this does, it was in the tutorial tho so its still here
		// theory, chooses which attributes array inside the VAO to use to get the vertices from
		// scratch that, i know what it does. It enables an attribute array inside the vao to be
		// used when rendering, with out this that attrib array is unusable
		GL20.glEnableVertexAttribArray(0);
		GL20.glEnableVertexAttribArray(1);
		GL20.glEnableVertexAttribArray(2);
		
		// temp store le texture
		Texture tex = model.getTex();
		// load the size of the atlas if it is one
		shader.loadNumRows(tex.getNumRows());
		// if tex has pixels that are see through, we dont want to cull the back faces of that
		if (tex.hasTransparency()) {
			MainRenderer.disableCulling();
		}
		// also if the tex wants fake lighting, load that into the shader
		shader.loadFakeLightValue(tex.useFakedLighting());
		// bruv (simple)
		shader.loadReflectivity(tex.getReflectivity());
		// also bruv (simple)
		shader.loadShineDamper(tex.getShineDamper());
		// sets where the current texture is going to be stored in the texture banks
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		// bind the texture of the model as the active current texture to use because like hello how
		// else are we gonna know which texture to use
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, tex.glTextureID());
		// load if we use a specular map
		shader.loadUseSpecularMap(tex.hasSpecularMap());
		// if we use a map, bind it
		if (tex.hasSpecularMap()) {
			GL13.glActiveTexture(GL13.GL_TEXTURE1);
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, tex.specularMapID());
		}
	}
	
	/**
	 * disables the vertex attribs for what ever textured model is currently being used and also unbinds the VAO
	 */
	private void unbindTexturedModel() {
		// just make sure its enabled, even tho adding a new method onto the stack is alot, 
		// it might be less than checking every time if a change was made to it
		MainRenderer.enableCulling();
		// opposite of the enable vertex attrib array call, probably just unbinds it
		// so yea this is the opposite of the enable call, it essentally says that this will not be used anymore
		GL20.glDisableVertexAttribArray(0);
		GL20.glDisableVertexAttribArray(1);
		GL20.glDisableVertexAttribArray(2);
		// unbinds the currently bound VAO by passing in zero, which is the value of NULL in C/C++
		GL30.glBindVertexArray(0);
	}
	
	/**
	 * does a thing and loads a transformation matrix into the shader, specific to the current entity
	 * @param e
	 */
	private void prepareInstance(Entity e) {
		// this line is ridicilous but like here is how we make a trans matrix
		Matrix4f transMatrix = MathHelper.createTransformationMatrix(e.getPos(), e.getRotx(), e.getRoty(), e.getRotz(), e.getScale());
		// USE UNIFORM TO MAKE THE TRANS MATRIX BE A PART OF THE RENDER WOOOOOOOOOOO
		shader.loadTransformation(transMatrix);
		// load the offset for which atlas index we are using for the current entitiy, maybe like this could be done differently
		shader.loadOffset(e.getTextureXOffset(), e.getTextureYOffset());
		// load weather or not light can like go through it
		shader.loadIsLightSource(e.isLightSource());
	}
}
