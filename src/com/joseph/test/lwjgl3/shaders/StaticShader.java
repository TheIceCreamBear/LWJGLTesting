package com.joseph.test.lwjgl3.shaders;

/**
 * does this even need docs? meh, okay so this is just a static shader 
 * that statically shades the basic rectangle that is at the tutorial thingy
 * and like yea basic
 * @author Joseph
 *
 */
public class StaticShader extends ShaderProgram {
	private static final String VERTEX_FILE = "src/com/joseph/test/lwjgl3/shaders/vertexShader.vert";
	private static final String FRAGMENT_FILE = "src/com/joseph/test/lwjgl3/shaders/fragmentShader.frag";
	
	public StaticShader() {
		super(VERTEX_FILE, FRAGMENT_FILE);
	}

	@Override
	protected void bindAttributes() {
		super.bindAttribute(0, "position");
	}
}
