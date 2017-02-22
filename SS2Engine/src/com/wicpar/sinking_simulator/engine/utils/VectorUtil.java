package com.wicpar.sinking_simulator.engine.utils;

import org.joml.Vector2f;
import org.joml.Vector2i;

/**
 * Created by Frederic on 08/12/2016.
 */
public class VectorUtil {
	public static Vector2f toVector2f(final Vector2i vector2i) {
		return new Vector2f(vector2i.x, vector2i.y);
	}
}
