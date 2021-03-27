package com.joseph.test.lwjgl3.particle.cube;

import org.joml.Matrix4f;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import com.joseph.test.lwjgl3.entity.Camera;
import com.joseph.test.lwjgl3.math.MathHelper;

public class CubeParticleRenderer {
	
	private CubeParticleShader shader;
	private Matrix4f projectionMatrix;
	
	public CubeParticleRenderer(Matrix4f projectionMatrix) {
		this.shader = new CubeParticleShader();
		this.projectionMatrix = projectionMatrix;
	}
	
	public void prepare() {
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glEnable(GL11.GL_CULL_FACE);
		GL11.glCullFace(GL11.GL_BACK);
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
		GL11.glClearColor(0.2f, 0.38f, 0.31f, 1.0f);
		GL11.glEnable(GL20.GL_VERTEX_PROGRAM_POINT_SIZE);
	}
	
	public void render(CubeParticleVao particles, Camera cam) {
		shader.start();
		this.loadProjViewMatrix(cam);
		GL30.glBindVertexArray(particles.getVao());
		GL20.glEnableVertexAttribArray(0);
		GL11.glDrawArrays(GL11.GL_POINTS, 0, particles.getParticleCount());
		GL20.glDisableVertexAttribArray(0);
		GL30.glBindVertexArray(0);
		shader.stop();
	}
	
	private void loadProjViewMatrix(Camera cam) {
		Matrix4f viewMat = MathHelper.createViewMatrix(cam);
		Matrix4f projView = projectionMatrix.mul(viewMat, viewMat);
		shader.loadProjectionViewMatrix(projView);
	}
	
	public void cleanUp() {
		this.shader.cleanUp();
	}
}
