package com.wicpar.sinking_simulator.engine.opencl.core;

import com.wicpar.sinking_simulator.engine.opencl.structure.CLObject;
import org.lwjgl.BufferUtils;
import org.lwjgl.opencl.CL10;

import java.nio.IntBuffer;

import static com.wicpar.sinking_simulator.engine.opencl.util.InfoUtil.checkCLError;
import static org.lwjgl.opencl.CL10.clCreateCommandQueue;

/**
 * Created by Frederic on 28/11/2016.
 */
public class CLCommandQueue extends CLObject {

	private final long id;

	public CLCommandQueue(final CLContext context, final int deviceIndex) {
		final IntBuffer errcode_ret = BufferUtils.createIntBuffer(1);
		id = clCreateCommandQueue(context.getId(), context.getDevices().get(deviceIndex).getId(), 0, errcode_ret);
		checkCLError(errcode_ret);
	}

	public CLCommandQueue(final CLContext context) {
		this(context, 0);
	}

	public long getId() {
		return id;
	}

	@Override
	protected void destroy() {
		checkCLError(CL10.clReleaseCommandQueue(id));
	}
}
