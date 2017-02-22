package com.wicpar.sinking_simulator.engine.core;

import org.joml.Matrix4f;
import org.joml.Vector2i;
import org.joml.Vector4f;

import java.util.LinkedList;

/**
 * Created by Frederic on 08/12/2016.
 */
public class Camera2D {

	private final LinkedList<CameraCallback> callbacks = new LinkedList<>();
	private final Matrix4f matrix = new Matrix4f();
	private final Vector2i size = new Vector2i();
	private float scale = 1;

	public Camera2D(final int width, final int height){
		size.set(width, height);
		final float xmid = width*0.5f;
		final float ymid = height*0.5f;
		final float scale = 16;
		matrix.ortho2D(-xmid, xmid, -ymid, ymid).scale(scale, scale, 1);
		this.scale *= scale;
	}

	public Camera2D(final Vector2i vector2i){
		this(vector2i.x, vector2i.y);
	}

	private void onUpdate(){
		callbacks.forEach(callback -> callback.invoke(this));
	}

	private final Vector4f rel = new Vector4f();
	public void resize(final Vector2i current) {
		rel.set(-1, 1, 0, 1).mul(matrix.invert());
		matrix.invert();
		final float xratio = (size.x / (float) current.x);
		final float yratio = (size.y / (float) current.y);
		matrix.scaleAround(xratio, yratio, 1, rel.x, rel.y, rel.z);
		size.set(current);
		onUpdate();
	}

	public void translate(final float x, final float y) {
		final float movx = x / scale;
		final float movy = y / scale;
		matrix.translate(movx, movy, 0);
		onUpdate();
	}

	public void scale(final float multiplier, final Vector4f relative) {
		final Vector4f rel = matrix.invert().transform(relative);
		matrix.invert();
		matrix.scaleAround(multiplier, multiplier, 1, rel.x, rel.y, rel.z);
		scale *= multiplier;
		onUpdate();
	}

	public void addCameraCallback(final CameraCallback callback) {
		callbacks.add(callback);
		callback.invoke(this);
	}

	public void removeCameraCallback(final CameraCallback callback) {
		callbacks.remove(callback);
	}

	public Matrix4f getMatrix() {
		return new Matrix4f(matrix);
	}

	public float getScale() {
		return scale;
	}

	public Vector2i getSize() {
		return new Vector2i(size);
	}

	public interface CameraCallback {
		void invoke(Camera2D camera);
	}
}
