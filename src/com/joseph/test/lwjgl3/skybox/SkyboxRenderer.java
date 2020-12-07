package com.joseph.test.lwjgl3.skybox;

import org.joml.Matrix4f;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import com.joseph.test.lwjgl3.entity.Camera;
import com.joseph.test.lwjgl3.models.ModelLoader;
import com.joseph.test.lwjgl3.models.RawModel;
import com.joseph.test.lwjgl3.textures.TextureLoader;

public class SkyboxRenderer {
	private static final float SIZE = 500.0f;
	
	private static final float[] VERTICES = {        
	    -SIZE,  SIZE, -SIZE,
	    -SIZE, -SIZE, -SIZE,
	     SIZE, -SIZE, -SIZE,
	     SIZE, -SIZE, -SIZE,
	     SIZE,  SIZE, -SIZE,
	    -SIZE,  SIZE, -SIZE,

	    -SIZE, -SIZE,  SIZE,
	    -SIZE, -SIZE, -SIZE,
	    -SIZE,  SIZE, -SIZE,
	    -SIZE,  SIZE, -SIZE,
	    -SIZE,  SIZE,  SIZE,
	    -SIZE, -SIZE,  SIZE,

	     SIZE, -SIZE, -SIZE,
	     SIZE, -SIZE,  SIZE,
	     SIZE,  SIZE,  SIZE,
	     SIZE,  SIZE,  SIZE,
	     SIZE,  SIZE, -SIZE,
	     SIZE, -SIZE, -SIZE,

	    -SIZE, -SIZE,  SIZE,
	    -SIZE,  SIZE,  SIZE,
	     SIZE,  SIZE,  SIZE,
	     SIZE,  SIZE,  SIZE,
	     SIZE, -SIZE,  SIZE,
	    -SIZE, -SIZE,  SIZE,

	    -SIZE,  SIZE, -SIZE,
	     SIZE,  SIZE, -SIZE,
	     SIZE,  SIZE,  SIZE,
	     SIZE,  SIZE,  SIZE,
	    -SIZE,  SIZE,  SIZE,
	    -SIZE,  SIZE, -SIZE,

	    -SIZE, -SIZE, -SIZE,
	    -SIZE, -SIZE,  SIZE,
	     SIZE, -SIZE, -SIZE,
	     SIZE, -SIZE, -SIZE,
	    -SIZE, -SIZE,  SIZE,
	     SIZE, -SIZE,  SIZE
	};
	
	private static final String[] TEXTURE_FILES = {
		"res/provided/skybox/right.png",
		"res/provided/skybox/left.png",
		"res/provided/skybox/top.png",
		"res/provided/skybox/bottom.png",
		"res/provided/skybox/back.png",
		"res/provided/skybox/front.png"
	};
	
	private RawModel cube;
	private int texture;
	private SkyboxShader shader;
	
	public SkyboxRenderer(ModelLoader loader, Matrix4f projectionMatrix) {
		// load the cube into a vao cause duh
		cube = loader.loadToVAO(VERTICES, 3);
		// we also probably need a texture
		texture = TextureLoader.loadCubeMap(TEXTURE_FILES);
		// create a shader
		shader = new SkyboxShader();
		// start it
		shader.start();
		// load the proj mat
		shader.loadProjection(projectionMatrix);
		// stop it
		shader.stop();
	}
	
	public void render(Camera cam) {
		// disable depth testing for the skybox
//		GL11.glDepthMask(false);
//		GL11.glDepthRange(1.0f, 1.0f);
		// start it back up again
		shader.start();
		// load the view of the cam
		shader.loadViewMatrix(cam);
		// bind the vao of the cube
		GL30.glBindVertexArray(cube.getVaoID());
		// enable the positions
		GL20.glEnableVertexAttribArray(0);
		// tell whcih texture we are using
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		// bind it
		GL11.glBindTexture(GL13.GL_TEXTURE_CUBE_MAP, texture);
		// draw the sky box
		GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, cube.getVertexCount());
		// disable
		GL20.glDisableVertexAttribArray(0);
		// disable
		GL30.glBindVertexArray(0);
		// stop
		shader.stop();
		// renable depth testing 
//		GL11.glDepthRange(0.0f, 1.0f);
//		GL11.glDepthMask(true);
	}

	/**
	 * that meme where java is like "im not using this memory anymore, So im just going to recycle it"
	 * but C/C++ is like *drops it* *points* "pick it up bitch"
	 * 
	 * see https://www.reddit.com/r/ProgrammerHumor/comments/ix74sv/garbage_collection/
	 */
	public void cleanUp() {
		shader.cleanUp();
	}
}
