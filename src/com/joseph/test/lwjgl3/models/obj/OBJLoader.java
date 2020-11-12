package com.joseph.test.lwjgl3.models.obj;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.joml.Vector2f;
import org.joml.Vector3f;

/**
 * oh fun yay we are loading our own objects yay this is nice yay NOT
 * this is POOPY but it also works (for most objects)
 * @author Joseph
 *
 */
public class OBJLoader {
	/**
	 * actually loads the model, reads everything in, and returns the data of it
	 * @param fileName
	 * @return
	 */
	public static ModelData loadObjModel(String fileName) {
		// lists of lists not really but lots of lists and null arrays
		List<Vertex> verticies = new ArrayList<Vertex>();
		List<Vector2f> textures = new ArrayList<Vector2f>();
		List<Vector3f> normals = new ArrayList<Vector3f>();
		List<Integer> indicies = new ArrayList<Integer>();
		float[] vertexData = null;
		float[] textureData = null;
		float[] normalData = null;
		int[] indexData = null;
		// tryyyyyyyyyyy with RESOURCESSSSSSSSSSSSS great feature
		try (BufferedReader reader = new BufferedReader(new FileReader(new File(fileName)))) {
			String line;
			
			// while yes do yes
			while (true) {
				// read line
				line = reader.readLine();
				// split line
				String[] splits = line.split(" ");
				// each prefix has a different meaning
				if (line.startsWith("v ")) {
					Vector3f vert = new Vector3f(Float.parseFloat(splits[1]), Float.parseFloat(splits[2]), Float.parseFloat(splits[3]));
					verticies.add(new Vertex(verticies.size(), vert));
				} else if (line.startsWith("vt ")) {
					textures.add(new Vector2f(Float.parseFloat(splits[1]), Float.parseFloat(splits[2])));
				} else if (line.startsWith("vn ")) {
					normals.add(new Vector3f(Float.parseFloat(splits[1]), Float.parseFloat(splits[2]), Float.parseFloat(splits[3])));
				} else if (line.startsWith("f ")) {
					break;
				}
			}
			
			// while ! end file more yes
			while (line != null) {
				// if not face SKIPPPPPPPPPPPPPPPPPPPPPPP
				if (!line.startsWith("f ")) {
					line = reader.readLine();
					continue;
				}
				// split line
				String[] splits = line.split(" ");
				// split split of line
				String[] vertex1 = splits[1].split("/");
				// split ception
				String[] vertex2 = splits[2].split("/");
				// you ever see a word too many times and it just seems WRONG to you?
				// me with split right now
				String[] vertex3 = splits[3].split("/");
				
				// bruv, process it, like hello
				processVertex(vertex1, verticies, indicies);
				processVertex(vertex2, verticies, indicies);
				processVertex(vertex3, verticies, indicies);
				
				// read next line
				line = reader.readLine();
			}
		} catch (FileNotFoundException e) {
			// i mean obv, if file not found then cant load that one duh
			System.err.println("Cant load that file bud: " + fileName);
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
		// init some arrs
		vertexData = new float[verticies.size() * 3];
		textureData = new float[verticies.size() * 2];
		normalData = new float[verticies.size() * 3];
		float furthest = convertDataToArrays(verticies, textures, normals, vertexData, textureData, normalData);
		indexData = convertIndicesListToArray(indicies);
		
		// retrun
		return new ModelData(vertexData, textureData, normalData, indexData, furthest);
	}
	
	/**
	 * Processes the current vertex string, adds it to the verticies list and the indicies list
	 * @param vertex
	 * @param vertices
	 * @param indices
	 */
	private static void processVertex(String[] vertex, List<Vertex> vertices, List<Integer> indices) {
		// find the current vertex index
		int curVertex = Integer.parseInt(vertex[0]) - 1;
		// get the vertexAsociated with that index
		Vertex currentVertex = vertices.get(curVertex);
		// get the tex data index that is associated with this index
		int textureIndex = Integer.parseInt(vertex[1]) - 1;
		// get the normal data index that is associated with this index
		int normalIndex = Integer.parseInt(vertex[2]) - 1;
		// if the currentVertex has not been touched, touch it (lol thats kinda sus)
		if (!currentVertex.isSet()) {
			// down
			currentVertex.setTextureIndex(textureIndex);
			// set
			currentVertex.setNormalIndex(normalIndex);
			// hike
			indices.add(curVertex);
		} else {
			// bruh
			dealWithAlreadyProcessedVertex(currentVertex, textureIndex, normalIndex, indices, vertices);
		}
	}
	
	/**
	 * Converts it to an array, simple
	 * @param indices - list to convert
	 * @return an array representation of this list
	 */
	private static int[] convertIndicesListToArray(List<Integer> indices) {
		int[] indicesArray = new int[indices.size()];
		for (int i = 0; i < indicesArray.length; i++) {
			indicesArray[i] = indices.get(i);
		}
		return indicesArray;
	}
	
	/**
	 * Converts the given data lists to their corresponding data arrays, simple
	 * @param vertices - list of verticies
	 * @param textures - list of Vec2fs representing UV data
	 * @param normals - list of Vec3fs representing normal data
	 * @param vertexData - array of vertex data
	 * @param texturesData - array of texture data
	 * @param normalsData - array of normal data
	 * @return the farthest point's distance from the origin of the model
	 */
	private static float convertDataToArrays(List<Vertex> vertices, List<Vector2f> textures, List<Vector3f> normals, float[] vertexData, float[] texturesData, float[] normalsData) {
		float furthestPoint = 0;
		// looooooooooooooooooooooop
		for (int i = 0; i < vertices.size(); i++) {
			// get
			Vertex currentVertex = vertices.get(i);
			// max update paradigm
			if (currentVertex.getLength() > furthestPoint) {
				furthestPoint = currentVertex.getLength();
			}
			// convert data, SIMPle
			Vector3f position = currentVertex.getPosition();
			Vector2f textureCoord = textures.get(currentVertex.getTextureIndex());
			Vector3f normalVector = normals.get(currentVertex.getNormalIndex());
			vertexData[i * 3] = position.x;
			vertexData[i * 3 + 1] = position.y;
			vertexData[i * 3 + 2] = position.z;
			texturesData[i * 2] = textureCoord.x;
			texturesData[i * 2 + 1] = 1 - textureCoord.y;
			normalsData[i * 3] = normalVector.x;
			normalsData[i * 3 + 1] = normalVector.y;
			normalsData[i * 3 + 2] = normalVector.z;
		}
		
		return furthestPoint;
	}
	
	/**
	 * deals with a vertex that has already been processed, 
	 * @param previousVertex
	 * @param newTextureIndex
	 * @param newNormalIndex
	 * @param indices
	 * @param vertices
	 */
	private static void dealWithAlreadyProcessedVertex(Vertex previousVertex, int newTextureIndex, int newNormalIndex, List<Integer> indices, List<Vertex> vertices) {
		// if they are literally the same
		if (previousVertex.hasSameTextureAndNormal(newTextureIndex, newNormalIndex)) {
			// add this index to the indicies list
			indices.add(previousVertex.getIndex());
		} else {
			// get the duplicate vertex
			Vertex anotherVertex = previousVertex.getDuplicateVertex();
			if (anotherVertex != null) {
				// if it exists, go to the next level 
				dealWithAlreadyProcessedVertex(anotherVertex, newTextureIndex, newNormalIndex, indices, vertices);
			} else {
				// if it does not exist, create a new duplicate vertex for that location
				Vertex duplicateVertex = new Vertex(vertices.size(), previousVertex.getPosition());
				duplicateVertex.setTextureIndex(newTextureIndex);
				duplicateVertex.setNormalIndex(newNormalIndex);
				previousVertex.setDuplicateVertex(duplicateVertex);
				vertices.add(duplicateVertex);
				indices.add(duplicateVertex.getIndex());
			}
			
		}
	}
	
	/**
	 * remove unused vertices from the list of verticies
	 * @param vertices - the list to deal with
	 */
	private static void removeUnusedVertices(List<Vertex> vertices) {
		for (Vertex vertex : vertices) {
			if (!vertex.isSet()) {
				vertex.setTextureIndex(0);
				vertex.setNormalIndex(0);
			}
		}
	}
}
