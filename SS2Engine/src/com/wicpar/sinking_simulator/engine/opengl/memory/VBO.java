package com.wicpar.sinking_simulator.engine.opengl.memory;

import com.wicpar.sinking_simulator.engine.opengl.structure.GLBuffer;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;

import java.nio.*;

/**
 * Created by Frederic on 11/03/2016 at 03:25.
 */
public class VBO extends GLBuffer
{
	protected final int type;
	protected final int mode;
	protected final int target;

	public VBO(final int target, final Buffer buffer, final int mode)
	{
		super(GL15.glGenBuffers());
		this.mode = mode;
		this.target = target;
		bind();
		if (buffer instanceof ByteBuffer)
		{
			GL15.glBufferData(target, (ByteBuffer) buffer, mode);
			type = GL11.GL_BYTE;
		} else if (buffer instanceof ShortBuffer)
		{
			GL15.glBufferData(target, (ShortBuffer) buffer, mode);
			type = GL11.GL_SHORT;
		} else if (buffer instanceof IntBuffer)
		{
			GL15.glBufferData(target, (IntBuffer) buffer, mode);
			type = GL11.GL_INT;
		} else if (buffer instanceof FloatBuffer)
		{
			GL15.glBufferData(target, (FloatBuffer) buffer, mode);
			type = GL11.GL_FLOAT;
		} else
		{
			GL15.glBufferData(target, (DoubleBuffer) buffer, mode);
			type = GL11.GL_DOUBLE;
		}
		unbind();
	}

	public void bind()
	{
		GL15.glBindBuffer(target, id);
	}

	public void unbind()
	{
		GL15.glBindBuffer(target, 0);
	}

	public int getType()
	{
		return type;
	}

	public int getMode()
	{
		return mode;
	}

	@Override
	public void destroy() {
		GL15.glDeleteBuffers(id);
	}
}
