package com.joseph.test.lwjgl3.terrain;

import com.joseph.test.lwjgl3.models.ModelLoader;
import com.joseph.test.lwjgl3.models.RawModel;
import com.joseph.test.lwjgl3.textures.Texture;

public class Terrain {
	private static final float SIZE = 800;
	private static final int VERTEX_COUNT = 128;
	
	private float x;
	private float z;
	private RawModel model;
	private Texture texture;
	
	/**
	 * create a new terrain square with like the ability to be a tileable and has a texture and is part of a grid, 
	 * kinda wack tbh but cool
	 * @param gridX
	 * @param gridZ
	 * @param loader
	 * @param texture
	 */
	public Terrain(int gridX, int gridZ, ModelLoader loader, Texture texture) {
		this.texture = texture;
		this.x = gridX * SIZE;
		this.z = gridZ * SIZE;
		this.model = this.generateTerrain(loader);
	}
	
	/**
	 * generate a flat boring terrain chunk of evenly space terrain tiles 
	 * @param loader
	 * @return
	 */
	private RawModel generateTerrain(ModelLoader loader) {
		// number of total verticies
		int count = VERTEX_COUNT * VERTEX_COUNT;
		// data
		float[] vertices = new float[count * 3];
		float[] normals = new float[count * 3];
		float[] textureCoords = new float[count * 2];
		int[] indices = new int[6 * (VERTEX_COUNT - 1) * (VERTEX_COUNT - 1)];
		
		// loop and create all vertex data
		int vertexPointer = 0;
		for(int i = 0; i < VERTEX_COUNT; i++) {
			for(int j = 0; j < VERTEX_COUNT; j++) {
				// position in x, y, z. all y are 0
				vertices[vertexPointer * 3] = (float) j / ((float) VERTEX_COUNT - 1) * SIZE;
				vertices[vertexPointer * 3 + 1] = 0;
				vertices[vertexPointer * 3 + 2] = (float) i / ((float) VERTEX_COUNT - 1) * SIZE;
				// normal vector, x, y, z. 1 in y as the vector is pointing up
				normals[vertexPointer * 3] = 0;
				normals[vertexPointer * 3 + 1] = 1;
				normals[vertexPointer * 3 + 2] = 0;
				// texture uv cords
				textureCoords[vertexPointer * 2] = (float) j / ((float) VERTEX_COUNT - 1);
				textureCoords[vertexPointer * 2 + 1] = (float) i / ((float) VERTEX_COUNT - 1);
				// ++
				vertexPointer++;
			}
		}
		
		// setup indices
		int pointer = 0;
		for(int gz = 0; gz < VERTEX_COUNT - 1; gz++) {
			for(int gx = 0; gx < VERTEX_COUNT - 1; gx++) {
				int topLeft = (gz * VERTEX_COUNT) + gx;
				int topRight = topLeft + 1;
				int bottomLeft = ((gz + 1)*VERTEX_COUNT) + gx;
				int bottomRight = bottomLeft + 1;
				indices[pointer++] = topLeft;
				indices[pointer++] = bottomLeft;
				indices[pointer++] = topRight;
				indices[pointer++] = topRight;
				indices[pointer++] = bottomLeft;
				indices[pointer++] = bottomRight;
			}
		}
		
		// load the vertices we just made
		return loader.loadToVAO(vertices, textureCoords, normals, indices);
	}
	
	public RawModel getModel() {
		return this.model;
	}
	
	public Texture getTexture() {
		return this.texture;
	}
	
	public float getX() {
		return this.x;
	}
	
	public float getZ() {
		return this.z;
	}
}
