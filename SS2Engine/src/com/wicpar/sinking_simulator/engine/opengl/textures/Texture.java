package com.wicpar.sinking_simulator.engine.opengl.textures;

import com.wicpar.sinking_simulator.engine.memory.ADestructible;
import com.wicpar.sinking_simulator.engine.utils.FileReader;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.GL32;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

/**
 * Created by Frederic on 12/03/2016 at 02:45.
 */
public class Texture extends ADestructible
{
	protected final int id;
	protected final int target;

	public Texture(final int target) {
		id = GL11.glGenTextures();
		this.target = target;
	}

	public static int getComponentFormat(final int compnum)
	{
		switch (compnum)
		{
			case 1:
				return GL11.GL_LUMINANCE;
			case 2:
				return GL11.GL_LUMINANCE_ALPHA;
			case 3:
				return GL11.GL_RGB;
			case 4:
				return GL11.GL_RGBA;
			default:
				return 0;
		}
	}

	public static Texture loadTexture(final File file, final int internalFormat, final boolean mipmap)
	{
		final IntBuffer width = IntBuffer.allocate(1);
		final IntBuffer height = IntBuffer.allocate(1);
		final IntBuffer elements = IntBuffer.allocate(1);
		ByteBuffer img = null;
		try
		{
			img = FileReader.readImage(file, width, height, elements, 0);
		} catch (final IOException e)
		{
			e.printStackTrace();
		}
		final int format = Texture.getComponentFormat(elements.get(0));
		if (width.get(0) == 1 || height.get(0) == 1)
			return new Texture1D(img, Math.max(width.get(0), height.get(0)), format, internalFormat, GL11.GL_UNSIGNED_BYTE);
		else
			return new Texture2D(img, width.get(0), height.get(0), format, internalFormat, GL11.GL_UNSIGNED_BYTE, mipmap);
	}

	public void bind() {
		GL11.glBindTexture(target, id);
	}

	public void unbind()
	{
		GL11.glBindTexture(target, 0);
	}


	public void bind(final int index)
	{
		GL13.glActiveTexture(GL13.GL_TEXTURE0 + index);
		GL11.glBindTexture(target, id);
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
	}

	public void unbind(final int index)
	{
		GL13.glActiveTexture(GL13.GL_TEXTURE0 + index);
		GL11.glBindTexture(target, 0);
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
	}

	public void setParameter(final int param, final int value)
	{
		bind();
		GL11.glTexParameteri(target, param, value);
		unbind();
	}

	public FloatBuffer download() {
		bind();
		GL11.glReadBuffer(GL11.GL_FRONT);
		final IntBuffer res = BufferUtils.createIntBuffer(4);
		GL11.glGetIntegerv(GL11.GL_VIEWPORT, res);
		final ByteBuffer buffer = BufferUtils.createByteBuffer(res.get(2) * res.get(3) * 4 * 4);
		GL11.glReadPixels(0, 0, res.get(2), res.get(3), GL11.GL_RGBA, GL11.GL_FLOAT, buffer);
		unbind();
		return buffer.asFloatBuffer();
	}

	public void bindToFramebuffer(final int attachement)
	{
		GL32.glFramebufferTexture(GL30.GL_FRAMEBUFFER, attachement, id, 0);
	}

	public int getId() {
		return id;
	}

	public int getTarget() {
		return target;
	}

	@Override
	protected void destroy() {
		GL11.glDeleteTextures(id);
	}
}
