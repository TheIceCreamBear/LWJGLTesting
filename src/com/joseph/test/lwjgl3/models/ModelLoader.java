package com.joseph.test.lwjgl3.models;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.GL33;

import com.joseph.test.lwjgl3.models.obj.ModelData;

/**
 * Class to load models up, and saves the vao and vbo IDs associated with them
 * This should honestly probably be static, but LOL the video didnt do that
 * so when this gets redesigned it probably will be static, because why do you 
 * need more than one loader like hello?
 * @author Joseph
 *
 */
public class ModelLoader {
	// lists that contain all of the created ids of the vaos and vbos, for clean up
	private List<Integer> vaos = new ArrayList<Integer>();
	private List<Integer> vbos = new ArrayList<Integer>();
	
	// im tired of this not being static
	public static final ModelLoader instance = new ModelLoader();
	
	private ModelLoader() {
		// so nobody decides its a smart idea to try and construct this
	}
	
	/**
	 * Overload for {@link ModelLoader#loadToVAO(float[], float[], float[], int[])}
	 * @param data
	 * @return
	 */
	public RawModel loadToVAO(ModelData data) {
		return this.loadToVAO(data.getVertices(), data.getTextureCoords(), data.getNormals(), data.getIndices());
	}
	
	/**
	 * loads an array of vertices into a brand new vao and attrib list
	 * and returns it as a raw model for use in rendering
	 * @param vertecies - the vertices of the object
	 * @param textureCoords
	 * @param normals
	 * @param indices - the list of vertices to use when constructing the object
	 * @return
	 */
	public RawModel loadToVAO(float[] vertices, float[] textureCoords, float[] normals, int[] indices) {
		// creates the new vao and returns the id
		int vaoID = createVAO();
		// create the indices buffer and bind it
		bindIndicesBuffer(indices);
		// stores the vertex data in the attribute list at index 0 
		dataToAttribList(0, 3, vertices);
		// stores the texture uv data in the attribute list at index 1
		dataToAttribList(1, 2, textureCoords);
		// store le normal vectorsssssssss in the attribute list at index 2
		dataToAttribList(2, 3, normals);
		// unbinds the vao because duh
		unbindVAO();
		
		// return the new model with the vao and indices length
		return new RawModel(vaoID, indices.length);
	}
	
	/**
	 * 
	 * @param vertices
	 * @param textureCoords
	 * @param normals
	 * @param tangents
	 * @param indices
	 * @return
	 */
	public RawModel loadToVAO(float[] vertices, float[] textureCoords, float[] normals, float[] tangents, int[] indices) {
		// creates the new vao and returns the id
		int vaoID = createVAO();
		// create the indices buffer and bind it
		bindIndicesBuffer(indices);
		// stores the vertex data in the attribute list at index 0 
		dataToAttribList(0, 3, vertices);
		// stores the texture uv data in the attribute list at index 1
		dataToAttribList(1, 2, textureCoords);
		// store le normal vectorsssssssss in the attribute list at index 2
		dataToAttribList(2, 3, normals);
		// store the tangent values in the attribute list at index 3
		dataToAttribList(3, 3, tangents);
		// unbinds the vao because duh
		unbindVAO();
		
		// return the new model with the vao and indices length
		return new RawModel(vaoID, indices.length);
	}
	
	/**
	 * one special boi, responsible for loading one only one ping jk one quad to a vao, its a quad
	 * for gui stuffs
	 * @param positions
	 * @return
	 */
	public RawModel loadToVAO(float[] positions, int dimensions) {
		// creates the new vao and returns the id
		int vaoID = createVAO();
		// stores the position data in the attrib list at index 0
		dataToAttribList(0, dimensions, positions);
		// lol obviously we need to undind the VAO
		unbindVAO();
		
		// return the thing
		return new RawModel(vaoID, positions.length / dimensions);
	}
	
	/**
	 * loads an array of vertices into a brand new vao and attrib list
	 * and returns it as a raw model for use in rendering, used for text rendering
	 * @param vertecies - the vertices of the object
	 * @param textureCoords
	 * @param normals
	 * @param indices - the list of vertices to use when constructing the object
	 * @return
	 * 
	 * TODO this should be named different and take different args, cause this is just, wtf
	 */
	public int loadToVAO(float[] positions, float[] textureCoords) {
		// creates the new vao and returns the id
		int vaoID = createVAO();
		// stores the vertex data in the attribute list at index 0 
		dataToAttribList(0, 2, positions);
		// stores the texture uv data in the attribute list at index 1
		dataToAttribList(1, 2, textureCoords);
		// unbinds the vao because duh
		unbindVAO();
		
		// return the new model with the vao and indices length
		return vaoID;
	}
	
	/**
	 * Creates an empty vbo with float count number of floats, used for instance rendering
	 * @param floatCount
	 * @return
	 */
	public int createEmptyVBO(int floatCount) {
		// create vbo
		int vbo = GL15.glGenBuffers();
		// add to list
		vbos.add(vbo);
		// bind it
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vbo);
		// set the data limit and that it is going to be stream draw
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, floatCount * 4, GL15.GL_STREAM_DRAW);
		// unbind
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
		return vbo;
	}
	
	/**
	 * adds to the vao the vbo data, into the specified attribute, with size number of values stored per
	 * unit, and with data length stride and offset offset
	 * @param vao
	 * @param vbo
	 * @param attribute
	 * @param size
	 * @param dataLength
	 * @param offset
	 */
	public void addInstancedAttribute(int vao, int vbo, int attribute, int size, int dataLength, int offset) {
		// bind the buffer
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vbo);
		// bind the vao
		GL30.glBindVertexArray(vao);
		// set the attribute into the list with the size, datalength stride, and offset
		GL20.glVertexAttribPointer(attribute, size, GL11.GL_FLOAT, false, dataLength * 4, offset * 4);
		// say that it is instanced data
		GL33.glVertexAttribDivisor(attribute, 1);
		// unbind
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
		// unbind
		GL30.glBindVertexArray(0);
	}
	
	/**
	 * update the data in the vbo with the given data array, and reuse the given float buffer
	 * @param vbo
	 * @param data
	 * @param buffer
	 */
	public void updateVbo(int vbo, float[] data, FloatBuffer buffer) {
		// clear the buffer of the old data
		buffer.clear();
		// put the new data in
		buffer.put(data);
		// flip the buffer to the other direction to be able to read
		buffer.flip();
		// bind the vbo
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vbo);
		// say what type of data goes in it and how much
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, buffer.capacity() * 4, GL15.GL_STREAM_DRAW);
		// put the data in the buffer
		GL15.glBufferSubData(GL15.GL_ARRAY_BUFFER, 0, buffer);
		// unbind
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
	}
	
	/**
	 * kinda self explanatory, but like all this does is delets the 
	 * created vaos and vbos because like memory management
	 */
	public void cleanUp() {
		// for each vao delete it
		for (int i: vaos) {
			GL30.glDeleteVertexArrays(i);
		}
		// for each vbo delete it
		for (int i: vbos) {
			GL15.glDeleteBuffers(i);
		}
	}
	
	/**
	 * kinda simple, but creates a new vao and returns its id, also saves it to the list
	 * and binds it for use
	 * @return the id of the created vao
	 */
	private int createVAO() {
		// create vao and get the id
		int vaoID = GL30.glGenVertexArrays();
		// add id to the list
		vaos.add(vaoID);
		// bind it for use (duh)
		GL30.glBindVertexArray(vaoID);
		// JAVA! its 4pm, time for you to return the vaoID created by this method
		return vaoID;
		// yes honey
	}
	
	/**
	 * okay so this just puts the data passed in into the specified attribute index of 
	 * the bound VAO because yes
	 * @param attribNum - the index of the desired destination for the data
	 * @param data - the data
	 */
	private void dataToAttribList(int attribNum, int attribSize, float[] data) {
		// create the vbo because like we need it hello?
		int vboID = GL15.glGenBuffers();
		// add to list
		vbos.add(vboID);
		// bind the buffer to put data in because theres no way to modify an int
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboID);
		// create a buffer so that info can be stored because thats how it happens
		FloatBuffer fb = dataToFloatBuffer(data);
		// okay this like puys the stuff into the array buffer and like labes it as a static draw
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, fb, GL15.GL_STATIC_DRAW);
		// yea no idea what this does
		// OHHH wait okay so it says that the vbo at attribNum is like not normalized and not anything 
		// else but its also a float and it 
		GL20.glVertexAttribPointer(attribNum, attribSize, GL11.GL_FLOAT, false, 0, 0);
		// yea youre typical unbinding of something for open gl
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
	}
	
	/**
	 * DOES THIS NEED TO BE HERE???? HELLOOOOO???
	 * like, why cant you just type this out every time...
	 */
	private void unbindVAO() {
		GL30.glBindVertexArray(0);
	}
	
	/**
	 * idk kinda doing a thing here where new vbo to hold the indices because thats less memory
	 * than using a float array
	 * @param indices
	 */
	private void bindIndicesBuffer(int[] indices) {
		// create the vbo because like we still need one hello?
		int vboID = GL15.glGenBuffers();
		// add to list because yes
		vbos.add(vboID);
		// binding of the buffer, but it is a different type of data
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, vboID);
		// CREATE A BUFFER
		IntBuffer buffer = dataToIntBuffer(indices);
		// store the buffer
		GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
	}
	
	/**
	 * simple, just creates an int buffer to read the info from
	 * @param data - the data
	 * @return the buffer
	 */
	private IntBuffer dataToIntBuffer(int[] data) {
		IntBuffer b = BufferUtils.createIntBuffer(data.length);
		b.put(data);
		b.flip();
		return b;
	}
	
	/**
	 * simple, just creates a float buffer to read the info from
	 * @param data - the data
	 * @return the buffer
	 */
	private FloatBuffer dataToFloatBuffer(float[] data) {
		FloatBuffer b = BufferUtils.createFloatBuffer(data.length);
		b.put(data);
		b.flip();
		return b;
	}
}
