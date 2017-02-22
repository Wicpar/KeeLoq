package com.wicpar.sinking_simulator.engine.opencl.memory;

import com.wicpar.sinking_simulator.engine.opencl.core.CLContext;
import com.wicpar.sinking_simulator.engine.opencl.structure.CLObject;
import org.lwjgl.BufferUtils;
import org.lwjgl.opencl.CL10;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import static com.wicpar.sinking_simulator.engine.opencl.util.InfoUtil.checkCLError;
import static org.lwjgl.opencl.CL10.CL_MEM_COPY_HOST_PTR;
import static org.lwjgl.opencl.CL10.clCreateBuffer;

/**
 * Created by Frederic on 28/11/2016.
 */
public class CLBuffer extends CLObject {

	protected final long id;

	protected CLBuffer(final long id) {
		this.id = id;
	}

	public CLBuffer(final CLContext context, final long size, final long flags) {
		final IntBuffer errcode_ret = BufferUtils.createIntBuffer(1);
		id = clCreateBuffer(context.getId(), flags, size, errcode_ret);
		checkCLError(errcode_ret);
	}

	public CLBuffer(final CLContext context, final ByteBuffer buffer, final long flags) {
		final IntBuffer errcode_ret = BufferUtils.createIntBuffer(1);
		id = clCreateBuffer(context.getId(), flags | CL_MEM_COPY_HOST_PTR, buffer, errcode_ret);
		checkCLError(errcode_ret);
	}

	public long getId() {
		return id;
	}

	@Override
	protected void destroy() {
		checkCLError(CL10.clReleaseMemObject(id));
	}
}
