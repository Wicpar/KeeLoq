package com.wicpar.sinking_simulator.utils;

import com.wicpar.sinking_simulator.engine.opencl.core.CLContext;
import com.wicpar.sinking_simulator.engine.opencl.core.CLDevice;
import com.wicpar.sinking_simulator.engine.opencl.program.CLKernel;

import java.util.HashMap;

/**
 * Created by Frederic on 07/12/2016.
 */
public class CLProgramHandler {

	private static final String basePath = "src/com/wicpar/sinking_simulator/clprograms";
	private final HashMap<String, CLKernel> programs = new HashMap<>();
	private final CLContext context;
	private final CLDevice device;

	public CLProgramHandler(final CLContext context, final CLDevice device) {
		this.context = context;
		this.device = device;
	}

	public CLKernel getProgram(final String name) {
		return getProgram(name, "");
	}

	public CLKernel getProgram(final String name, final String options) {
		final CLKernel toRet = programs.get(name);
		if (programs.get(name) != null)
			return toRet;
		final CLKernel kernel = new CLKernel(device, context, basePath + "/" + name + ".cl", name);
		programs.put(name, kernel);
		return kernel;
	}

	public CLContext getContext() {
		return context;
	}

	public CLDevice getDevice() {
		return device;
	}
}
