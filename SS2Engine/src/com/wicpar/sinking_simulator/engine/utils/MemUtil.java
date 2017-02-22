package com.wicpar.sinking_simulator.engine.utils;

import org.lwjgl.BufferUtils;
import org.lwjgl.PointerBuffer;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.system.MemoryUtil.memByteBufferNT1;
import static org.lwjgl.system.MemoryUtil.memUTF8;

/**
 * Created by Frederic on 07/12/2016.
 */
public class MemUtil {

	public static String[] charArrayArrayToStringArray(final int count, final long names) {
		final PointerBuffer buf = PointerBuffer.create(names, count);
		final String[] strings = new String[count];
		for (int i = 0; i < count; i++) {
			strings[i] = memUTF8(memByteBufferNT1(buf.get(i)));
		}
		return strings;
	}
	
	public static FloatBuffer wrapFloatBuffer(final float[] floats) {
		return (FloatBuffer) BufferUtils.createFloatBuffer(floats.length)
		                                .put(floats)
		                                .flip();
	}

	public static IntBuffer wrapIntBuffer(final int[] ints) {
		return (IntBuffer) BufferUtils.createIntBuffer(ints.length)
		                                .put(ints)
		                                .flip();
	}

}
