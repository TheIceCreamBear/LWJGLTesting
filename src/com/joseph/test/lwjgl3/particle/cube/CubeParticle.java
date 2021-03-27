package com.joseph.test.lwjgl3.particle.cube;

import org.joml.Vector3f;

import com.joseph.test.lwjgl3.Main;
import com.joseph.test.lwjgl3.entity.Player;

public class CubeParticle {
	private static final float GRAVITY = 0.5f;
	private static final float LIFE_LENGTH = 5f;
	private static final float SPEED = 4f;
	
	private Vector3f velocity;
	private Vector3f position;
	
	private float lifeLeft = LIFE_LENGTH;
	
	public CubeParticle(Vector3f velocity) {
		this.velocity = velocity;
		this.position = new Vector3f(0.0f, 0.0f, 0.0f);
	}
	
	public boolean update() {
		velocity.y += GRAVITY * Main.delta;
		Vector3f deltaPos = new Vector3f(velocity).mul(Main.delta * SPEED);
		position.add(deltaPos);
		lifeLeft -= Main.delta;
		return lifeLeft > 0;
	}
	
	public Vector3f getPosition() {
		return this.position;
	}
}
