package com.joseph.test.lwjgl3.particle;

import org.joml.Vector2f;
import org.joml.Vector3f;

import com.joseph.test.lwjgl3.Main;
import com.joseph.test.lwjgl3.entity.Camera;
import com.joseph.test.lwjgl3.entity.Player;

public class Particle {
	private Vector3f position;
	private Vector3f velocity;
	private float gravityEffect;
	private float lifeLength;
	private float rotation;
	private float scale;
	
	private float aliveTime = 0;
	private float distance; // to cam
	
	private ParticleTexture texture;
	private Vector2f curOffset = new Vector2f();
	private Vector2f nextOffset = new Vector2f();
	private float blendFactor;
	
	public Particle(ParticleTexture texture, Vector3f position, Vector3f velocity, float gravityEffect, float lifeLength, float rotation, float scale) {
		this.texture = texture;
		this.position = position;
		this.velocity = velocity;
		this.gravityEffect = gravityEffect;
		this.lifeLength = lifeLength;
		this.rotation = rotation;
		this.scale = scale;
		
		Particles.addParticle(this);
	}
	
	public boolean update(Camera cam) {
		velocity.y += Player.GRAVITY * gravityEffect * Main.delta;
		Vector3f deltaPos = new Vector3f(velocity).mul(Main.delta);
		position.add(deltaPos);
		distance = cam.getPosition().sub(position, new Vector3f()).lengthSquared();
		this.updateTexCoordInfo();
		aliveTime += Main.delta;
		return aliveTime < lifeLength;
	}
	
	private void updateTexCoordInfo() {
		float lifeFactor = aliveTime / lifeLength;
		int stageCount = texture.getNumRows() * texture.getNumRows();
		float atlasProgression = lifeFactor * stageCount;
		int indexCur = (int) atlasProgression; // why floor this? its slower
		int indexNext = indexCur < stageCount - 1 ? indexCur + 1 : indexCur;
		this.blendFactor = atlasProgression % 1;
		this.indexToOffset(curOffset, indexCur);
		this.indexToOffset(nextOffset, indexNext);
	}
	
	private void indexToOffset(Vector2f offset, int index) {
		int column = index % texture.getNumRows();
		int row = index / texture.getNumRows();
		offset.x = (float) column / texture.getNumRows();
		offset.y = (float) row / texture.getNumRows();
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
	
	public ParticleTexture getTexture() {
		return this.texture;
	}
	
	public Vector2f getCurOffset() {
		return this.curOffset;
	}
	
	public Vector2f getNextOffset() {
		return this.nextOffset;
	}
	
	public float getBlendFactor() {
		return this.blendFactor;
	}
	
	public float getDistance() {
		return this.distance;
	}
}
