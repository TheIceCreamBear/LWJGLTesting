package com.joseph.test.lwjgl3.shadows;

import java.util.List;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import com.joseph.test.lwjgl3.math.MathHelper;
import com.joseph.test.lwjgl3.models.RawModel;
import com.joseph.test.lwjgl3.terrain.Terrain;

public class ShadowMapTerrainRenderer {
	private Matrix4f projectionViewMatrix;
	private ShadowShader shader;
	
	protected ShadowMapTerrainRenderer(ShadowShader shader, Matrix4f projectionViewMatrix) {
		this.shader = shader;
		this.projectionViewMatrix = projectionViewMatrix;
	}
	
	protected void render(List<Terrain> terrains) {
		for (Terrain t : terrains) {
			GL13.glActiveTexture(GL13.GL_TEXTURE0);
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, t.getTexPack().getRTex().glTextureID());
			prepareTerrain(t);
			GL11.glDrawElements(GL11.GL_TRIANGLES, t.getModel().getVertexCount(), GL11.GL_UNSIGNED_INT, 0);
		}
		GL20.glDisableVertexAttribArray(0);
		GL20.glDisableVertexAttribArray(1);
		GL30.glBindVertexArray(0);
	}
	
	private void prepareTerrain(Terrain t) {
		RawModel mod = t.getModel();
		GL30.glBindVertexArray(mod.getVaoID());
		GL20.glEnableVertexAttribArray(0);
		GL20.glEnableVertexAttribArray(1);
		Matrix4f modelMatrix = MathHelper.createTransformationMatrix(new Vector3f(t.getX(), 0, t.getZ()), 0, 0, 0, 1);
		Matrix4f mvpMatrix = projectionViewMatrix.mul(modelMatrix, modelMatrix);
		shader.loadMvpMatrix(mvpMatrix);
	}
	
}
