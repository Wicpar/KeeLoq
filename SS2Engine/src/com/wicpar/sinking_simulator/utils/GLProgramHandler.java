package com.wicpar.sinking_simulator.utils;

import com.wicpar.sinking_simulator.engine.opengl.shaders.Shader;
import com.wicpar.sinking_simulator.engine.opengl.shaders.ShaderProgram;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.stream.Collectors;

/**
 * Created by Frederic on 07/12/2016.
 */
public class GLProgramHandler {

	private static final String basePath = "src/com/wicpar/sinking_simulator/glprograms/";
	private static final HashMap<String, Shader> shaders = new HashMap<>();

	public static Shader getShader(final String shader) {
		Shader toRet = shaders.get(shader);
		if (toRet != null)
			return toRet;
		toRet =  new Shader(new File(basePath + shader));
		shaders.put(shader, toRet);
		return toRet;
	}

	public static ShaderProgram getProgram(final String ...names) {
		return new ShaderProgram(Arrays.stream(names).map(GLProgramHandler::getShader).collect(Collectors.toSet()).toArray(new Shader[names.length]));
	}
}
