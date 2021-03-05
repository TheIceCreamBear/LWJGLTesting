package com.joseph.test.lwjgl3.textures;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.EXTTextureFilterAnisotropic;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL14;
import org.lwjgl.opengl.GL30;

import com.joseph.test.lwjgl3.particle.ParticleTexture;

import de.matthiasmann.twl.utils.PNGDecoder;

public class TextureLoader {
	private static List<Integer> textures = new ArrayList<Integer>();
	
	// TODO does open GL have an invalid texture default texture number or like does 
	// that need to be created by me, and if it does how should i do that
	
	/**
	 * Creates a texture from the given PNG only PNG file and like does all
	 * the reading in of it into memory and allows for both RGB and RBGA PNGS
	 * and it might return null if it encounters an error because thats what it does
	 * @param file
	 * @return
	 */
	public static Texture loadTexture(String file, float bias, boolean ansiotropic) {
		// store the texture object that is going to be created
		Texture tex = null;
		try {
			// load the texture
			int id = loadTextureInternal(file, bias, ansiotropic);
			
			// actually create the texture object
			tex = new Texture(id);
		} catch (IOException e) {
			// print if bug or error because thats what we do
			e.printStackTrace();
		}
		
		// retrun the tex
		return tex;
	}
	
	/**
	 * Overload for {@link TextureLoader#loadTexture(String, float)} to use the default bias value
	 * of -0.4f. Short hands make life easier
	 * @param file
	 * @return
	 */
	public static Texture loadTexture(String file) {
		return loadTexture(file, 0.0f, true);
	}
	

	/**
	 * Creates a texture for text from the given PNG only PNG file and like does all
	 * the reading in of it into memory and allows for both RGB and RBGA PNGS
	 * and it might return null if it encounters an error because thats what it does
	 * and it uses a LOD bias of 0.0f
	 * @param file
	 * @return
	 */
	public static int loadTextTexture(String file) {
		try {
			// load the texture
			int id = loadTextureInternal(file, 0.0f, false);
			
			// return the id
			return id;
		} catch (IOException e) {
			// print if bug or error because thats what we do
			e.printStackTrace();
			// return -1 if something went weird
			return -1;
		}
	}
	
	/**
	 * Creates a texture from the given PNGs, this texture stores both the regular texture
	 * and the supplied normal map. It is assumed that normal is not null, if you want to load only
	 * one texture file, use {@link TextureLoader#loadTexture(String)} instead. The normal map
	 * will be available throught {@link Texture#normalMapID()}.
	 * @param texture - the png file of the texture
	 * @param normal - the png file of the normal map
	 * @return a new texture
	 * @see TextureLoader#loadTexture(String)
	 * @see Texture#normalMapID()
	 */
	public static Texture loadTextureWithNormal(String texture, String normal) {
		try {
			// load the regular texture
			int tex = loadTextureInternal(texture, 0.0f, true);
			// load the normal map
			int norm = loadTextureInternal(normal, 0.0f, true);
			// create a new texture object
			return new Texture(tex, norm);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			// return a null texture here
			return null;
		}
	}
	
	public static ParticleTexture loadParticleTex(String texture, int numRows) {
		// store the texture object that is going to be created
		ParticleTexture tex = null;
		try {
			// load the texture
			int id = loadTextureInternal(texture, 0.0f, true);
			
			// actually create the texture object
			tex = new ParticleTexture(id, numRows);
		} catch (IOException e) {
			// print if bug or error because thats what we do
			e.printStackTrace();
		}
		
		// retrun the tex
		return tex;
	}

	/**
	 * Load a cube map from the given files, which must be in the order
	 * <ul>
	 * <li>Right Face</li>
	 * <li>Left Face</li>
	 * <li>Top Face</li>
	 * <li>Bottom Face</li>
	 * <li>Back Face</li>
	 * <li>Front Face</li>
	 * </ul>
	 * @param texFiles
	 * @return
	 */
	public static int loadCubeMap(String[] texFiles) {
		// make a GL texture and bind it and specify an active bank
		int texID = GL11.glGenTextures();
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL13.GL_TEXTURE_CUBE_MAP, texID);
		
		// loop through input files
		for (int i = 0; i < texFiles.length; i++) {
			try {
				// decode the png
				TextureData data = decodeTexture(texFiles[i]);
				// and send OGL the data, while specifying that it is the positive X + i face, because they are all in order
				GL11.glTexImage2D(GL13.GL_TEXTURE_CUBE_MAP_POSITIVE_X + i, 0, GL11.GL_RGBA, data.getWidth(), data.getHeight(), 0, data.getGlPixelFormat(), GL11.GL_UNSIGNED_BYTE, data.getBuffer());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		// set these filters as linear because that is what we are supposed to do (see mipMap)
		GL11.glTexParameteri(GL13.GL_TEXTURE_CUBE_MAP, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
		GL11.glTexParameteri(GL13.GL_TEXTURE_CUBE_MAP, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
		
		// try to prevent seams
		GL11.glTexParameteri(GL13.GL_TEXTURE_CUBE_MAP, GL11.GL_TEXTURE_WRAP_S, GL12.GL_CLAMP_TO_EDGE);
		GL11.glTexParameteri(GL13.GL_TEXTURE_CUBE_MAP, GL11.GL_TEXTURE_WRAP_T, GL12.GL_CLAMP_TO_EDGE);
		
		// save the tex id to the list so it will get yeeted into oblivion when we are done
		textures.add(texID);
		
		return texID;
	}

	/**
	 * Actually does the job of loading the texture from the file, and loading it to OpenGL. The specified bias
	 * will be used for the LOD bias
	 * @param file - file to load from
	 * @param bias - bias to use
	 * @return a new opengl texture id
	 * @throws IOException
	 */
	private static int loadTextureInternal(String file, float bias, boolean ansiotropic) throws IOException {
		TextureData texData = decodeTexture(file);
		
		// make a GL texture and bind it
		int id = GL11.glGenTextures();
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, id);
		
		// not sure but might not be needed
		GL11.glPixelStorei(GL11.GL_UNPACK_ALIGNMENT, 1);

		// set these filters as linear because that is what we are supposed to do (see mipMap)
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
		
		// actually save the information of the textgure into Open GL and save it so it can be used and save it basically
		GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, texData.getWidth(), texData.getHeight(), 0, texData.getGlPixelFormat(), GL11.GL_UNSIGNED_BYTE, texData.getBuffer());
		
		// mip map it because why not
		GL30.glGenerateMipmap(GL11.GL_TEXTURE_2D);
		
		// oh wow, so um when i did the image decoding, i forgot that i didnt need the above mipmap line (and infact
		// i never used it anywhere or enabled it LOL) but this line will enable that for this newly generated texture
		// in the furture, this should probably be a type of deal where they do the ting and pass in a bool to signify
		// if they want a mip map
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR_MIPMAP_LINEAR);
		
		// effects the bias of the above mip mapping and how far away it starts to do it and what not, negative means 
		// more detail, positive means less
		GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL14.GL_TEXTURE_LOD_BIAS, bias);
		
		// check if ansiotropic filtering extension is present and requested by caller
		if (ansiotropic && GL.getCapabilities().GL_EXT_texture_filter_anisotropic) {
			// it should almost be assumed that it is enabled, given that it has been available since 1999,
			// but due to weird intellectual property rights asociated with it, so it isnt part of core OGL
			// the bottom of this link is a good explanation of this: https://paroj.github.io/gltut/Texturing/Tut15%20Anisotropy.html
			
			// firstly make sure that there is no LOD bias
			if (bias != 0.0f) {				
				GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL14.GL_TEXTURE_LOD_BIAS, 0.0f);
			}
			
			// set the amount we want and set it on the texture
			float amount = Math.min(4.0f, GL11.glGetFloat(EXTTextureFilterAnisotropic.GL_MAX_TEXTURE_MAX_ANISOTROPY_EXT));
			GL11.glTexParameterf(GL11.GL_TEXTURE_2D, EXTTextureFilterAnisotropic.GL_TEXTURE_MAX_ANISOTROPY_EXT, amount);
		}

		// add it to the list
		textures.add(id);
		
		return id;
	}
	
	/**
	 * reads the texture into a class that stores its data and some meta data like side and format
	 * @param file
	 * @return
	 * @throws IOException
	 */
	private static TextureData decodeTexture(String file) throws IOException {
		// store the input stream so we can close it
		FileInputStream is = new FileInputStream(new File(file));
		
		// we need some way to decode the PNG so like this is it because decoding the PNG 
		// is how we get the data
		PNGDecoder decoder = new PNGDecoder(is);
		
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
		ByteBuffer buffer = BufferUtils.createByteBuffer(perPixel * decoder.getWidth() * decoder.getHeight());
		// used the PNG decoder to decode the complex cypher that is the PNG and read it into the buffer
		decoder.decode(buffer, decoder.getWidth() * perPixel, perPixel == 4 ? PNGDecoder.Format.RGBA : PNGDecoder.Format.RGB);
		
		// close the stream
		is.close();
		
		// make le buf redy for le read
		buffer.flip();
		
		// store the data and return it
		return new TextureData(buffer, width, height, glPixelFormat);
	}
	
	/**
	 * Clears out all the memory used and what not just like tells
	 * ogl to delete it all
	 */
	public static void cleanUp() {
		for (int tex : textures) {
			// yeet
			GL11.glDeleteTextures(tex);
		}
	}
}
