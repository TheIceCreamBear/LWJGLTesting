package com.joseph.test.lwjgl3.renderer.text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.joseph.test.lwjgl3.models.ModelLoader;
import com.joseph.test.lwjgl3.renderer.text.mesh.FontType;
import com.joseph.test.lwjgl3.renderer.text.mesh.GUIText;
import com.joseph.test.lwjgl3.renderer.text.mesh.TextMeshData;

/**
 * Controller of all text, stores it, renders it, does the loading of it too
 * @author Joseph
 *
 */
public class Text {
	private static Map<FontType, List<GUIText>> texts = new HashMap<FontType, List<GUIText>>();
	private static TextRenderer renderer;
	
	/**
	 * init the system
	 */
	public static void init() {
		renderer = new TextRenderer();
	}
	
	/**
	 * render the text
	 */
	public static void render() {
		renderer.render(texts);
	}
	
	/**
	 * load the specific text into memory and what not
	 * @param text
	 */
	public static void loadText(GUIText text) {
		// get the font
		FontType font = text.getFont();
		// use the font to determine the mesh
		TextMeshData data = font.loadText(text);
		// use the mesh to generate the vao
		int vao = ModelLoader.instance.loadToVAO(data.getVertexPositions(), data.getTextureCoords());
		// set that to the text so it knows what it is
		text.setMeshInfo(vao, data.getVertexCount());
		// get all texts matching this font
		List<GUIText> batch = texts.get(font);
		// if there isnt a list make one duh stupi
		if (batch == null) {
			batch = new ArrayList<GUIText>();
			texts.put(font, batch);
		}
		// add
		batch.add(text);
	}
	
	/**
	 * remove the text
	 * @param text
	 */
	public static void removeText(GUIText text) {
		// get all (BAD BAD BAD, doesnt check for null, tho technically that is impossible)
		List<GUIText> batch = texts.get(text.getFont());
		// remove
		batch.remove(text);
		// check for empty
		if (batch.isEmpty()) {
			texts.remove(text.getFont());
		}
	}
	
	public static void cleanUp() {
		renderer.cleanUp();
	}
}
