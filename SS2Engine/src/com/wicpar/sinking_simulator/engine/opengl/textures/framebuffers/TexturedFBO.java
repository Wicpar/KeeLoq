package com.wicpar.sinking_simulator.engine.opengl.textures.framebuffers;

import com.wicpar.sinking_simulator.engine.opengl.textures.Texture;
import com.wicpar.sinking_simulator.engine.opengl.textures.Texture2D;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;

import java.util.Arrays;

/**
 * Created by Frederic on 24/03/2016 at 12:14.
 */
public class TexturedFBO extends FBO
{

	protected final Texture[] targets;
	protected final Texture depth;
	protected final int width, height;

	public TexturedFBO(final int width, final int height, final Texture depth, final Texture... textures)
	{
		super();
		bind();
		targets = textures;
		this.depth = depth;
		this.width = width;
		this.height = height;
		if (textures.length > 24)
			throw new RuntimeException("cannot bind more than 24 render targets to FBO");
		for (int i = 0; i < textures.length; i++)
			bindTexture(textures[i], GL30.GL_COLOR_ATTACHMENT0 + i);
		if (depth != null)
			bindTexture(depth, GL30.GL_DEPTH_STENCIL_ATTACHMENT);
		unbind();
	}

	public TexturedFBO(final int width, final int height, final int number, final int format, final boolean stencilDepth)
	{
		super();
		this.width = width;
		this.height = height;
		targets = new Texture[number];
		if (number > 24)
			throw new RuntimeException("cannot bind more than 24 render targets to FBO");
		for (int i = 0; i < number; i++)
		{
			targets[i] = new Texture2D(null, width, height, GL11.GL_RGBA, format, GL11.GL_UNSIGNED_INT, false);
			targets[i].setParameter(GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
			targets[i].setParameter(GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
			bindTexture(targets[i], GL30.GL_COLOR_ATTACHMENT0 + i);
		}
		if (stencilDepth)
		{
			depth = new Texture2D(null, width, height, GL11.GL_RGBA, GL30.GL_DEPTH32F_STENCIL8, GL11.GL_UNSIGNED_INT, false);
			depth.setParameter(GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
			depth.setParameter(GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
			bindTexture(depth, GL30.GL_DEPTH_STENCIL_ATTACHMENT);
		} else
		{
			depth = null;
		}
	}

	public Texture[] getTargets()
	{
		return Arrays.copyOf(targets, targets.length);
	}

	public Texture getDepth()
	{
		return depth;
	}

	public int getWidth()
	{
		return width;
	}

	public int getHeight()
	{
		return height;
	}
}
