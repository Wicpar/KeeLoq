package com.wicpar.sinking_simulator.engine.graphics;

import com.wicpar.sinking_simulator.engine.opengl.memory.VBO;
import com.wicpar.sinking_simulator.engine.opengl.shaders.ShaderProgram;
import com.wicpar.sinking_simulator.engine.utils.MemUtil;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

/**
 * Created by Frederic on 07/12/2016.
 */
public class TexturedModel extends ShadedModel {

	private final VBO textureCoords;

	public TexturedModel(final IntBuffer inds, final FloatBuffer verts, final int componentSize, final FloatBuffer uvs, final int renderStyle, final ShaderProgram shader) {
		super(inds, verts, componentSize, renderStyle, shader);
		textureCoords = new VBO(GL15.GL_ARRAY_BUFFER, uvs, GL15.GL_STATIC_DRAW);
		getVao().bindVBOAsAttribute(textureCoords, 1, 2, false);
	}

	public TexturedModel(final int[] inds, final float[] verts, final int componentSize, final float[] uvs, final int renderStyle, final ShaderProgram shader) {
		this(MemUtil.wrapIntBuffer(inds), MemUtil.wrapFloatBuffer(verts), componentSize, MemUtil.wrapFloatBuffer(uvs), renderStyle, shader);
	}

	public TexturedModel(final IntBuffer inds, final FloatBuffer verts, final int componentSize, final FloatBuffer uvs, final ShaderProgram shader) {
		this(inds, verts, componentSize, uvs, GL11.GL_TRIANGLES, shader);
	}

	public TexturedModel(final int[] inds, final float[] verts, final int componentSize, final float[] uvs, final ShaderProgram shader) {
		this(inds, verts, componentSize, uvs, GL11.GL_TRIANGLES, shader);
	}

	@Override
	protected void enableAttributes() {
		super.enableAttributes();
		GL20.glEnableVertexAttribArray(1);
	}

	@Override
	protected void disableAttributes() {
		GL20.glDisableVertexAttribArray(1);
		super.disableAttributes();
	}
}
