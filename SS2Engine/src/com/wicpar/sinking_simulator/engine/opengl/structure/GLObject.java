package com.wicpar.sinking_simulator.engine.opengl.structure;

import com.wicpar.sinking_simulator.engine.memory.ADestructible;

/**
 * Created by Frederic on 28/11/2016.
 */
public abstract class GLObject extends ADestructible {
	protected final int id;

	protected GLObject(final int id) {
		this.id = id;
	}

	public int getId() {
		return id;
	}
}
