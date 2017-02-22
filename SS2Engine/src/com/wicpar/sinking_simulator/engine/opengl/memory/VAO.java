package com.wicpar.sinking_simulator.engine.opengl.memory;

import com.wicpar.sinking_simulator.engine.opengl.structure.GLObject;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

/**
 * Created by Frederic on 11/03/2016 at 03:21.
 */
public class VAO extends GLObject {

	public VAO() {
		super(GL30.glGenVertexArrays());
	}

	@Override
	public void destroy() {
		GL30.glDeleteVertexArrays(id);
	}

	public void bind() {
		GL30.glBindVertexArray(id);
	}

	public void unbind() {
		GL30.glBindVertexArray(0);
	}

	public void attachVBO(VBO vbo) {
		bind();
		vbo.bind();
		unbind();
	}

	public void bindVBOAsAttribute(final VBO vbo, final int index, final int componentSize, final boolean normalized) {
		bind();
		vbo.bind();
		GL20.glVertexAttribPointer(index, componentSize, vbo.getType(), normalized, 0, 0);
		vbo.unbind();
		unbind();
	}
}
