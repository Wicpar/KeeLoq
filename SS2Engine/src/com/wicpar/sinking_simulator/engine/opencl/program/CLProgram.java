package com.wicpar.sinking_simulator.engine.opencl.program;

import com.wicpar.sinking_simulator.engine.opencl.core.CLContext;
import com.wicpar.sinking_simulator.engine.opencl.core.CLDevice;
import com.wicpar.sinking_simulator.engine.opencl.structure.CLObject;
import com.wicpar.sinking_simulator.engine.utils.FileReader;
import org.lwjgl.BufferUtils;
import org.lwjgl.PointerBuffer;
import org.lwjgl.opencl.CL10;

import java.nio.ByteBuffer;

import static com.wicpar.sinking_simulator.engine.opencl.util.InfoUtil.checkCLError;
import static org.lwjgl.system.MemoryUtil.memUTF8;

/**
 * Created by Frederic on 28/11/2016.
 */
public class CLProgram extends CLObject{

	private final CLDevice device;
	private final CLProgramSource source;

	public CLProgram(final CLDevice device, final CLProgramSource source) {
		this.device = device;
		this.source = source;
		try {
			checkCLError(CL10.clBuildProgram(source.getId(), device.getId(), source.getOptions(), null, 0));
		} catch (final Exception e) {
			final PointerBuffer size = PointerBuffer.allocateDirect(1);
			checkCLError(CL10.clGetProgramBuildInfo(source.getId(), device.getId(), CL10.CL_PROGRAM_BUILD_LOG, (ByteBuffer) null, size));
			final ByteBuffer buffer = BufferUtils.createByteBuffer((int) size.get());
			checkCLError(CL10.clGetProgramBuildInfo(source.getId(), device.getId(), CL10.CL_PROGRAM_BUILD_LOG, buffer, null));
			throw new RuntimeException(memUTF8(buffer));
		}
	}

	public CLProgram(final CLDevice device, final CLContext clContext, final String path) {
		this(device, new CLProgramSource(clContext, FileReader.readFile(path)));
	}


	public CLProgram(final CLDevice device, final CLContext clContext, final String path, final String options) {
		this(device, new CLProgramSource(clContext, FileReader.readFile(path), options));
	}

	public CLDevice getDevice() {
		return device;
	}

	public CLProgramSource getSource() {
		return source;
	}

	public long getId() {
		return source.getId();
	}

	/**
	 * do nothing as this only builds the program
	 */
	@Override
	protected void destroy() {

	}
}
