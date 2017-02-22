package com.wicpar.sinking_simulator.engine.opengl.shaders;

import com.wicpar.sinking_simulator.engine.memory.ADestructible;
import org.joml.Matrix4f;
import org.lwjgl.opengl.GL20;

import java.io.File;

/**
 * Created by Frederic on 11/03/2016 at 22:08.
 */
public class ShaderProgram extends ADestructible {
	private final int id;
	private final Shader[] shaders;

	public ShaderProgram(final File... files) {
		this(fromFiles(files));
	}

	public ShaderProgram(final Shader... shaders) {
		id = GL20.glCreateProgram();
		for (final Shader shader : shaders)
			shader.attach(id);
		this.shaders = shaders;
		GL20.glLinkProgram(id);
		GL20.glValidateProgram(id);
		System.out.println(GL20.glGetProgramInfoLog(id));
	}

	private static Shader[] fromFiles(final File... files) {
		final Shader[] shaders = new Shader[files.length];
		for (int i = 0; i < files.length; i++)
			shaders[i] = new Shader(files[i]);
		return shaders;
	}

	public void start() {
		GL20.glUseProgram(id);
	}

	public void stop() {
		GL20.glUseProgram(0);
	}

	public int getUniformLocation(final String s) {
		return GL20.glGetUniformLocation(id, s);
	}

	public void setArg(final int location, final float... arg) {
		start();
		switch (arg.length) {
			case 1:
				GL20.glUniform1f(location, arg[0]);
				break;
			case 2:
				GL20.glUniform2f(location, arg[0], arg[1]);
				break;
			case 3:
				GL20.glUniform3f(location, arg[0], arg[1], arg[2]);
				break;
			case 4:
				GL20.glUniform4f(location, arg[0], arg[1], arg[2], arg[3]);
				break;
			default:
				throw new RuntimeException("Maximum 4 vector components, " + arg.length + " given.");
		}
		stop();
	}

	public void setArg(final int location, final int... arg) {
		start();
		switch (arg.length) {
			case 1:
				GL20.glUniform1i( location, arg[0]);
				break;
			case 2:
				GL20.glUniform2i( location, arg[0], arg[1]);
				break;
			case 3:
				GL20.glUniform3i( location, arg[0], arg[1], arg[2]);
				break;
			case 4:
				GL20.glUniform4i( location, arg[0], arg[1], arg[2], arg[3]);
				break;
			default:
				throw new RuntimeException("Maximum 4 vector components, " + arg.length + " given.");
		}
		stop();
	}

	public void setArg(final int location, final boolean transposed, final Matrix4f matrix) {
		start();
		GL20.glUniformMatrix4fv(location, transposed, matrix.get(new float[16]));
		stop();
	}

	@Override
	protected void destroy() {
		for (final Shader shader : shaders)
			shader.detach(id);
		GL20.glDeleteProgram(id);
	}
}
