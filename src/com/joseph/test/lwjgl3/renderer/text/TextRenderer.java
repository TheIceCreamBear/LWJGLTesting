package com.joseph.test.lwjgl3.renderer.text;

import java.util.List;
import java.util.Map;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import com.joseph.test.lwjgl3.renderer.text.mesh.FontType;
import com.joseph.test.lwjgl3.renderer.text.mesh.GUIText;

/**
 * Lets render some text yo
 * @author Joseph
 *
 */
public class TextRenderer {
	private TextShader shader;
	
	public TextRenderer() {
		this.shader = new TextShader();
	}
	
	/**
	 * Actuall render the text
	 * @param texts
	 */
	public void render(Map<FontType, List<GUIText>> texts) {
		// prepare to render
		this.prepare();
		// get each font type
		for (FontType type : texts.keySet()) {
			// activate the texture atlas
			GL13.glActiveTexture(GL13.GL_TEXTURE0);
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, type.getTextureAtlas());
			// get each text
			for (GUIText text : texts.get(type)) {
				// render le text
				this.renderText(text);
			}
		}
		// do the post render stuff
		this.postRender();
	}
	
	private void prepare() {
		// enable blending of stuffs
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		// disable this, stuff doesnt need to be determined and what not
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		// start shader
		this.shader.start();
	}
	
	private void renderText(GUIText text) {
		// bind the mesh and enable attribs 1 and 2
		GL30.glBindVertexArray(text.getMesh());
		GL20.glEnableVertexAttribArray(0);
		GL20.glEnableVertexAttribArray(1);
		// load the color and translation
		shader.loadColor(text.getColor());
		shader.loadTranslation(text.getPosition());
		// DRAW CALL
		GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, text.getVertexCount());
		// disable the mesh and attribs 1 and 2
		GL20.glDisableVertexAttribArray(0);
		GL20.glDisableVertexAttribArray(1);
		GL30.glBindVertexArray(0);
	}
	
	private void postRender() {
		// stop shader
		this.shader.stop();
		// flip state back to what is was
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glDisable(GL11.GL_BLEND);
	}
	
	public void cleanUp() {
		this.shader.cleanUp();
	}
}
