package com.joseph.test.lwjgl3.entity;

import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;

import com.joseph.test.lwjgl3.GLFWHandler;
import com.joseph.test.lwjgl3.models.TexturedModel;

public class Player extends Entity {
	private static final float MOVE_SPEED = 20.0f;
	private static final float TURN_SPEED = 160.0f;
	private static final float GRAVITY = -50;
	private static final float JUMP_POWER = 30;
	
	private static final float TERRAIN_HEIGHT = 0;
	
	private float yVel;
	private boolean inAir;

	public Player(TexturedModel model, Vector3f pos, float rotx, float roty, float rotz, float scale) {
		super(model, pos, rotx, roty, rotz, scale);
	}
	
	public void move(float delta) {
		// rotate the dude
		if (GLFWHandler.keyDown[GLFW.GLFW_KEY_KP_6]) {
			super.increaseRotation(0, delta * -TURN_SPEED, 0);
		}
		if (GLFWHandler.keyDown[GLFW.GLFW_KEY_KP_4]) {
			super.increaseRotation(0, delta * TURN_SPEED, 0);
		}
		
		Vector3f displacement = new Vector3f(0, 0, 0);
		// calculate the frame speed
		float speed = MOVE_SPEED * delta;
		// different action based on key
		if (GLFWHandler.keyDown[GLFW.GLFW_KEY_W]) {
			displacement.x -= speed * Math.sin(Math.toRadians(super.getRoty()));
			displacement.z -= speed * Math.cos(Math.toRadians(super.getRoty()));
		}
		if (GLFWHandler.keyDown[GLFW.GLFW_KEY_S]) {
			displacement.x += speed * Math.sin(Math.toRadians(super.getRoty()));
			displacement.z += speed * Math.cos(Math.toRadians(super.getRoty()));
		}
		if (GLFWHandler.keyDown[GLFW.GLFW_KEY_A]) {
			displacement.x -= speed * Math.cos(Math.toRadians(super.getRoty()));
			displacement.z -= speed * Math.sin(Math.toRadians(super.getRoty()));
		}
		if (GLFWHandler.keyDown[GLFW.GLFW_KEY_D]) {
			displacement.x += speed * Math.cos(Math.toRadians(super.getRoty()));
			displacement.z += speed * Math.sin(Math.toRadians(super.getRoty()));
		}
		if (GLFWHandler.keyDown[GLFW.GLFW_KEY_SPACE] && !inAir) {
			inAir = true;
			yVel = JUMP_POWER;
		}
		// handle gravity
		yVel += GRAVITY * delta;
		displacement.y = yVel * delta;
		
		// move the dude
		super.displace(displacement);
		
		// no below terrain
		if (super.getPos().y < TERRAIN_HEIGHT) {
			inAir = false;
			yVel = 0;
			super.getPos().y = TERRAIN_HEIGHT;
		}
		
	}
	
}
