package com.joseph.test.lwjgl3.particle.cube;

import java.nio.FloatBuffer;
import java.util.List;

import org.joml.Vector3f;
import org.lwjgl.BufferUtils;

import com.joseph.test.lwjgl3.models.ModelLoader;

public class CubeParticleVao {
	private static final int MAX_PARTICLES = 1000;
	private static final FloatBuffer buffer = BufferUtils.createFloatBuffer(MAX_PARTICLES * 3);
	
	private int vao;
	private int vbo;
	private int particleCount = 0;
	
	public CubeParticleVao(int vao, int vbo) {
		this.vao = vao;
		this.vbo = vbo;
	}
	
	public void store(List<CubeParticle> particles) {
		this.particleCount = Math.min(particles.size(), MAX_PARTICLES);
		float[] particleData = new float[this.particleCount * 3];
		for (int i = 0; i < this.particleCount; i++) {
			Vector3f pos = particles.get(i).getPosition();
			particleData[i * 3] = pos.x;
			particleData[i * 3 + 1] = pos.y;
			particleData[i * 3 + 2] = pos.z;
		}
		ModelLoader.instance.updateVbo(vbo, particleData, buffer);
	}
	
	public int getVao() {
		return this.vao;
	}
	
	public int getParticleCount() {
		return this.particleCount;
	}
	
	public static CubeParticleVao create() {
		return ModelLoader.instance.createCubeVao(MAX_PARTICLES);
	}
}
