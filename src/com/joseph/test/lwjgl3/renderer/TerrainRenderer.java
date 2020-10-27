package com.joseph.test.lwjgl3.renderer;

import java.util.List;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import com.joseph.test.lwjgl3.entity.Entity;
import com.joseph.test.lwjgl3.math.MathHelper;
import com.joseph.test.lwjgl3.models.RawModel;
import com.joseph.test.lwjgl3.models.TexturedModel;
import com.joseph.test.lwjgl3.shaders.TerrainShader;
import com.joseph.test.lwjgl3.terrain.Terrain;
import com.joseph.test.lwjgl3.textures.Texture;

public class TerrainRenderer {
	private TerrainShader shader;
	
	/**
	 * make a new renderer instance obv
	 * @param shader
	 * @param projMat
	 */
	public TerrainRenderer(TerrainShader shader, Matrix4f projMat) {
		this.shader = shader;
		shader.start();
		shader.loadProjection(projMat);
		shader.stop();
	}
	
	/**
	 * render the list of terrains
	 * @param terrains
	 */
	public void render(List<Terrain> terrains) {
		for (Terrain t : terrains) {
			// prep
			this.prepareTerrain(t);
			// load
			this.loadModelMatrix(t);
			// draw call
			GL11.glDrawElements(GL11.GL_TRIANGLES, t.getModel().getVertexCount(), GL11.GL_UNSIGNED_INT, 0);
			// unbind
			this.unbindTerrain();
		}
	}
	
	/**
	 * sets up the current Terrain ready for a render call to be rendered, by 
	 * binding the vao and enabling the vbo, and doing some fun texture stuff like loading the 
	 * reflectivity property and binding the texture to a bank
	 * @param t
	 */
	private void prepareTerrain(Terrain t) {
		RawModel mod = t.getModel();
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
		Texture tex = t.getTexture();
		// bruv (simple)
		shader.loadReflectivity(tex.getReflectivity());
		// also bruv (simple)
		shader.loadShineDamper(tex.getShineDamper());
		// sets where the current texture is going to be stored in the texture banks
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		// bind the texture of the model as the active current texture to use because like hello how
		// else are we gonna know which texture to use
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, tex.glTextureID());
	}
	
	/**
	 * disables the vertex attribs for what ever terrain is currently being used and also unbinds the VAO
	 */
	private void unbindTerrain() {
		// opposite of the enable vertex attrib array call, probably just unbinds it
		// so yea this is the opposite of the enable call, it essentally says that this will not be used anymore
		GL20.glDisableVertexAttribArray(0);
		GL20.glDisableVertexAttribArray(1);
		GL20.glDisableVertexAttribArray(2);
		// unbinds the currently bound VAO by passing in zero, which is the value of NULL in C/C++
		GL30.glBindVertexArray(0);
	}
	
	/**
	 * does a thing and loads a transformation matrix into the shader, specific to the current Terrain
	 * @param t
	 */
	private void loadModelMatrix(Terrain t) {
		// this line is ridicilous but like here is how we make a trans matrix
		Matrix4f transMatrix = MathHelper.createTransformationMatrix(new Vector3f(t.getX(), 0, t.getZ()), 0, 0, 0, 1);
		// USE UNIFORM TO MAKE THE TRANS MATRIX BE A PART OF THE RENDER WOOOOOOOOOOO
		shader.loadTransformation(transMatrix);
	}
}
