package com.joseph.test.lwjgl3.grid;

import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;

import com.joseph.test.lwjgl3.models.ModelLoader;
import com.joseph.test.lwjgl3.particle.cube.CubeParticleVao;

public class GridVao {
	private static final int MAX_VERTS = radiusToCount(1000);
	private static final FloatBuffer buffer = BufferUtils.createFloatBuffer(MAX_VERTS * 3);
	
	private int vao;
	private int vbo;
	private int vertCount;
	private int radius;
	
	public GridVao(int vao, int vbo) {
		this.vao = vao;
		this.vbo = vbo;
		this.generateFresh(1, 1);
	}
	
	public void generateFresh(int radius, int distancePerLine) {
		// update radius and vert count
		this.radius = radius;
		this.vertCount = Math.min(radiusToCount(radius), MAX_VERTS);
		float[] vertData = new float[this.vertCount * 3];
		
		// create offset for the second row of verts
		int zIndexOffset = radiusToCount(radius) / 2;

		// generate verticies
		for (int i = -radius, j = 0; i <= radius && j < vertCount; i += distancePerLine, j++) {
			vertData[j * 3] = i;          // x
			vertData[j * 3 + 1] = 0;      // y
			vertData[j * 3 + 2] = radius; // z
			
			vertData[j * 3 + zIndexOffset * 3] = radius; // x
			vertData[j * 3 + 1 + zIndexOffset * 3] = 0;  // y
			vertData[j * 3 + 2 + zIndexOffset * 3] = i;  // z
			
			System.out.printf("vertData[%d]=(%d, %d, %d)\n", j, i, 0, radius);
			System.out.printf("vertData[%d]=(%d, %d, %d)\n", j + zIndexOffset, radius, 0, i);
		}
		ModelLoader.instance.updateVbo(vbo, vertData, buffer);
	}

	public int getVao() {
		return this.vao;
	}
	
	public int getVertCount() {
		return this.vertCount;
	}
	
	public int getRadius() {
		return this.radius;
	}
	
	public static GridVao create() {
		return ModelLoader.instance.createGridVao(MAX_VERTS);
	}
	
	private static int radiusToCount(int radius) {
		// each side has radius * 2 + 1 vetrs, and there are 2 sides
		return ((radius * 2) + 1) * 2;
	}
}
