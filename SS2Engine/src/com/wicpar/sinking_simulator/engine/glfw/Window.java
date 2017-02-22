package com.wicpar.sinking_simulator.engine.glfw;

import com.wicpar.sinking_simulator.engine.glfw.callbacks.*;
import com.wicpar.sinking_simulator.engine.glfw.enums.*;
import com.wicpar.sinking_simulator.engine.utils.MemUtil;
import org.joml.Vector2d;
import org.joml.Vector2i;
import org.joml.Vector4i;
import org.lwjgl.glfw.GLFWImage;
import org.lwjgl.glfw.GLFWVidMode;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;

/**
 * Created by Frederic on 03/12/2016.
 */
public class Window {

	private static WeakReference<Window> currentContext = new WeakReference<>(null);

	private final long id;
	private final List<WindowPosCallback> windowPosCallbacks = new ArrayList<>();
	private final List<WindowSizeCallback> windowSizeCallbacks = new ArrayList<>();
	private final List<WindowCloseCallback> windowCloseCallbacks = new ArrayList<>();
	private final List<WindowRefreshCallback> windowRefreshCallbacks = new ArrayList<>();
	private final List<WindowFocusCallback> windowFocusCallbacks = new ArrayList<>();
	private final List<WindowIconifyCallback> windowIconifyCallbacks = new ArrayList<>();
	private final List<FramebufferSizeCallback> windowFramebufferSizeCallbacks = new ArrayList<>();
	private final List<KeyCallback> windowKeyCallbacks = new ArrayList<>();
	private final List<CharCallback> windowCharCallbacks = new ArrayList<>();
	private final List<CharModsCallback> windowCharModsCallbacks = new ArrayList<>();
	private final List<MouseButtonCallback> windowMouseButtonCallbacks = new ArrayList<>();
	private final List<CursorPosCallback> windowCursorPosCallbacks = new ArrayList<>();
	private final List<CursorEnterCallback> windowCursorEnterCallbacks = new ArrayList<>();
	private final List<ScrollCallback> windowScrollCallbacks = new ArrayList<>();
	private final List<DropCallback> windowDropCallbacks = new ArrayList<>();
	private final GLFW glfw;
	private boolean destroyed = false;
	private final int refreshRate;

	public static void setWindowHints(final Boolean resizeable, final Boolean visible, final Boolean decorated,
			final Boolean focused, final Boolean autoIconify, final Boolean topMost, final Boolean maximized) {

		if (resizeable != null) glfwWindowHint(GLFW_RESIZABLE, resizeable ? GLFW_TRUE : GLFW_FALSE);
		if (visible != null) glfwWindowHint(GLFW_VISIBLE, visible ? GLFW_TRUE : GLFW_FALSE);
		if (decorated != null) glfwWindowHint(GLFW_DECORATED, decorated ? GLFW_TRUE : GLFW_FALSE);
		if (focused != null) glfwWindowHint(GLFW_FOCUSED, focused ? GLFW_TRUE : GLFW_FALSE);
		if (autoIconify != null) glfwWindowHint(GLFW_AUTO_ICONIFY, autoIconify ? GLFW_TRUE : GLFW_FALSE);
		if (topMost != null) glfwWindowHint(GLFW_FLOATING, topMost ? GLFW_TRUE : GLFW_FALSE);
		if (maximized != null) glfwWindowHint(GLFW_MAXIMIZED, maximized ? GLFW_TRUE : GLFW_FALSE);
	}

	public static void setFrameBufferHints(final Integer redbits, final Integer greenbits, final Integer bluebits,
			final Integer alphabits, final Integer depthbits, final Integer stencilbits, final Boolean stereo,
			final Integer samples, final Boolean srgb, final Boolean doubleBuffer) {

		if (redbits != null) glfwWindowHint(GLFW_RED_BITS, redbits);
		if (greenbits != null) glfwWindowHint(GLFW_GREEN_BITS, greenbits);
		if (bluebits != null) glfwWindowHint(GLFW_BLUE_BITS, bluebits);
		if (alphabits != null) glfwWindowHint(GLFW_ALPHA_BITS, alphabits);
		if (depthbits != null) glfwWindowHint(GLFW_DEPTH_BITS, depthbits);
		if (stencilbits != null) glfwWindowHint(GLFW_STENCIL_BITS, stencilbits);
		if (stereo != null) glfwWindowHint(GLFW_STEREO, stereo ? GLFW_TRUE : GLFW_FALSE);
		if (samples != null) glfwWindowHint(GLFW_SAMPLES, samples);
		if (srgb != null) glfwWindowHint(GLFW_SRGB_CAPABLE, srgb ? GLFW_TRUE : GLFW_FALSE);
		if (doubleBuffer != null) glfwWindowHint(GLFW_DOUBLEBUFFER, doubleBuffer ? GLFW_TRUE : GLFW_FALSE);
	}

	public static void setFrameBufferHints(final GLFWVidMode vidMode, final Integer alphabits,
			final Integer depthbits, final Integer stencilbits, final Boolean stereo, final Integer samples,
			final Boolean srgb, final Boolean doubleBuffer) {

		if (vidMode != null) {
			setFrameBufferHints(vidMode.redBits(), vidMode.greenBits(), vidMode.blueBits(), alphabits, depthbits,
			                    stencilbits, stereo, samples, srgb, doubleBuffer);
		} else {
			setFrameBufferHints(null, null, null, alphabits, depthbits,
			                    stencilbits, stereo, samples, srgb, doubleBuffer);
		}
	}


	public static void setMonitorHints(final Integer refreshRate) {

		if (refreshRate != null) glfwWindowHint(GLFW_REFRESH_RATE, refreshRate);
	}

	public static void setMonitorHints(final GLFWVidMode vidMode) {

		setMonitorHints(vidMode != null ? vidMode.refreshRate() : null);
	}

	public static void setContextHints(final ClientApi api, final ContextCreationApi creationApi,
			final Integer versionMajor, final Integer versionMinor, final Boolean forwardCompat, final Boolean debug,
			final OpenglProfile openglProfile, final ContextRobustness contextRobustness,
			final ReleaseBehavior releaseBehavior, final Boolean noErrors) {

		if (api != null) glfwWindowHint(GLFW_CLIENT_API, api.getId());
		if (creationApi != null) glfwWindowHint(GLFW_CONTEXT_CREATION_API, creationApi.getId());
		if (versionMajor != null) glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, versionMajor);
		if (versionMinor != null) glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, versionMinor);
		if (forwardCompat != null) glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, forwardCompat ? GLFW_TRUE : GLFW_FALSE);
		if (debug != null) glfwWindowHint(GLFW_OPENGL_DEBUG_CONTEXT, debug ? GLFW_TRUE : GLFW_FALSE);
		if (openglProfile != null) glfwWindowHint(GLFW_OPENGL_PROFILE, openglProfile.getId());
		if (contextRobustness != null) glfwWindowHint(GLFW_CONTEXT_ROBUSTNESS, contextRobustness.getId());
		if (releaseBehavior != null) glfwWindowHint(GLFW_CONTEXT_RELEASE_BEHAVIOR, releaseBehavior.getId());
		if (noErrors != null) glfwWindowHint(GLFW_CONTEXT_NO_ERROR, noErrors ? GLFW_TRUE : GLFW_FALSE);
	}

	public void useContext() {
		if (currentContext.get() != this) {
			currentContext = new WeakReference<>(this);
			glfwMakeContextCurrent(id);
		}
	}

	public Window(final GLFW glfw, final int width, final int height, final String title, final Monitor monitor, final Window share) {
		this.glfw = glfw;
		if (monitor == null) {
			refreshRate = 60;
			setMonitorHints(refreshRate);
			id = glfwCreateWindow(width, height, title, 0, share == null ? 0 : share.id);
		} else {
			refreshRate = monitor.getCurrentVidMode()
			                     .refreshRate();
			id = glfwCreateWindow(width, height, title, monitor.getId(), share == null ? 0 : share.id);
		}
		glfwSetWindowPosCallback(id, (window, xpos, ypos) -> windowPosCallbacks.forEach(callback -> callback.invoke(this, xpos, ypos)));
		glfwSetWindowSizeCallback(id, (window, xsize, ysize) -> windowSizeCallbacks.forEach(callback -> callback.invoke(this, xsize, ysize)));
		glfwSetWindowRefreshCallback(id, (window) -> windowRefreshCallbacks.forEach(callback -> callback.invoke(this)));
		glfwSetWindowCloseCallback(id, (window) -> windowCloseCallbacks.forEach(callback -> callback.invoke(this)));
		glfwSetWindowFocusCallback(id, (window, focused) -> windowFocusCallbacks.forEach(callback -> callback.invoke(this, focused)));
		glfwSetWindowIconifyCallback(id, (window, iconified) -> windowIconifyCallbacks.forEach(callback -> callback.invoke(this, iconified)));
		glfwSetFramebufferSizeCallback(id, (window, xsize, ysize) -> windowFramebufferSizeCallbacks.forEach(callback -> callback.invoke(this, xsize, ysize)));
		glfwSetKeyCallback(id, (window, key, scancode, action, mods) -> windowKeyCallbacks.forEach(callback -> callback.invoke(this, key, scancode, action, mods)));
		glfwSetCharCallback(id, (window, codepoint) -> windowCharCallbacks.forEach(callback -> callback.invoke(this, codepoint)));
		glfwSetCharModsCallback(id, (window, codepoint, mods) -> windowCharModsCallbacks.forEach(callback -> callback.invoke(this, codepoint, mods)));
		glfwSetMouseButtonCallback(id, (window, button, action, mods) -> windowMouseButtonCallbacks.forEach(callback -> callback.invoke(this, button, action, mods)));
		glfwSetCursorPosCallback(id, (window, xpos, ypos) -> windowCursorPosCallbacks.forEach(callback -> callback.invoke(this, xpos, ypos)));
		glfwSetCursorEnterCallback(id, (window, entered) -> windowCursorEnterCallbacks.forEach(callback -> callback.invoke(this, entered)));
		glfwSetScrollCallback(id, (window, xoffset, yoffset) -> windowScrollCallbacks.forEach(callback -> callback.invoke(this, xoffset, yoffset)));
		glfwSetDropCallback(id, (window, count, names) -> windowDropCallbacks.forEach(callback -> callback.invoke(this, MemUtil.charArrayArrayToStringArray(count, names))));
		glfwDefaultWindowHints();
	}

	public boolean addPosCallback(final WindowPosCallback posCallback) {
		return windowPosCallbacks.add(posCallback);
	}

	public boolean addSizeCallback(final WindowSizeCallback windowSizeCallback) {
		return windowSizeCallbacks.add(windowSizeCallback);
	}

	public boolean addRefreshCallback(final WindowRefreshCallback windowRefreshCallback) {
		return windowRefreshCallbacks.add(windowRefreshCallback);
	}

	public boolean addCloseCallback(final WindowCloseCallback windowCloseCallback) {
		return windowCloseCallbacks.add(windowCloseCallback);
	}

	public boolean addFocusCallback(final WindowFocusCallback windowFocusCallback) {
		return windowFocusCallbacks.add(windowFocusCallback);
	}

	public boolean addIconifyCallback(final WindowIconifyCallback windowIconifyCallback) {
		return windowIconifyCallbacks.add(windowIconifyCallback);
	}

	public boolean addFramebufferCallback(final FramebufferSizeCallback framebufferSizeCallback) {
		return windowFramebufferSizeCallbacks.add(framebufferSizeCallback);
	}

	public boolean addKeyCallback(final KeyCallback keyCallback) {
		return windowKeyCallbacks.add(keyCallback);
	}

	public boolean addCharCallback(final CharCallback charCallback) {
		return windowCharCallbacks.add(charCallback);
	}

	public boolean addCharModsCallback(final CharModsCallback charModsCallback) {
		return windowCharModsCallbacks.add(charModsCallback);
	}

	public boolean addMouseButtonCallback(final MouseButtonCallback mouseButtonCallback) {
		return windowMouseButtonCallbacks.add(mouseButtonCallback);
	}

	public boolean addCursorPosCallback(final CursorPosCallback cursorPosCallback) {
		return windowCursorPosCallbacks.add(cursorPosCallback);
	}

	public boolean addCursorEnterCallback(final CursorEnterCallback cursorEnterCallback) {
		return windowCursorEnterCallbacks.add(cursorEnterCallback);
	}

	public boolean addScrollCallback(final ScrollCallback scrollCallback) {
		return windowScrollCallbacks.add(scrollCallback);
	}

	public boolean addDropCallback(final DropCallback dropCallback) {
		return windowDropCallbacks.add(dropCallback);
	}

	public boolean removePosCallback(final WindowPosCallback posCallback) {
		return windowPosCallbacks.remove(posCallback);
	}

	public boolean removeSizeCallback(final WindowSizeCallback windowSizeCallback) {
		return windowSizeCallbacks.remove(windowSizeCallback);
	}

	public boolean removeRefreshCallback(final WindowRefreshCallback windowRefreshCallback) {
		return windowRefreshCallbacks.remove(windowRefreshCallback);
	}

	public boolean removeCloseCallback(final WindowCloseCallback windowCloseCallback) {
		return windowCloseCallbacks.remove(windowCloseCallback);
	}

	public boolean removeFocusCallback(final WindowFocusCallback windowFocusCallback) {
		return windowFocusCallbacks.remove(windowFocusCallback);
	}

	public boolean removeIconifyCallback(final WindowIconifyCallback windowIconifyCallback) {
		return windowIconifyCallbacks.remove(windowIconifyCallback);
	}

	public boolean removeFramebufferCallback(final FramebufferSizeCallback framebufferSizeCallback) {
		return windowFramebufferSizeCallbacks.remove(framebufferSizeCallback);
	}

	public boolean removeKeyCallback(final KeyCallback keyCallback) {
		return windowKeyCallbacks.remove(keyCallback);
	}

	public boolean removeCharCallback(final CharCallback charCallback) {
		return windowCharCallbacks.remove(charCallback);
	}

	public boolean removeCharModsCallback(final CharModsCallback charModsCallback) {
		return windowCharModsCallbacks.remove(charModsCallback);
	}

	public boolean removeMouseButtonCallback(final MouseButtonCallback mouseButtonCallback) {
		return windowMouseButtonCallbacks.remove(mouseButtonCallback);
	}

	public boolean removeCursorPosCallback(final CursorPosCallback cursorPosCallback) {
		return windowCursorPosCallbacks.remove(cursorPosCallback);
	}

	public boolean removeCursorEnterCallback(final CursorEnterCallback cursorEnterCallback) {
		return windowCursorEnterCallbacks.remove(cursorEnterCallback);
	}

	public boolean removeScrollCallback(final ScrollCallback scrollCallback) {
		return windowScrollCallbacks.remove(scrollCallback);
	}

	public boolean removeDropCallback(final DropCallback dropCallback) {
		return windowDropCallbacks.remove(dropCallback);
	}

	public void iconify() {
		glfwIconifyWindow(id);
	}

	public void restore() {
		glfwRestoreWindow(id);
	}

	public void maximize() {
		glfwRestoreWindow(id);
	}

	public void show() {
		glfwShowWindow(id);
	}

	public void hide() {
		glfwHideWindow(id);
	}

	public void focus() {
		glfwFocusWindow(id);
	}

	public void close() {
		glfwSetWindowShouldClose(id, true);
	}

	public void reopen() {
		glfwSetWindowShouldClose(id, true);
	}

	public boolean shouldClose() {
		return glfwWindowShouldClose(id);
	}

	public void setSwapInterval(final int i) {
		useContext();
		glfwSwapInterval(i);
	}

	public void setTitle(final String title) {
		glfwSetWindowTitle(id, title);
	}

	public void setWindowIcon(final GLFWImage.Buffer image) {
		glfwSetWindowIcon(id, image);
	}

	public Vector2i getPos() {
		final int[] posx = new int[1];
		final int[] posy = new int[1];
		glfwGetWindowPos(id, posx, posy);
		return new Vector2i(posx[0], posy[0]);
	}

	public void setPos(final int posx, final int posy) {
		glfwSetWindowPos(id, posx, posy);
	}

	public Vector2i getSize() {
		final int[] sizex = new int[1];
		final int[] sizey = new int[1];
		glfwGetWindowSize(id, sizex, sizey);
		return new Vector2i(sizex[0], sizey[0]);
	}

	public void setSize(final int sizex, final int sizey) {
		glfwSetWindowSize(id, sizex, sizey);
	}

	public void setSizeLimits(final int minwidth, final int minheight, final int maxwidth, final int maxheight) {
		glfwSetWindowSizeLimits(id, minwidth, minheight, maxwidth, maxheight);
	}

	public void setAspectRatio(final int numer, final int denom) {
		glfwSetWindowAspectRatio(id, numer, denom);
	}

	public Vector2i getFramebufferSize() {
		final int[] sizex = new int[1];
		final int[] sizey = new int[1];
		glfwGetFramebufferSize(id, sizex, sizey);
		return new Vector2i(sizex[0], sizey[0]);
	}

	public Vector4i getFrameSize() {
		final int[] left = new int[1];
		final int[] top = new int[1];
		final int[] right = new int[1];
		final int[] bottom = new int[1];
		glfwGetWindowFrameSize(id, left, top, right, bottom);
		return new Vector4i(left[0], top[0], right[0], bottom[0]);
	}

	public void setMonitor(final Monitor monitor, final int xpos, final int ypos, final int width, final int height, final int refreshRate) {
		glfwSetWindowMonitor(id, monitor != null ? monitor.getId() : 0, xpos, ypos, width, height, refreshRate);
	}

	public void center() {
		final Vector2i size = getSize();
		final Monitor monitor = Monitor.getPrimaryMonitor(glfw);
		final GLFWVidMode vidmode = monitor.getCurrentVidMode();
		setPos((vidmode.width() - size.x) / 2, (vidmode.height() - size.y) / 2);
	}

	public Vector2d getMousePos() {
		final double[] xpos = new double[1];
		final double[] ypos = new double[1];
		glfwGetCursorPos(id, xpos, ypos);
		return new Vector2d(xpos[0], ypos[0]);
	}

	public Vector2d getMousePosRelative() {
		final Vector2d mouse = getMousePos();
		final Vector2i screen = getFramebufferSize();
		return new Vector2d((mouse.x - screen.x * 0.5) * 2 / screen.x, (mouse.y - screen.y * 0.5) * 2 / screen.y);
	}

	public long getId() {
		return id;
	}

	public int getRefreshRate() {
		return refreshRate;
	}

	public void swapBuffers() {
		glfwSwapBuffers(id);
	}

	public boolean isDestroyed() {
		return destroyed;
	}

	public void destroy() {
		useContext();
		glfwFreeCallbacks(id);
		glfwDestroyWindow(id);
		this.destroyed = true;
	}
}
