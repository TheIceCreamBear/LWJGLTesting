package com.joseph.test.lwjgl3.models;

import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

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
	
	/**
	 * loads an array of vertices into a brand new vao and attrib list
	 * and returns it as a raw model for use in rendering
	 * @param vertecies
	 * @return
	 */
	public RawModel loadToVAO(float[] vertices) {
		// creates the new vao and returns the id
		int vaoID = createVAO();
		// stores the vertex data in the attribute list at index 0 
		dataToAttribList(0, vertices);
		// unbinds the vao because duh
		unbindVAO();
		
		// return the new model with the vao and because the verticies array
		// is always in triangles, divide it by 3
		return new RawModel(vaoID, vertices.length / 3);
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
	private void dataToAttribList(int attribNum, float[] data) {
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
		GL20.glVertexAttribPointer(attribNum, 3, GL11.GL_FLOAT, false, 0, 0);
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
