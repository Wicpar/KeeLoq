package com.wicpar.sinking_simulator.engine.graphics;

import com.wicpar.sinking_simulator.engine.opengl.memory.VAO;
import com.wicpar.sinking_simulator.engine.opengl.memory.VBO;
import com.wicpar.sinking_simulator.engine.utils.MemUtil;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

/**
 * Created by Frederic on 01/12/2016.
 */
public class Model implements IDrawable {

	private final VAO vao;
	private final VBO vertices;
	private final VBO indicies;
	private final FloatBuffer verts;
	private final IntBuffer inds;
	private final int renderStyle;
	private final int componentSize;

	public Model(final IntBuffer inds, final FloatBuffer verts, final int componentSize, final int renderStyle) {
		vao = new VAO();
		this.componentSize = componentSize;
		this.renderStyle = renderStyle;
		this.verts = verts;
		this.inds = inds;
		this.vertices = new VBO(GL15.GL_ARRAY_BUFFER, this.verts, GL15.GL_STATIC_DRAW);
		this.indicies = new VBO(GL15.GL_ELEMENT_ARRAY_BUFFER, this.inds, GL15.GL_STATIC_DRAW);
		vao.attachVBO(indicies);
		vao.bindVBOAsAttribute(vertices, 0, componentSize, false);
	}

	public Model(final int[] inds, final float[] verts, final int componentSize, final int renderStyle) {
		this(MemUtil.wrapIntBuffer(inds), MemUtil.wrapFloatBuffer(verts), componentSize, renderStyle);
	}

	public Model(final IntBuffer inds, final FloatBuffer verts, final int componentSize) {
		this(inds, verts, componentSize, GL11.GL_TRIANGLES);
	}


	public Model(final int[] inds, final float[] verts, final int componentSize) {
		this(inds, verts, componentSize, GL11.GL_TRIANGLES);
	}

	public VAO getVao() {
		return vao;
	}

	public VBO getVertices() {
		return vertices;
	}

	public VBO getIndicies() {
		return indicies;
	}

	public FloatBuffer getVerts() {
		return verts;
	}

	public IntBuffer getInds() {
		return inds;
	}

	public int getRenderStyle() {
		return renderStyle;
	}

	public int getComponentSize() {
		return componentSize;
	}

	protected void enableAttributes() {
		GL20.glEnableVertexAttribArray(0);
	}

	protected void disableAttributes() {
		GL20.glDisableVertexAttribArray(0);
	}

	@Override
	public void render() {
		vao.bind();
		enableAttributes();
		GL11.glDrawElements(renderStyle, inds.capacity(), GL11.GL_UNSIGNED_INT, 0);
		disableAttributes();
		vao.unbind();
	}
}
