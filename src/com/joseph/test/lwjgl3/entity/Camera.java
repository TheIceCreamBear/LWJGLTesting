package com.joseph.test.lwjgl3.entity;

import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;

import com.joseph.test.lwjgl3.Main;

/**
 * Una camera, only one camera, and it is a view, and you see it, and it is now a thing that 
 * you can see yes, meaning you can move things around
 * @author Joseph
 */
public class Camera {
	private static final float CAM_SPEED = 0.5f;
	private Vector3f position = new Vector3f(100.0f, 35.0f, 50.0f);
	private float pitch = 10.0f;
	private float yaw;
	private float roll;
	
	public Camera() {}
	
	public void move() {
		
	}
	
	public Vector3f getPosition() {
		return this.position;
	}
	
	public float getPitch() {
		return this.pitch;
	}
	
	public float getYaw() {
		return this.yaw;
	}
	
	public float getRoll() {
		return this.roll;
	}
}
