package com.wicpar.sinking_simulator.engine.opengl.shaders;

import com.wicpar.sinking_simulator.engine.memory.ADestructible;
import com.wicpar.sinking_simulator.engine.utils.FileReader;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL32;
import org.lwjgl.opengl.GL43;

import java.io.File;

/**
 * Created by Frederic on 11/03/2016 at 02:15.
 */
public class Shader extends ADestructible {
	private final int id;
	private final int type;

	public Shader(final File file) {
		this(FileReader.readFile(file.getPath()), getFileShaderType(file.getName()));
	}

	public Shader(final String shader, final int type) {
		id = GL20.glCreateShader(type);
		this.type = type;
		GL20.glShaderSource(id, shader);
		GL20.glCompileShader(id);
		if (GL20.glGetShaderi(id, GL20.GL_COMPILE_STATUS) == GL11.GL_FALSE) {
			System.out.println(GL20.glGetShaderInfoLog(id, 1024));
			throw new RuntimeException("Failed to create Shader");
		}
	}

	public int getType() {
		return type;
	}

	public void attach(final int programID) {
		GL20.glAttachShader(programID, id);
	}

	public void detach(final int programID) {
		GL20.glDetachShader(programID, id);
	}

	public static int getFileShaderType(final String fileName) {
		if (fileName.endsWith(".frag"))
			return GL20.GL_FRAGMENT_SHADER;
		else if (fileName.endsWith(".vert"))
			return GL20.GL_VERTEX_SHADER;
		else if (fileName.endsWith(".geom"))
			return GL32.GL_GEOMETRY_SHADER;
		else if (fileName.endsWith(".comp"))
			return GL43.GL_COMPUTE_SHADER;
		else return 0;
	}

	@Override
	protected void destroy() {
		GL20.glDeleteShader(id);
	}
}
