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
	
	private static final String[] NIGHT_TEXTURE_FILES = {
		"res/provided/skybox/nightRight.png",
		"res/provided/skybox/nightLeft.png",
		"res/provided/skybox/nightTop.png",
		"res/provided/skybox/nightBottom.png",
		"res/provided/skybox/nightBack.png",
		"res/provided/skybox/nightFront.png"
	};
	
	private RawModel cube;
	private int tex;
	private int nightTex;
	private SkyboxShader shader;
	private float time = 0.0f;
	
	public SkyboxRenderer(ModelLoader loader, Matrix4f projectionMatrix) {
		// load the cube into a vao cause duh
		cube = loader.loadToVAO(VERTICES, 3);
		// we also probably need a texture
		tex = TextureLoader.loadCubeMap(TEXTURE_FILES);
		// we also also probably need another texture
		nightTex = TextureLoader.loadCubeMap(NIGHT_TEXTURE_FILES);
		// create a shader
		shader = new SkyboxShader();
		// start it
		shader.start();
		// make the samplers act right
		shader.connectTexUnits();
		// load the proj mat
		shader.loadProjection(projectionMatrix);
		// stop it
		shader.stop();
	}
	
	public void render(Camera cam, float delta, float r, float g, float b) {
		// disable depth testing for the skybox
//		GL11.glDepthMask(false);
//		GL11.glDepthRange(1.0f, 1.0f);
		// start it back up again
		shader.start();
		// load the view of the cam
		shader.loadViewMatrix(cam, delta);
		// load the fog color
		shader.loadFogColor(r, g, b);
		// bind the vao of the cube
		GL30.glBindVertexArray(cube.getVaoID());
		// enable the positions
		GL20.glEnableVertexAttribArray(0);
		// do it in a method
		this.bindTextures(delta);
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
	
	private void bindTextures(float delta) {
		// weird provided day night cycle type thing
		// not the best but following the TUT
		// probably wont have a dynamic skybox in the actual engine
		time += delta * 1000;
		time %= 24000;
		int texture1;
		int texture2;
		float blend;
		if (time >= 0 && time < 5000) {
			texture1 = nightTex;
			texture2 = nightTex;
			blend = (time - 0) / (5000 - 0);
		} else if (time >= 5000 && time < 8000) {
			texture1 = nightTex;
			texture2 = tex;
			blend = (time - 5000) / (8000 - 5000);
		} else if (time >= 8000 && time < 21000) {
			texture1 = tex;
			texture2 = tex;
			blend = (time - 8000) / (21000 - 8000);
		} else {
			texture1 = tex;
			texture2 = nightTex;
			blend = (time - 21000) / (24000 - 21000);
		}
		
		// tell whcih texture we are using
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		// bind it
		GL11.glBindTexture(GL13.GL_TEXTURE_CUBE_MAP, texture1);
		// tell whcih texture we are using
		GL13.glActiveTexture(GL13.GL_TEXTURE1);
		// bind it
		GL11.glBindTexture(GL13.GL_TEXTURE_CUBE_MAP, texture2);
		// load the factor
		shader.loadBlendFactor(blend);
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
