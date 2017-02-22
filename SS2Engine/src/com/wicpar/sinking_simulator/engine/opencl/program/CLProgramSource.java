package com.wicpar.sinking_simulator.engine.opencl.program;

import com.wicpar.sinking_simulator.engine.opencl.core.CLContext;
import com.wicpar.sinking_simulator.engine.opencl.structure.CLObject;
import org.lwjgl.BufferUtils;
import org.lwjgl.opencl.CL10;

import java.nio.IntBuffer;

import static com.wicpar.sinking_simulator.engine.opencl.util.InfoUtil.checkCLError;

/**
 * Created by Frederic on 29/11/2016.
 */
public class CLProgramSource extends CLObject {

	private final long id;
	private final String options;

	public CLProgramSource(final CLContext context, final String source, final String options) {
		this.options = options;
		final IntBuffer errcode_ret = BufferUtils.createIntBuffer(1);
		id = CL10.clCreateProgramWithSource(context.getId(), source, errcode_ret);
		checkCLError(errcode_ret);
	}

	public CLProgramSource(final CLContext context, final String source) {
		this(context, source, "");
	}

	public long getId() {
		return id;
	}

	public String getOptions() {
		return options;
	}

	@Override
	protected void destroy() {
		checkCLError(CL10.clReleaseProgram(id));
	}
}
