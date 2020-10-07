package com.joseph.test.lwjgl3.textures;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;

import de.matthiasmann.twl.utils.PNGDecoder;

public class TextureLoader {
	private static List<Integer> textures = new ArrayList<Integer>();
	
	/**
	 * Creats a texture from the given PNG only PNG file and like does all
	 * the reading in of it into memory and allows for both RGB and RBGA PNGS
	 * and it might return null if it encounters an error because thats what it does
	 * @param file
	 * @return
	 */
	public static Texture loadTexture(String file) {
		// store the texture object that is going to be created
		Texture tex = null;
		try {
			// we need some way to decode the PNG so like this is it because decoding the PNG 
			// is how we get the data
			PNGDecoder decoder = new PNGDecoder(new FileInputStream(new File(file)));
			
			// only support the RGB because thats the only thing that makes sense
			if (!decoder.isRGB()) {
				throw new IOException("PNG must be RGB formmated to be loaded.");
			}
			
			// store some information that we need, like the width height how many bytes per pix
			// and what the final GL pixel format is so yea store things
			int width = decoder.getWidth();
			int height = decoder.getHeight();
			int perPixel = decoder.hasAlpha() ? 4 : 3;
			int glPixelFormat = decoder.hasAlpha() ? GL11.GL_RGBA : GL11.GL_RGB;
			
			// make a buffer to buffer my buffer of buffers
			ByteBuffer buffer = BufferUtils.createByteBuffer(4 * decoder.getWidth() * decoder.getHeight());
			// used the PNG decoder to decode the complex cypher that is the PNG and read it into the buffer
			decoder.decode(buffer, decoder.getWidth() * perPixel, perPixel == 4 ? PNGDecoder.Format.RGBA : PNGDecoder.Format.RGB);
			
			// make le buf redy for le read
			buffer.flip();
			
			// make a GL texture and bind it
			int id = GL11.glGenTextures();
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, id);
			
			// not sure but might not be needed
			GL11.glPixelStorei(GL11.GL_UNPACK_ALIGNMENT, 1);
			
			// set these filters as linear because that is what we are supposed to do
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
			
			// actually save the information of the textgure into Open GL and save it so it can be used and save it basically
			GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, glPixelFormat, decoder.getWidth(), decoder.getHeight(), 0, glPixelFormat, GL11.GL_UNSIGNED_BYTE, buffer);
			
			// mip map it because why not
			GL30.glGenerateMipmap(GL11.GL_TEXTURE_2D);
			
			// actually create the texture object
			tex = new Texture(id);
			
			// dad it to the list
			textures.add(id);
		} catch (IOException e) {
			// print if bug or error because thats what we do
			e.printStackTrace();
		}
		
		// retrun the tex
		return tex;
	}
	
	/**
	 * Clears out all the memory used and what not just like tells
	 * ogl to delete it all
	 */
	public static void cleanUp() {
		for (int tex : textures) {
			GL11.glDeleteTextures(tex);
		}
	}
}
