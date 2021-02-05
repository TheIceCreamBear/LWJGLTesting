package com.joseph.test.lwjgl3.water;

import java.util.List;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import com.joseph.test.lwjgl3.Main;
import com.joseph.test.lwjgl3.entity.Camera;
import com.joseph.test.lwjgl3.entity.Light;
import com.joseph.test.lwjgl3.math.MathHelper;
import com.joseph.test.lwjgl3.models.ModelLoader;
import com.joseph.test.lwjgl3.models.RawModel;
import com.joseph.test.lwjgl3.textures.Texture;
import com.joseph.test.lwjgl3.textures.TextureLoader;


public class WaterRenderer {
	private static final String DUDV_MAP = "res/provided/waterDUDV.png";
	private static final String NORMAL_MAP = "res/provided/matchingNormalMap.png";
	private static final float WAVE_SPEED = 0.03f;

	private RawModel quad;
	private WaterShader shader;
	private WaterFrameBuffers wfb;
	
	private Texture dudvTex;
	private Texture normalMap;
	private float moveFactor = 0.0f;

	public WaterRenderer(ModelLoader loader, WaterShader shader, Matrix4f projectionMatrix, WaterFrameBuffers wfb) {
		this.shader = shader;
		this.wfb = wfb;
		this.dudvTex = TextureLoader.loadTexture(DUDV_MAP);
		this.normalMap = TextureLoader.loadTexture(NORMAL_MAP);
		shader.start();
		shader.connectTextures();
		shader.loadProjectionMatrix(projectionMatrix);
		shader.stop();
		setUpVAO(loader);
	}

	public void render(List<WaterTile> water, Camera camera, Light sky) {
		prepareRender(camera, sky);	
		for (WaterTile tile : water) {
			Matrix4f modelMatrix = MathHelper.createTransformationMatrix(new Vector3f(tile.getX(), tile.getHeight(), tile.getZ()), 0, 0, 0, WaterTile.TILE_SIZE);
			shader.loadModelMatrix(modelMatrix);
			GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, quad.getVertexCount());
		}
		unbind();
	}
	
	private void prepareRender(Camera camera, Light sky) {
		shader.start();
		shader.loadViewMatrix(camera);
		shader.loadLight(sky);
		moveFactor += WAVE_SPEED * Main.delta;
		moveFactor %= 1;
		shader.loadMoveFactor(moveFactor);
		GL30.glBindVertexArray(quad.getVaoID());
		GL20.glEnableVertexAttribArray(0);
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.wfb.getReflectionTexture());
		GL13.glActiveTexture(GL13.GL_TEXTURE1);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.wfb.getRefractionTexture());
		GL13.glActiveTexture(GL13.GL_TEXTURE2);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.dudvTex.glTextureID());
		GL13.glActiveTexture(GL13.GL_TEXTURE3);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.normalMap.glTextureID());
		GL13.glActiveTexture(GL13.GL_TEXTURE4);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.wfb.getRefractionDepthTexture());
		
		// enable alpha blending
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
	}
	
	private void unbind() {
		GL11.glDisable(GL11.GL_BLEND);
		GL20.glDisableVertexAttribArray(0);
		GL30.glBindVertexArray(0);
		shader.stop();
	}

	private void setUpVAO(ModelLoader loader) {
		// Just x and z vectex positions here, y is set to 0 in v.shader
		float[] vertices = { -1, -1, -1, 1, 1, -1, 1, -1, -1, 1, 1, 1 };
		quad = loader.loadToVAO(vertices, 2);
	}

}
