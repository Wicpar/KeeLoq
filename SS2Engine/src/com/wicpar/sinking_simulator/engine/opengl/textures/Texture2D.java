package com.wicpar.sinking_simulator.engine.opengl.textures;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;

import java.nio.ByteBuffer;

/**
 * Created by Frederic on 12/03/2016 at 02:55.
 */
public class Texture2D extends Texture
{
	private final ByteBuffer img;
	private final int width, height, format, internalFormat;

	public Texture2D(final ByteBuffer image, final int  width, final int height, final int srcFormat, final int internalFormat, final int dataFormat, final boolean mipmap)
	{
		super(GL11.GL_TEXTURE_2D);
		bind();
		img = image;
		this.internalFormat = internalFormat;
		this.width = width;
		this.height = height;
		format = srcFormat;
		GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, internalFormat, width, height, 0, srcFormat, dataFormat, img);
		if (mipmap)
			GL30.glGenerateMipmap(GL11.GL_TEXTURE_2D);
		unbind();
	}

	public ByteBuffer getImg() {
		return img;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public int getFormat() {
		return format;
	}

	public int getInternalFormat() {
		return internalFormat;
	}
}
