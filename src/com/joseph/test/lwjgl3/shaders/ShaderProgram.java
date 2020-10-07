package com.joseph.test.lwjgl3.shaders;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL46;

/**
 * Abstract shader program, it does the shading, and makes things have color yay,
 * allows for sub things to define own attributes and what not, kinda dont like
 * the class design but itll get better, but rn its like the way it is because like
 * i cant be bothered and also it makes it easier to follow the tutorial
 * @author Joseph
 *
 */
public abstract class ShaderProgram {
	private int programID;
	private int vertexShaderID;
	private int fragmentShaderID;
	
	/**
	 * Creates a new shader program thingy with the given like hardcoded string to the 
	 * file that contains the shader and it does all the attaching and everything
	 * @param vertexFile
	 * @param fragmentFile
	 */
	public ShaderProgram(String vertexFile, String fragmentFile) {
		// i think maybe load a vertex shader
		this.vertexShaderID = loadShader(vertexFile, GL20.GL_VERTEX_SHADER);
		// i think maybe load a vertex shader
		this.fragmentShaderID = loadShader(fragmentFile, GL20.GL_FRAGMENT_SHADER);
		// create the program maybe
		this.programID = GL20.glCreateProgram();
		// possibly attach the shader to the program
		GL20.glAttachShader(programID, vertexShaderID);
		// possibly attach the shader to the program
		GL20.glAttachShader(programID, fragmentShaderID);
		// link the program
		GL20.glLinkProgram(programID);
		// this probably validates the program
		GL20.glValidateProgram(programID);
		// call this so that they have to bind their attribs and what not
		bindAttributes();
	}
	
	/**
	 * starts this shader
	 */
	public void start() {
		// start it by doing use with the id of this
		GL20.glUseProgram(this.programID);
	}
	
	/**
	 * stops this shader
	 */
	public void stop() {
		// stop it by doing use on 0
		GL20.glUseProgram(0);
	}
	
	/**
	 * cleans everything up
	 */
	public void cleanUp() {
		// stop the thing first because i mean thats smart yea
		stop();
		// detach the shaders because memory
		GL20.glDetachShader(programID, vertexShaderID);
		GL20.glDetachShader(programID, fragmentShaderID);
		// also delete them too
		GL20.glDeleteShader(vertexShaderID);
		GL20.glDeleteShader(fragmentShaderID);
		// yea just yeet the whole thing out of here thanks
		GL20.glDeleteProgram(programID);
	}
	
	protected abstract void bindAttributes();
	
	/**
	 * kinda a helper type deal where it attaches one singular attribute to
	 * its name in the GLSL thingy and its a vao attribute so yea
	 * @param attribute
	 * @param variableName
	 */
	protected void bindAttribute(int attribute, String variableName) {
		// do the bind thingy
		GL20.glBindAttribLocation(programID, attribute, variableName);
	}
	
	/**
	 * Helper to load a shader from a hard coded file location with the specific type 
	 * idk why this is static but like its not smart, itll return the id of the shader
	 * it makes from them
	 * @param file
	 * @param type
	 * @return
	 */
	private static int loadShader(String file, int type) {
		// builder of strings, what is your wisdom
		StringBuilder shader = new StringBuilder();
		// try with resources, that is what the wisdom is, google this if you dont know what it is
		try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
			// basically read each line and then add it to the builder with \n at the end
			String line = reader.readLine();
			while (line != null) {
				shader.append(line).append("\n");
				line = reader.readLine();
			}
		} catch (IOException e) {
			// print the things
			e.printStackTrace();
		}
		
		// create a shader make an id it is of type type
		int shaderID = GL20.glCreateShader(type);
		// here is the source code for you oh darling OpenGL
		GL20.glShaderSource(shaderID, shader);
		// yes, make it into machine codeeeeeeeeeee
		GL20.glCompileShader(shaderID);
		// if error, print error, then dont
		if (GL20.glGetShaderi(shaderID, GL20.GL_COMPILE_STATUS) == GL11.GL_FALSE) {
			System.err.println(GL20.glGetShaderInfoLog(shaderID));
			System.err.println("Could not compile shader " + file);
			System.exit(-1);
		}
		
		// hello here is the shader id back
		return shaderID;
	}
}