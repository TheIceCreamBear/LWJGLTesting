package com.joseph.test.lwjgl3.entity;

import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;

import com.joseph.test.lwjgl3.GLFWHandler;
import com.joseph.test.lwjgl3.Main;
import com.joseph.test.lwjgl3.models.TexturedModel;
import com.joseph.test.lwjgl3.terrain.Terrain;

public class Player extends Entity {
	private static final float MOVE_SPEED = 20.0f;
	private static final float TURN_SPEED = 160.0f;
	private static final float GRAVITY = -50;
	private static final float JUMP_POWER = 30;
	
	private float yVel;
	private boolean inAir;

	public Player(TexturedModel model, Vector3f pos, float rotx, float roty, float rotz, float scale) {
		super(model, pos, rotx, roty, rotz, scale);
	}
	
	public void move(Terrain terrain) {
		// rotate the dude
		if (GLFWHandler.keyDown[GLFW.GLFW_KEY_KP_6]) {
			super.increaseRotation(0, Main.delta * -TURN_SPEED, 0);
		}
		if (GLFWHandler.keyDown[GLFW.GLFW_KEY_KP_4]) {
			super.increaseRotation(0, Main.delta * TURN_SPEED, 0);
		}
		
		Vector3f displacement = new Vector3f(0, 0, 0);
		// calculate the frame speed
		float speed = MOVE_SPEED * Main.delta;
		// different action based on key
		if (GLFWHandler.keyDown[GLFW.GLFW_KEY_W]) {
			float speeed = speed;
			// if they are "sprinting"
			if (GLFWHandler.keyDown[GLFW.GLFW_KEY_LEFT_SHIFT]) {
				speeed *= 2;
			}
			displacement.x += speeed * Math.sin(Math.toRadians(super.getRoty()));
			displacement.z += speeed * Math.cos(Math.toRadians(super.getRoty()));
		}
		if (GLFWHandler.keyDown[GLFW.GLFW_KEY_S]) {
			displacement.x -= speed * Math.sin(Math.toRadians(super.getRoty()));
			displacement.z -= speed * Math.cos(Math.toRadians(super.getRoty()));
		}
		if (GLFWHandler.keyDown[GLFW.GLFW_KEY_A]) {
			displacement.x += speed * Math.cos(Math.toRadians(super.getRoty()));
			displacement.z -= speed * Math.sin(Math.toRadians(super.getRoty()));
		}
		if (GLFWHandler.keyDown[GLFW.GLFW_KEY_D]) {
			displacement.x -= speed * Math.cos(Math.toRadians(super.getRoty()));
			displacement.z += speed * Math.sin(Math.toRadians(super.getRoty()));
		}
		if (GLFWHandler.keyDown[GLFW.GLFW_KEY_SPACE] && !inAir) {
			inAir = true;
			yVel = JUMP_POWER;
		}
		// handle gravity
		yVel += GRAVITY * Main.delta;
		displacement.y = yVel * Main.delta;
		
		// move the dude
		super.displace(displacement);
		
		// no below terrain
		float terrainHeight = terrain.getHeightOfTerrain(super.getPos().x, super.getPos().z);
		if (super.getPos().y < terrainHeight) {
			inAir = false;
			yVel = 0;
			super.getPos().y = terrainHeight;
		}
	}
}
