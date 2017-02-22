package com.wicpar.sinking_simulator.engine.opengl.textures.framebuffers;

import com.wicpar.sinking_simulator.engine.memory.ADestructible;
import com.wicpar.sinking_simulator.engine.opengl.textures.Texture;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;

/**
 * Created by Frederic on 16/03/2016 at 08:20.
 */
public class FBO extends ADestructible {
	protected final int id;

	public FBO() {
		id = GL30.glGenFramebuffers();
	}


	public void bind() {
		GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, id);
	}

	public void bindTexture(final Texture texture, final int attachement) {
		bind();
		texture.bind();
		texture.setParameter(GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
		texture.setParameter(GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
		texture.unbind();
		texture.bindToFramebuffer(attachement);
		unbind();
	}

	public void unbind() {
		GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, 0);
	}

	@Override
	protected void destroy() {
		GL30.glDeleteFramebuffers(id);
	}
}
