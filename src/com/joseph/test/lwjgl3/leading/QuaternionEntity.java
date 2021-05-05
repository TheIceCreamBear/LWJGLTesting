package com.joseph.test.lwjgl3.leading;

import org.joml.Quaternionf;
import org.joml.Vector3f;
import org.joml.Math;

import com.joseph.test.lwjgl3.entity.Entity;
import com.joseph.test.lwjgl3.models.TexturedModel;

public class QuaternionEntity extends Entity {
	private final static Vector3f forward = new Vector3f(0.0f, 0.0f, 1.0f);
	private final static Vector3f up = new Vector3f(0.0f, 1.0f, 0.0f);
	private Quaternionf rotation;
	
	public QuaternionEntity(TexturedModel model, Vector3f pos, float scale) {
		super(model, pos, scale);
		this.rotation = new Quaternionf();
	}
	
	public void face(Vector3f direction) {
		// kinda dont trust this one
		this.rotation.identity();
		this.rotation.lookAlong(direction, new Vector3f(0, 1, 0));
	}
	
	public void lookAlong(Vector3f direction) {
		this.rotation.identity();
		float dot = forward.dot(direction);
		if (Math.abs(dot - (-1.0f)) < 0.000001f) {
			this.rotation.set(up.x, up.y, up.z, (float) Math.PI);
			return;
		}
		if (Math.abs(dot - (1.0f)) < 0.000001f) {
			return;
		}
		
		float rotAngle = Math.acos(dot);
		Vector3f rotAxis = forward.cross(direction, new Vector3f()).normalize();
		float halfAngle = rotAngle * 0.5f;
		float s = Math.sin(halfAngle);
		this.rotation.set(rotAxis.x * s, rotAxis.y * s, rotAxis.z * s, Math.cos(halfAngle));
	}
	
	public Quaternionf getRotation() {
		return this.rotation;
	}
}
