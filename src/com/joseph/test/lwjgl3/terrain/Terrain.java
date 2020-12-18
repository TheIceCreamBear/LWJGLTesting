package com.joseph.test.lwjgl3.terrain;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.joml.Vector2f;
import org.joml.Vector3f;

import com.joseph.test.lwjgl3.math.MathHelper;
import com.joseph.test.lwjgl3.models.ModelLoader;
import com.joseph.test.lwjgl3.models.RawModel;
import com.joseph.test.lwjgl3.textures.TerrainTexture;
import com.joseph.test.lwjgl3.textures.TerrainTexturePack;

public class Terrain {
	private static final float SIZE = 800;
	private static final float MAX_HEIGHT = 40.0f;
	private static final float MAX_PIXEL_COLOR = 256.0f * 256.0f * 256.0f;
	
	private float x;
	private float z;
	private RawModel model;
	private TerrainTexturePack texPack;
	private TerrainTexture blendMap;
	
	private float[][] heights;
	
	/**
	 * create a new terrain square with like the ability to be a tileable and has a texture and is part of a grid, 
	 * kinda wack tbh but cool
	 * @param gridX
	 * @param gridZ
	 * @param loader
	 * @param texture
	 */
	public Terrain(int gridX, int gridZ, ModelLoader loader, TerrainTexturePack texPack, TerrainTexture blendMap, String heightMap) {
		this.texPack = texPack;
		this.blendMap = blendMap;
		this.x = gridX * SIZE;
		this.z = gridZ * SIZE;
		this.model = this.generateTerrain(loader, heightMap);
	}
	
	/**
	 * generate a flat boring terrain chunk of evenly space terrain tiles 
	 * @param loader
	 * @return
	 */
	private RawModel generateTerrain(ModelLoader loader, String heightMap) {
		BufferedImage img = null;
		try {
			img = ImageIO.read(new File(heightMap));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		// amount of vertices is the number of pixels in the height map
		int VERTEX_COUNT = img.getHeight();
		
		// number of total verticies
		int count = VERTEX_COUNT * VERTEX_COUNT;
		// data
		this.heights = new float[VERTEX_COUNT][VERTEX_COUNT];
		float[] vertices = new float[count * 3];
		float[] normals = new float[count * 3];
		float[] textureCoords = new float[count * 2];
		int[] indices = new int[6 * (VERTEX_COUNT - 1) * (VERTEX_COUNT - 1)];
		
		// loop and create all vertex data
		int vertexPointer = 0;
		for(int i = 0; i < VERTEX_COUNT; i++) {
			for(int j = 0; j < VERTEX_COUNT; j++) {
				float height = getHeight(j, i, img);
				// position in x, y, z. all y are 0
				vertices[vertexPointer * 3] = (float) j / ((float) VERTEX_COUNT - 1) * SIZE;
				vertices[vertexPointer * 3 + 1] = height;
				vertices[vertexPointer * 3 + 2] = (float) i / ((float) VERTEX_COUNT - 1) * SIZE;
				// update heights table
				this.heights[j][i] = height;				
				// normal vector, special calculated
				Vector3f norm = calculateNormal(j, i, img);
				normals[vertexPointer * 3] = norm.x;
				normals[vertexPointer * 3 + 1] = norm.y;
				normals[vertexPointer * 3 + 2] = norm.z;
				// texture uv coords
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
	
	/**
	 * Calculates the normal of the given location based on the height of the points around it
	 * @param x
	 * @param z
	 * @param img
	 * @return
	 */
	private Vector3f calculateNormal(int x, int z, BufferedImage img) {
		// get the goods
		float heightL = getHeight(x - 1, z, img);
		float heightR = getHeight(x + 1, z, img);
		float heightD = getHeight(x, z - 1, img);
		float heightU = getHeight(x, z + 1, img);
		
		// create the Vec based on the differences in the heights and also then normalize that
		Vector3f normal = new Vector3f(heightL - heightR, 2.0f, heightD - heightU);
		return normal.normalize();
	}
	
	/**
	 * Gets the height of the given location as specified by the height map
	 * @param x
	 * @param z
	 * @param img
	 * @return
	 */
	private float getHeight(int x, int z, BufferedImage img) {
		// make bounds wrap, only have to handle the range where it is -1 and img.getHieght because of how this is called
		if (x < 0) {
			x = img.getHeight() - 1;
		}
		if (x >= img.getHeight()) {
			x = 0;
		}
		if (z < 0) {
			z = img.getHeight() - 1;
		}
		if (z >= img.getHeight()) {
			z = 0;
		}
		
		// get the height and scale it into a range of +/- MAX_HEIGHT
		float height = img.getRGB(x, z);
		height += MAX_PIXEL_COLOR / 2f;
		height /= MAX_PIXEL_COLOR / 2f;
		height *= MAX_HEIGHT;
		return height;
	}
	
	/**
	 * gets the height of the terrain at the position in the world
	 * @param worldX
	 * @param worldZ
	 * @return
	 */
	public float getHeightOfTerrain(float worldX, float worldZ) {
		// coord relative to the terrain
		float terrainX = worldX - this.x;
		float terrainZ = worldZ - this.z;
		float gridSquareSize = SIZE / (float) (this.heights.length - 1);
		
		// grid coords
		int gridX = (int) Math.floor(terrainX / gridSquareSize);
		int gridZ = (int) Math.floor(terrainZ / gridSquareSize);
		
		// if out of bounds, return 0
		if (gridX >= this.heights.length - 1 || gridZ >= this.heights.length - 1 || gridX < 0 || gridZ < 0) {
			return 0;
		}
		
		// the coord of the tile
		float xCoord = (terrainX % gridSquareSize) / gridSquareSize;
		float zCoord = (terrainZ % gridSquareSize) / gridSquareSize;
		
		// determine which triangle, calc answer, and return
		if (xCoord <= (1 - zCoord)) {
			// normally i would be the type to one line this code, but itll be TOOOO long
			Vector3f p1 = new Vector3f(0, this.heights[gridX][gridZ], 0);
			Vector3f p2 = new Vector3f(1, this.heights[gridX + 1][gridZ], 0);
			Vector3f p3 = new Vector3f(0, this.heights[gridX][gridZ + 1], 1);
			Vector2f pos = new Vector2f(xCoord, zCoord);
			return MathHelper.barryCentric(p1, p2, p3, pos);
		} else {
			// normally i would be the type to one line this code, but itll be TOOOO long
			Vector3f p1 = new Vector3f(1, this.heights[gridX + 1][gridZ], 0);
			Vector3f p2 = new Vector3f(1, this.heights[gridX + 1][gridZ + 1], 1);
			Vector3f p3 = new Vector3f(0, this.heights[gridX][gridZ + 1], 1);
			Vector2f pos = new Vector2f(xCoord, zCoord);
			return MathHelper.barryCentric(p1, p2, p3, pos);
		}
	}
	
	public RawModel getModel() {
		return this.model;
	}
	
	public TerrainTexturePack getTexPack() {
		return this.texPack;
	}
	
	public TerrainTexture getBlendMap() {
		return this.blendMap;
	}
	
	public float getX() {
		return this.x;
	}
	
	public float getZ() {
		return this.z;
	}
}
