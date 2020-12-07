package com.joseph.test.lwjgl3.textures;

import java.nio.ByteBuffer;

public class TextureData {
	private ByteBuffer buffer;
	private int width;
	private int height;
	private int glPixelFormat;
	
	public TextureData(ByteBuffer buffer, int width, int height, int glPixelFormat) {
		this.buffer = buffer;
		this.width = width;
		this.height = height;
		this.glPixelFormat = glPixelFormat;
	}
	
	public ByteBuffer getBuffer() {
		return this.buffer;
	}
	
	public int getHeight() {
		return this.height;
	}
	
	public int getWidth() {
		return this.width;
	}
	
	public int getGlPixelFormat() {
		return this.glPixelFormat;
	}
}
