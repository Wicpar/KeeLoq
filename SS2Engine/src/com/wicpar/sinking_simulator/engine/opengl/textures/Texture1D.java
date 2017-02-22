package com.wicpar.sinking_simulator.engine.opengl.textures;

import org.lwjgl.opengl.GL11;

import java.nio.ByteBuffer;

/**
 * Created by Frederic on 12/03/2016 at 02:55.
 */
public class Texture1D extends Texture {
	private final ByteBuffer img;

	public Texture1D(final ByteBuffer image, final int size, final int srcFormat, final int internalFormat, final int dataFormat) {
		super(GL11.GL_TEXTURE_1D);
		bind();
		img = image;
		GL11.glTexImage1D(GL11.GL_TEXTURE_1D, 0, internalFormat, size, 0, srcFormat, dataFormat, img);
		unbind();
	}

	public ByteBuffer getImg() {
		return img;
	}
}
