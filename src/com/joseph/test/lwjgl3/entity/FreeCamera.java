package com.joseph.test.lwjgl3.entity;

import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;

import com.joseph.test.lwjgl3.GLFWHandler;
import com.joseph.test.lwjgl3.Main;

public class FreeCamera extends Camera {
	private static final float CAM_MOVE_SPEED = 100.0f;
	private static final float CAM_TURN_SPEED = 160.0f;

	public FreeCamera(Vector3f pos, float pitch, float yaw, float roll) {
		super(pos, pitch, yaw, roll);
	}
	
	@Override
	public void move() {
		Vector3f displacement = new Vector3f(0, 0, 0);
		float speed = CAM_MOVE_SPEED * Main.delta;
		float turnSpeed = CAM_TURN_SPEED * Main.delta;
		if (GLFWHandler.keyDown[GLFW.GLFW_KEY_W]) {
			displacement.x -= speed * Math.sin(Math.toRadians(this.yaw));
			displacement.z -= speed * Math.cos(Math.toRadians(this.yaw));
		}
		if (GLFWHandler.keyDown[GLFW.GLFW_KEY_S]) {
			displacement.x += speed * Math.sin(Math.toRadians(this.yaw));
			displacement.z += speed * Math.cos(Math.toRadians(this.yaw));
		}
		if (GLFWHandler.keyDown[GLFW.GLFW_KEY_A]) {
			displacement.x += speed * Math.cos(Math.toRadians(this.yaw));
			displacement.z -= speed * Math.sin(Math.toRadians(this.yaw));
		}
		if (GLFWHandler.keyDown[GLFW.GLFW_KEY_D]) {
			displacement.x -= speed * Math.cos(Math.toRadians(this.yaw));
			displacement.z += speed * Math.sin(Math.toRadians(this.yaw));
		}
		if (GLFWHandler.keyDown[GLFW.GLFW_KEY_SPACE]) {
			displacement.y += speed / 2;
		}
		if (GLFWHandler.keyDown[GLFW.GLFW_KEY_LEFT_SHIFT]) {
			displacement.y -= speed / 2;
		}
		if (GLFWHandler.keyDown[GLFW.GLFW_KEY_UP]) {
			this.pitch += turnSpeed;
		}
		if (GLFWHandler.keyDown[GLFW.GLFW_KEY_DOWN]) {
			this.pitch -= turnSpeed;
		}
		if (GLFWHandler.keyDown[GLFW.GLFW_KEY_LEFT]) {
			this.yaw -= turnSpeed;
		}
		if (GLFWHandler.keyDown[GLFW.GLFW_KEY_RIGHT]) {
			this.yaw += turnSpeed;
		}
		this.position.add(displacement);
	}
}
