package com.joseph.test.lwjgl3.entity;

import org.joml.Vector3f;

import com.joseph.test.lwjgl3.models.TexturedModel;

/**
 * le entity class, kinda DONT LIKE THIS AT ALL IT IS HORRIBLE DESIGN
 * but that is again okay for this project cause it is all about learning.
 * also its really simple, it has a position, some rotation, a scale, and a
 * textured model, so no more documentation 4 u
 * 
 * @author Joseph
 */
public class Entity {
	private TexturedModel model;
	private Vector3f pos;
	private float rotx;
	private float roty;
	private float rotz;
	private float scale;
	
	private int texIndex;
	private boolean isLightSource;
	
	public Entity(TexturedModel model, Vector3f pos, float scale) {
		this(model, pos, 0.0f, 0.0f, 0.0f, scale);
	}
	
	public Entity(TexturedModel model, Vector3f pos, float rotx, float roty, float rotz, float scale) {
		this(model, 0, pos, rotx, roty, rotz, scale);
	}
	
	public Entity(TexturedModel model, int index, Vector3f pos, float rotx, float roty, float rotz, float scale) {
		this(model, 0, pos, rotx, roty, rotz, scale, false);
	}
	
	public Entity(TexturedModel model, int index, Vector3f pos, float rotx, float roty, float rotz, float scale, boolean isLightSource) {
		this.model = model;
		this.texIndex = index;
		this.pos = pos;
		this.rotx = rotx;
		this.roty = roty;
		this.rotz = rotz;
		this.scale = scale;
		this.isLightSource = isLightSource;
	}
	
	public float getTextureXOffset() {
		int column = this.texIndex % this.model.getTex().getNumRows();
		return (float) column / (float) this.model.getTex().getNumRows();
	}
	
	public float getTextureYOffset() {
		int row = this.texIndex / this.model.getTex().getNumRows();
		return (float) row / (float) this.model.getTex().getNumRows();
	}
	
	public void increasePosition(float dx, float dy, float dz) {
		this.pos.add(dx, dy, dz);
	}
	
	public void displace(Vector3f displacement) {
		this.pos.add(displacement);
	}
	
	public void increaseRotation(float dx, float dy, float dz) {
		this.rotx += dx;
		this.roty += dy;
		this.rotz += dz;
	}
	
	public TexturedModel getModel() {
		return this.model;
	}
	
	public Vector3f getPos() {
		return this.pos;
	}
	
	public float getRotx() {
		return this.rotx;
	}
	
	public float getRoty() {
		return this.roty;
	}
	
	public float getRotz() {
		return this.rotz;
	}
	
	public float getScale() {
		return this.scale;
	}
	
	public boolean isLightSource() {
		return this.isLightSource;
	}
	
	public void setModel(TexturedModel model) {
		this.model = model;
	}
	
	public void setPos(Vector3f pos) {
		this.pos = pos;
	}
	
	public void setRotx(float rotx) {
		this.rotx = rotx;
	}
	
	public void setRoty(float roty) {
		this.roty = roty;
	}
	
	public void setRotz(float rotz) {
		this.rotz = rotz;
	}
	
	public void setScale(float scale) {
		this.scale = scale;
	}	
}
