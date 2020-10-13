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
	private Vector3f position = new Vector3f(0, 0, 0);
	private float pitch;
	private float yaw;
	private float roll;
	
	public Camera() {}
	
	/**
	 * THIS IS JANKY AND NOW HOW IT SHOULD BE THIS IS TEMPORARY WHEN WILL I CHANGE IT
	 * IDFK BUT THIS IS GROSS
	 */
	public void move() {
		if (Main.keyDown[GLFW.GLFW_KEY_W]) {
			position.z -= 0.02f;
		}
		if (Main.keyDown[GLFW.GLFW_KEY_D]) {
			position.x += 0.02f;
		}
		if (Main.keyDown[GLFW.GLFW_KEY_A]) {
			position.x -= 0.02f;
		}
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
