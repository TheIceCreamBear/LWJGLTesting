package com.joseph.test.lwjgl3.reflectionmap.render;

import java.util.List;

import org.joml.Matrix4f;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import com.joseph.test.lwjgl3.entity.Camera;
import com.joseph.test.lwjgl3.entity.Entity;
import com.joseph.test.lwjgl3.math.MathHelper;
import com.joseph.test.lwjgl3.models.RawModel;
import com.joseph.test.lwjgl3.models.TexturedModel;
import com.joseph.test.lwjgl3.reflectionmap.cubemap.CubeMap;

public class ReflectionEntityRenderer {
	private ReflectionEntityShader shader;
	private CubeMap envMap;
	
	public ReflectionEntityRenderer(Matrix4f projMat, CubeMap envMap) {
		this.envMap = envMap;
		this.shader = new ReflectionEntityShader();
		this.shader.start();
		this.shader.loadProjMat(projMat);
		this.shader.connectTextureUnits();
		this.shader.stop();
	}
	
	public void render(List<Entity> entities, Camera cam) {
		shader.start();
		shader.loadViewMat(cam);
		this.bindEnvrionmentMap();
		for (Entity entity : entities) {
			TexturedModel model = entity.getModel();
			this.bindModelVao(model);
			this.loadModelMatrix(entity);
			this.bindTexture(model);
			GL11.glDrawElements(GL11.GL_TRIANGLES, model.getModel().getVertexCount(), GL11.GL_UNSIGNED_INT, 0);
			this.unbindVao();
		}
		shader.stop();
	}
	
	private void bindModelVao(TexturedModel model) {
		RawModel rawModel = model.getModel();
		GL30.glBindVertexArray(rawModel.getVaoID());
		GL20.glEnableVertexAttribArray(0);
		GL20.glEnableVertexAttribArray(1);
		GL20.glEnableVertexAttribArray(2);
	}
	
	private void unbindVao() {
		GL20.glDisableVertexAttribArray(0);
		GL20.glDisableVertexAttribArray(1);
		GL20.glDisableVertexAttribArray(2);
		GL30.glBindVertexArray(0);
	}

	private void bindTexture(TexturedModel model) {
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, model.getTex().glTextureID());
	}
	
	private void bindEnvrionmentMap() {
		GL13.glActiveTexture(GL13.GL_TEXTURE1);
		GL11.glBindTexture(GL13.GL_TEXTURE_CUBE_MAP, envMap.getTexture());		
	}
	
	private void loadModelMatrix(Entity e) {
		Matrix4f mat = MathHelper.createTransformationMatrix(e.getPos(), e.getRotx(), e.getRoty(), e.getRotz(), e.getScale());
		shader.loadTransMat(mat);
	}
	
	public void cleanUp() {
		shader.cleanUp();
	}
}
