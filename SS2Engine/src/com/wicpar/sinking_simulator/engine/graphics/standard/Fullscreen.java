package com.wicpar.sinking_simulator.engine.graphics.standard;

import com.wicpar.sinking_simulator.engine.graphics.TexturedModel;
import com.wicpar.sinking_simulator.engine.opengl.shaders.ShaderProgram;
import com.wicpar.sinking_simulator.engine.utils.MemUtil;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

/**
 * Created by Frederic on 01/12/2016.
 */
public class Fullscreen extends TexturedModel {

	private static final int[] indices = new int[]{
			0, 1, 2,
			0, 2, 3
	};

	private static final float[] vertices = new float[]{
			-1, 1,
			-1, -1,
			1, -1,
			1, 1
	};

	private static final float[] textureCoords = new float[]{
			0, 1,
			0, 0,
			1, 0,
			1, 1
	};

	private static final IntBuffer inds = MemUtil.wrapIntBuffer(indices);
	private static final FloatBuffer verts = MemUtil.wrapFloatBuffer(vertices);
	private static final FloatBuffer uvs = MemUtil.wrapFloatBuffer(textureCoords);


	public Fullscreen(final ShaderProgram shader) {
		super(inds, verts, 2, uvs, shader);
	}
}
