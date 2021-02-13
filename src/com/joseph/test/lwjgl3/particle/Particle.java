package com.joseph.test.lwjgl3.particle;

import org.joml.Vector3f;

import com.joseph.test.lwjgl3.Main;
import com.joseph.test.lwjgl3.entity.Player;

public class Particle {
	private Vector3f position;
	private Vector3f velocity;
	private float gravityEffect;
	private float lifeLength;
	private float rotation;
	private float scale;
	
	private float aliveTime = 0;
	
	public Particle(Vector3f position, Vector3f velocity, float gravityEffect, float lifeLength, float rotation, float scale) {
		this.position = position;
		this.velocity = velocity;
		this.gravityEffect = gravityEffect;
		this.lifeLength = lifeLength;
		this.rotation = rotation;
		this.scale = scale;
		
		Particles.addParticle(this);
	}
	
	public boolean update() {
		velocity.y += Player.GRAVITY * gravityEffect * Main.delta;
		Vector3f deltaPos = new Vector3f(velocity).mul(Main.delta);
		position.add(deltaPos);
		aliveTime += Main.delta;
		return aliveTime < lifeLength;
	}
	
	public Vector3f getPosition() {
		return this.position;
	}
	
	public float getRotation() {
		return this.rotation;
	}
	
	public float getScale() {
		return this.scale;
	}
}
