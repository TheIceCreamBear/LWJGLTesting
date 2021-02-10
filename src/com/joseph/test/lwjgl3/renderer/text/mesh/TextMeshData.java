package com.joseph.test.lwjgl3.renderer.text.mesh;

//So this code was provided by the dude doing the tutorial im following (Karl)
//it is almost guranteed to be programmed in a way i would not write it
//so this is a big TODO: fix this for a final version
//this comment will be atop any file that was straight up given for this TUT

/**
 * Stores the vertex data for all the quads on which a text will be rendered.
 * @author Karl
 *
 */
public class TextMeshData {
	
	private float[] vertexPositions;
	private float[] textureCoords;
	
	protected TextMeshData(float[] vertexPositions, float[] textureCoords){
		this.vertexPositions = vertexPositions;
		this.textureCoords = textureCoords;
	}

	public float[] getVertexPositions() {
		return vertexPositions;
	}

	public float[] getTextureCoords() {
		return textureCoords;
	}

	public int getVertexCount() {
		return vertexPositions.length/2;
	}

}
