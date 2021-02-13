package com.joseph.test.lwjgl3.particle.example;

import org.joml.Vector3f;

import com.joseph.test.lwjgl3.Main;
import com.joseph.test.lwjgl3.particle.Particle;

/**
 * Provided example class of a particle system and how it would generate particles
 * the ideas in here arent bad per say, but not really how i would do it. 
 * this system is exactly as downloaded, with the only adjustments being this comment and
 * adjusting code to not cause syntax errors
 */
public class SimpleParticleExample {
	private float pps;
	private float speed;
	private float gravityComplient;
	private float lifeLength;
	
	public SimpleParticleExample(float pps, float speed, float gravityComplient, float lifeLength) {
		this.pps = pps;
		this.speed = speed;
		this.gravityComplient = gravityComplient;
		this.lifeLength = lifeLength;
	}
	
	public void generateParticles(Vector3f systemCenter) {
		float delta = Main.delta;
		float particlesToCreate = pps * delta;
		int count = (int) Math.floor(particlesToCreate);
		float partialParticle = particlesToCreate % 1;
		for (int i = 0; i < count; i++) {
			emitParticle(systemCenter);
		}
		if (Math.random() < partialParticle) {
			emitParticle(systemCenter);
		}
	}
	
	private void emitParticle(Vector3f center) {
		float dirX = (float) Math.random() * 2f - 1f;
		float dirZ = (float) Math.random() * 2f - 1f;
		Vector3f velocity = new Vector3f(dirX, 1, dirZ);
		velocity.normalize();
		velocity.mul(speed);
		new Particle(new Vector3f(center), velocity, gravityComplient, lifeLength, 0, 1);
	}
	
}
