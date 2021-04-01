package com.joseph.test.lwjgl3.reflectionmap.cubemap;

import org.joml.Matrix4f;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import com.joseph.test.lwjgl3.entity.Camera;
import com.joseph.test.lwjgl3.math.MathHelper;

public class CubeMapRenderer {
	private CubeMapShader shader;
	private Matrix4f projMat;
	private CubeMap cubeMap;
	
	public CubeMapRenderer(CubeMap cubeMap, Matrix4f projMat) {
		this.projMat = projMat;
		this.shader = new CubeMapShader();
		this.cubeMap = cubeMap;
	}
	
	public void render(Camera cam) {
		shader.start();
		this.loadProjViewMat(cam);
		this.bindTexture();
		this.bindCubeVao();
		GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, cubeMap.getCube().getVertexCount());
		this.unbind();
		shader.stop();
	}
	
	private void unbind() {
		GL20.glDisableVertexAttribArray(0);
		GL30.glBindVertexArray(0);
	}
	
	private void bindCubeVao() {
		GL30.glBindVertexArray(cubeMap.getCube().getVaoID());
		GL20.glEnableVertexAttribArray(0);
	}
	
	private void bindTexture() {
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL13.GL_TEXTURE_CUBE_MAP, cubeMap.getTexture());
	}
	
	private void loadProjViewMat(Camera cam) {
		Matrix4f viewMat = MathHelper.createViewMatrix(cam);
		Matrix4f projViewMat = projMat.mul(viewMat, viewMat);
		shader.loadProjViewMat(projViewMat);
	}
	
	public void cleanUp() {
		this.shader.cleanUp();
	}
}
