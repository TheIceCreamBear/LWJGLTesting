package com.joseph.test.lwjgl3.models;

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
	 * actually loads the model, reads everything in, and returns a raw version of it
	 * @param fileName
	 * @param loader
	 * @return
	 */
	public static RawModel loadObjModel(String fileName, ModelLoader loader) {
		// lists of lists not really but lots of lists and null arrays
		List<Vector3f> verticies = new ArrayList<Vector3f>();
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
					verticies.add(new Vector3f(Float.parseFloat(splits[1]), Float.parseFloat(splits[2]), Float.parseFloat(splits[3])));
				} else if (line.startsWith("vt ")) {
					textures.add(new Vector2f(Float.parseFloat(splits[1]), Float.parseFloat(splits[2])));
				} else if (line.startsWith("vn ")) {
					normals.add(new Vector3f(Float.parseFloat(splits[1]), Float.parseFloat(splits[2]), Float.parseFloat(splits[3])));
				} else if (line.startsWith("f ")) {
					break;
				}
			}
			
			// init some arrs
			textureData = new float[verticies.size() * 2];
			normalData = new float[verticies.size() * 3];
			
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
				processVertex(vertex1, indicies, textures, normals, textureData, normalData);
				processVertex(vertex2, indicies, textures, normals, textureData, normalData);
				processVertex(vertex3, indicies, textures, normals, textureData, normalData);
				
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
		
		// more init some arrs
		vertexData = new float[verticies.size() * 3];
		indexData = new int[indicies.size()];
		
		// move vertexes (yes) from list to arr of floating bouys
		int vertex = 0;
		for (Vector3f v : verticies) {
			vertexData[vertex++] = v.x;
			vertexData[vertex++] = v.y;
			vertexData[vertex++] = v.z;
		}
		
		// if only you could easily make an ArrayList<Integer> into int[]
		for (int i = 0; i < indicies.size(); i++) {
			indexData[i] = indicies.get(i);
		}

		// retrun
		return loader.loadToVAO(vertexData, textureData, indexData);
	}
	
	/**
	 * processes a specific vertex of the described input face and stores it in the arrays passed in, kinda bad design
	 * but like how else are you gonna do it when there is legit like 3 times you have to do this i mean you could loop...
	 * @param vertexData
	 * @param indicies
	 * @param textures
	 * @param normals
	 * @param textureData
	 * @param normalData
	 */
	private static void processVertex(String[] vertexData, List<Integer> indicies, List<Vector2f> textures, List<Vector3f> normals, float[] textureData, float[] normalData) {
		// find the current vertex index
		int curVertex = Integer.parseInt(vertexData[0]) - 1;
		// that is now a new index
		indicies.add(curVertex);
		// get current texture UV vec
		Vector2f curTex = textures.get(Integer.parseInt(vertexData[1]) - 1);
		// save it into the texture data
		textureData[curVertex * 2] = curTex.x;
		textureData[curVertex * 2 + 1] = 1 - curTex.y;
		// get current normal vec
		Vector3f curNorm = normals.get(Integer.parseInt(vertexData[2]) - 1);
		// save it into the normal data
		normalData[curVertex * 3] = curNorm.x;
		normalData[curVertex * 3 + 1] = curNorm.y;
		normalData[curVertex * 3 + 2] = curNorm.z;
	}
}
