package com.joseph.test.lwjgl3.particle;

import java.util.List;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import com.joseph.test.lwjgl3.entity.Camera;
import com.joseph.test.lwjgl3.math.MathHelper;
import com.joseph.test.lwjgl3.models.ModelLoader;
import com.joseph.test.lwjgl3.models.RawModel;

public class ParticleRenderer {
	private static final float[] VERTICIES = {-0.5f, 0.5f, -0.5f, -0.5f, 0.5f, 0.5f, 0.5f, -0.5f};
	
	private RawModel quad;
	private ParticleShader shader;

	public ParticleRenderer(Matrix4f projMat) {
		quad = ModelLoader.instance.loadToVAO(VERTICIES, 2);
		shader = new ParticleShader();
		shader.start();
		shader.loadProjection(projMat);
		shader.stop();
	}
	
	public void render(List<Particle> particles, Camera cam) {
		Matrix4f viewMat = MathHelper.createViewMatrix(cam);
		this.prepare();
		for (Particle particle : particles) {
			this.updateModelViewMatrix(particle.getPosition(), particle.getRotation(), particle.getScale(), viewMat);
			GL11.glDrawArrays(GL11.GL_TRIANGLE_STRIP, 0, quad.getVertexCount());
		}
		this.postRender();
	}
	
	private void prepare() {
		shader.start();
		GL30.glBindVertexArray(quad.getVaoID());
		GL20.glEnableVertexAttribArray(0);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glDepthMask(false);
	}
	
	private void updateModelViewMatrix(Vector3f position, float rotation, float scale, Matrix4f view) {
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
		shader.loadModelViewMatrix(modelView);
	}
	
	private void postRender() {
		GL11.glDepthMask(true);
		GL11.glDisable(GL11.GL_BLEND);
		GL20.glDisableVertexAttribArray(0);
		GL30.glBindVertexArray(0);
		shader.stop();
	}
	
	public void cleanUp() {
		this.shader.cleanUp();
	}
}
