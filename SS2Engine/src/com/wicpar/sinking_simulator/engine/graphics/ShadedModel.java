package com.wicpar.sinking_simulator.engine.graphics;

import com.wicpar.sinking_simulator.engine.opengl.shaders.ShaderProgram;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

/**
 * Created by Frederic on 01/12/2016.
 */
public class ShadedModel extends Model {

	private final ShaderProgram shader;

	public ShadedModel(final IntBuffer inds, final FloatBuffer verts, final int componentSize, final int renderStyle, final ShaderProgram shader) {
		super(inds, verts, componentSize, renderStyle);
		this.shader = shader;
	}

	public ShadedModel(final int[] inds, final float[] verts, final int componentSize, final int renderStyle, final ShaderProgram shader) {
		super(inds, verts, componentSize, renderStyle);
		this.shader = shader;
	}

	public ShadedModel(final IntBuffer inds, final FloatBuffer verts, final int componentSize, final ShaderProgram shader) {
		super(inds, verts, componentSize);
		this.shader = shader;
	}

	public ShadedModel(final int[] inds, final float[] verts, final int componentSize, final ShaderProgram shader) {
		super(inds, verts, componentSize);
		this.shader = shader;
	}

	public ShaderProgram getShader() {
		return shader;
	}

	@Override
	public void render() {
		shader.start();
		super.render();
		shader.stop();
	}
}
