package com.joseph.test.lwjgl3.renderer.nm;

import java.util.HashMap;
import java.util.List;

import org.joml.Matrix4f;
import org.joml.Vector4f;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import com.joseph.test.lwjgl3.entity.Camera;
import com.joseph.test.lwjgl3.entity.Entity;
import com.joseph.test.lwjgl3.entity.Light;
import com.joseph.test.lwjgl3.math.MathHelper;
import com.joseph.test.lwjgl3.models.RawModel;
import com.joseph.test.lwjgl3.models.TexturedModel;
import com.joseph.test.lwjgl3.renderer.MainRenderer;
import com.joseph.test.lwjgl3.textures.Texture;

public class NormalMapRenderer {

	private NormalMapShader shader;

	public NormalMapRenderer(Matrix4f projMatrix) {
		this.shader = new NormalMapShader();
		// start it so we can save it because like we have to save it to start it wot
		shader.start();
		// save le proj mat
		shader.loadProjectionMatrix(projMatrix);
		// connect le units de texture
		shader.connectTextureUnits();
		// stop it because we arent rendering anything rn like hello
		shader.stop();
	}

	/**
	 * handles all the heavy lifting for the redering for an entity with a normal map
	 * @param entities
	 * @param clipPlane
	 * @param lights
	 * @param camera
	 */
	public void render(HashMap<TexturedModel, List<Entity>> entities, Vector4f clipPlane, List<Light> lights, Camera camera) {
		this.shader.start();
		this.prepare(clipPlane, lights, camera);
		// get each textured model
		for (TexturedModel model : entities.keySet()) {
			// prep it
			this.prepareTexturedModel(model);
			// get the batch of entities
			List<Entity> batch = entities.get(model);
			// loop that (bet you did see that coming) [why repeat the same joke?]
			for (Entity e : batch) {
				// prep the E
				this.prepareInstance(e);
				// draw call
				GL11.glDrawElements(GL11.GL_TRIANGLES, model.getModel().getVertexCount(), GL11.GL_UNSIGNED_INT, 0);
			}
			// unbind the current one
			this.unbindTexturedModel();
		}
		// stop it cause we done rendering
		this.shader.stop();
	}
	
	/**
	 * clean up our mess
	 */
	public void cleanUp(){
		this.shader.cleanUp();
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
		GL20.glEnableVertexAttribArray(3);

		// temp store le texture
		Texture tex = model.getTex();
		// load the size of the atlas if it is one
		shader.loadNumberOfRows(tex.getNumRows());
		// if tex has pixels that are see through, we dont want to cull the back faces of that
		if (tex.hasTransparency()) {
			// side note here, this new system doesnt contain the code that ignores light and what not
			// when the thing is rendered. gonna guess thats handled by the normal stuff???? not sure?
			MainRenderer.disableCulling();
		}
		// bruv (simple)
		shader.loadReflectivity(tex.getReflectivity());
		// also bruv (simple)
		shader.loadShineDamper(tex.getShineDamper());
		// sets where the current texture is going to be stored in the texture banks
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		// bind the texture of the model as the active current texture to use because like hello how
		// else are we gonna know which texture to use
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, tex.glTextureID());
		// so this part is the setting of texture for the normal map into bank 1
		GL13.glActiveTexture(GL13.GL_TEXTURE1);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, tex.normalMapID());
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
		GL20.glDisableVertexAttribArray(3);
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
	}

	private void prepare(Vector4f clipPlane, List<Light> lights, Camera camera) {
		shader.loadClipPlane(clipPlane);
		shader.loadSkyColor(MainRenderer.RED, MainRenderer.GREEN, MainRenderer.BLUE);
		Matrix4f viewMatrix = MathHelper.createViewMatrix(camera);
		shader.loadLights(lights, viewMatrix);
		shader.loadViewMatrix(viewMatrix);
	}

}
