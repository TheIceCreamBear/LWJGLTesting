package com.joseph.test.lwjgl3.leading;

import org.joml.Vector3f;

import com.joseph.test.lwjgl3.Main;
import com.joseph.test.lwjgl3.entity.Entity;
import com.joseph.test.lwjgl3.models.TexturedModel;

public class VelocityEntity extends Entity {
	private Vector3f velocity;
	
	public VelocityEntity(TexturedModel model, Vector3f pos, Vector3f velocity, float rotx, float roty, float rotz, float scale) {
		super(model, pos, rotx, roty, rotz, scale);
		this.velocity = velocity;
	}
	
	public void move() {
		Vector3f delta = this.velocity.mul(Main.delta, new Vector3f());
		super.displace(delta);
	}
	
	public Vector3f getVelocity() {
		return this.velocity;
	}
}
