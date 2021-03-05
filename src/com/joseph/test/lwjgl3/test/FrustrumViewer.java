package com.joseph.test.lwjgl3.test;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import org.joml.Matrix4f;
import org.joml.Vector4f;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import com.joseph.test.lwjgl3.models.ModelLoader;
import com.joseph.test.lwjgl3.shaders.ShaderProgram;
import com.joseph.test.lwjgl3.shadows.ShadowBox;

/**
 * Made this a shader program as well as having a vao and what not to minimize bloating of this test code
 * really this stuff is only for testing an idea and trying to figure out why the shadows arent working right
 * @author Joseph
 *
 */
public class FrustrumViewer extends ShaderProgram {
	private static final int NUM_VERTS = 8;
	private static final int NUM_FLOATS_PER = 3;
	private static final FloatBuffer buf = BufferUtils.createFloatBuffer(NUM_VERTS * NUM_FLOATS_PER);
	private static final int[] indicies = {
											0, 1, 2,
											1, 2, 3,
											1, 5, 3,
											3, 5, 7,
											0, 4, 2,
											2, 6, 4,
											4, 5, 6,
											5, 6, 7
										  };
	
	private int vaoID;
	private int vboID;
	private int indiciesVbo;
	private int projectionViewLocation;

	public FrustrumViewer() {
		super("/com/joseph/test/lwjgl3/test/test.vert", "/com/joseph/test/lwjgl3/test/test.frag");
		// create vao and vbo and bind vbo (vao is bound from create vao)
		this.vaoID = ModelLoader.instance.createVAO();
		this.vboID = GL15.glGenBuffers();
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboID);
		// say what kind of data is going to go into this buffer
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, NUM_VERTS * NUM_FLOATS_PER * 4, GL15.GL_DYNAMIC_DRAW);
		// put the buffer into the vertex attrib of the vao
		GL20.glVertexAttribPointer(0, NUM_FLOATS_PER, GL11.GL_FLOAT, false, 0, 0);
		// create and store the indicies in an index buffer
		indiciesVbo = GL15.glGenBuffers();
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, indiciesVbo);
		IntBuffer b = BufferUtils.createIntBuffer(indicies.length);
		b.put(indicies);
		b.flip();
		GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, b, GL15.GL_DYNAMIC_DRAW);
		// unbind
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
		GL30.glBindVertexArray(0);
	}
	
	public void update() {
		float[] data = this.pointsToData(ShadowBox.temp);
		// clear the buffer of the old data
		buf.clear();
		// put the new data in
		buf.put(data);
		// flip the buffer to the other direction to be able to read
		buf.flip();
		// bind the vbo
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboID);
		// put the data in the buffer
		GL15.glBufferSubData(GL15.GL_ARRAY_BUFFER, 0, buf);
		// unbind
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
	}
	
	private float[] pointsToData(Vector4f[] points) {
		// convert the array of vector4f into a float array using only the xyz of the vec4
		float[] data = new float[points.length * 3];
		int pointer = 0;
		for (int i = 0; i < points.length; i++) {
			Vector4f cur = points[i];
			data[pointer++] = cur.x;
			data[pointer++] = cur.y;
			data[pointer++] = cur.z;
		}
		return data;
	}

	@Override
	protected void getUniformLocations() {
		this.projectionViewLocation = super.getUniformLocation("projectionView");
	}

	@Override
	protected void bindAttributes() {
		super.bindAttribute(0, "position");
	}
	
	public void loadProjectionView(Matrix4f projView) {
		super.loadMatrix4(projectionViewLocation, projView);
	}
	
	public int getVaoID() {
		return this.vaoID;
	}
	
	public int getVerts() {
		return indicies.length;
	}
	
	@Override
	public void cleanUp() {
		super.cleanUp();
		GL15.glDeleteBuffers(vboID);
		GL15.glDeleteBuffers(indiciesVbo);
	}
}
