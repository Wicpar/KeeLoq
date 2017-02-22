package com.wicpar.sinking_simulator.engine.core.scenes;

import com.wicpar.sinking_simulator.engine.glfw.Window;
import com.wicpar.sinking_simulator.engine.graphics.IDrawable;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Frederic on 13/01/2017.
 */
public abstract class DrawingScene extends Scene {

	private final List<IDrawable> drawables = new LinkedList<>();

	public DrawingScene(final Window window) {
		super(window);
	}

	public void addComponent(final Object o) {
		if (o != null) {
			if (o instanceof IDrawable) {
				drawables.add((IDrawable) o);
			}
		}
	}

	public void removeComponent(final Object o) {
		if (o != null) {
			if (o instanceof IDrawable)
				drawables.remove(o);
		}
	}

	@Override
	public boolean loop() {
		final boolean shouldClose = super.loop();
		if(!shouldClose) {
			drawables.forEach(IDrawable::render);
		}
		return shouldClose;
	}
}
