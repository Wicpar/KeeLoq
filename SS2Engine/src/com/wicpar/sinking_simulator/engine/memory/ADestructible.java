package com.wicpar.sinking_simulator.engine.memory;

import java.util.LinkedList;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by Frederic on 28/11/2016.
 */
public abstract class ADestructible {

	private static final AtomicLong count = new AtomicLong(0);
	private static final LinkedList<ADestructible> toDestroy = new LinkedList<>();

	protected ADestructible() {
		count.incrementAndGet();
	}

	protected abstract void destroy();

	@Override
	protected void finalize() throws Throwable {
		try {
			destroy();
		} catch (final Exception ignored) {
			synchronized (toDestroy) {
				toDestroy.add(this);
			}
		}
		count.decrementAndGet();
	}

	public static boolean hasDestructiblesLeft() {
		return count.get() != 0 && toDestroy.size() == 0;
	}

	public static void destroyFailedInMainThread() {
		synchronized (toDestroy) {
			toDestroy.forEach(aDestructible -> {
				try {
					aDestructible.destroy();
				} catch (final Exception e) {
					e.printStackTrace();
				}
			});
			toDestroy.clear();
		}
	}
}
