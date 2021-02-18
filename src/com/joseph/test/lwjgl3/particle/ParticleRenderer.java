package com.joseph.test.lwjgl3.particle;

import java.nio.FloatBuffer;
import java.util.List;
import java.util.Map;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.GL31;

import com.joseph.test.lwjgl3.entity.Camera;
import com.joseph.test.lwjgl3.math.MathHelper;
import com.joseph.test.lwjgl3.models.ModelLoader;
import com.joseph.test.lwjgl3.models.RawModel;

public class ParticleRenderer {
	private static final float[] VERTICIES = {-0.5f, 0.5f, -0.5f, -0.5f, 0.5f, 0.5f, 0.5f, -0.5f};
	private static final int MAX_INSTANCES = 10000;
	private static final int INSTANCE_DATA_LENGTH = 21;
	
	private static final FloatBuffer buffer = BufferUtils.createFloatBuffer(MAX_INSTANCES * INSTANCE_DATA_LENGTH);
	
	private RawModel quad;
	private ParticleShader shader;
	private int vbo;
	private int pointer = 0;

	public ParticleRenderer(Matrix4f projMat) {
		quad = ModelLoader.instance.loadToVAO(VERTICIES, 2);
		this.vbo = ModelLoader.instance.createEmptyVBO(INSTANCE_DATA_LENGTH * MAX_INSTANCES);
		ModelLoader.instance.addInstancedAttribute(quad.getVaoID(), vbo, 1, 4, INSTANCE_DATA_LENGTH, 0); // col 1
		ModelLoader.instance.addInstancedAttribute(quad.getVaoID(), vbo, 2, 4, INSTANCE_DATA_LENGTH, 4); // col 2
		ModelLoader.instance.addInstancedAttribute(quad.getVaoID(), vbo, 3, 4, INSTANCE_DATA_LENGTH, 8); // col 3
		ModelLoader.instance.addInstancedAttribute(quad.getVaoID(), vbo, 4, 4, INSTANCE_DATA_LENGTH, 12); // col 4
		ModelLoader.instance.addInstancedAttribute(quad.getVaoID(), vbo, 5, 4, INSTANCE_DATA_LENGTH, 16); // offset data
		ModelLoader.instance.addInstancedAttribute(quad.getVaoID(), vbo, 6, 1, INSTANCE_DATA_LENGTH, 20); // blend fact
		shader = new ParticleShader();
		shader.start();
		shader.loadProjection(projMat);
		shader.stop();
	}
	
	public void render(Map<ParticleTexture, List<Particle>> particles, Camera cam) {
		Matrix4f viewMat = MathHelper.createViewMatrix(cam);
		// prepare
		this.prepare();
		// loop through particle textures
		for (ParticleTexture tex : particles.keySet()) {
			// bind it
			this.bindTexture(tex);
			// get the list
			List<Particle> particleList = particles.get(tex);
			// reset this
			pointer = 0;
			// create the new data array
			float[] vboData = new float[particleList.size() * INSTANCE_DATA_LENGTH];
			// loop through each particle
			for (Particle particle : particleList) {
				// update the view matrix, and put it into the new data array
				this.updateModelViewMatrix(particle.getPosition(), particle.getRotation(), particle.getScale(), viewMat, vboData);
				// put the tex coord info into the data array
				this.updateTexCoordInfo(particle, vboData);
			}
			// put the new data into the vbo
			ModelLoader.instance.updateVbo(vbo, vboData, buffer);
			// draw all these particles
			GL31.glDrawArraysInstanced(GL11.GL_TRIANGLE_STRIP, 0, quad.getVertexCount(), particleList.size());
		}
		this.postRender();
	}
	
	private void prepare() {
		shader.start();
		GL30.glBindVertexArray(quad.getVaoID());
		GL20.glEnableVertexAttribArray(0);
		GL20.glEnableVertexAttribArray(1);
		GL20.glEnableVertexAttribArray(2);
		GL20.glEnableVertexAttribArray(3);
		GL20.glEnableVertexAttribArray(4);
		GL20.glEnableVertexAttribArray(5);
		GL20.glEnableVertexAttribArray(6);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glDepthMask(false);
	}
	
	private void updateTexCoordInfo(Particle particle, float[] data) {
		data[pointer++] = particle.getCurOffset().x;
		data[pointer++] = particle.getCurOffset().y;
		data[pointer++] = particle.getNextOffset().x;
		data[pointer++] = particle.getNextOffset().y;
		data[pointer++] = particle.getBlendFactor();
	}
	
	private void bindTexture(ParticleTexture tex) {
		// TODO care about different types of blending?
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, tex.getId());
		shader.loadNumRows(tex.getNumRows());
	}
	
	private void updateModelViewMatrix(Vector3f position, float rotation, float scale, Matrix4f view, float[] vboData) {
		Matrix4f model = new Matrix4f();
		model.translate(position);
		model.m00(view.m00());
		model.m01(view.m10());
		model.m02(view.m20());
		model.m10(view.m01());
		model.m11(view.m11());
		model.m12(view.m21());
		model.m20(view.m02());
		model.m21(view.m12());
		model.m22(view.m22());
		model.rotateZ((float) Math.toRadians(rotation));
		model.scale(scale);
		Matrix4f modelView = view.mul(model, new Matrix4f());
		this.storeMatrixData(modelView, vboData);
	}
	
	private void storeMatrixData(Matrix4f matrix, float[] vboData) {
		vboData[pointer++] = matrix.m00();
		vboData[pointer++] = matrix.m01();
		vboData[pointer++] = matrix.m02();
		vboData[pointer++] = matrix.m03();
		vboData[pointer++] = matrix.m10();
		vboData[pointer++] = matrix.m11();
		vboData[pointer++] = matrix.m12();
		vboData[pointer++] = matrix.m13();
		vboData[pointer++] = matrix.m20();
		vboData[pointer++] = matrix.m21();
		vboData[pointer++] = matrix.m22();
		vboData[pointer++] = matrix.m23();
		vboData[pointer++] = matrix.m30();
		vboData[pointer++] = matrix.m31();
		vboData[pointer++] = matrix.m32();
		vboData[pointer++] = matrix.m33();
	}
	
	private void postRender() {
		GL11.glDepthMask(true);
		GL11.glDisable(GL11.GL_BLEND);
		GL20.glDisableVertexAttribArray(0);
		GL20.glDisableVertexAttribArray(1);
		GL20.glDisableVertexAttribArray(2);
		GL20.glDisableVertexAttribArray(3);
		GL20.glDisableVertexAttribArray(4);
		GL20.glDisableVertexAttribArray(5);
		GL20.glDisableVertexAttribArray(6);
		GL30.glBindVertexArray(0);
		shader.stop();
	}
	
	public void cleanUp() {
		this.shader.cleanUp();
	}
}
