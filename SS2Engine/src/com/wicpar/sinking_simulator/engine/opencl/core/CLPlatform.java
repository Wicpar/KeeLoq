package com.wicpar.sinking_simulator.engine.opencl.core;

import org.lwjgl.BufferUtils;
import org.lwjgl.PointerBuffer;
import org.lwjgl.opencl.CL;
import org.lwjgl.opencl.CLCapabilities;

import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.wicpar.sinking_simulator.engine.opencl.util.InfoUtil.checkCLError;
import static com.wicpar.sinking_simulator.engine.opencl.util.InfoUtil.getPlatformInfoStringUTF8;
import static org.lwjgl.opencl.CL10.*;
import static org.lwjgl.opencl.KHRICD.CL_PLATFORM_ICD_SUFFIX_KHR;

/**
 * Created by Frederic on 28/11/2016.
 */
public class CLPlatform {

	private final List<CLDevice> devices;
	private final List<CLDevice> GPUDevices;
	private final List<CLDevice> CPUDevices;
	private final List<CLDevice> AcceleratorDevices;
	private final List<CLDevice> DefaultDevices;

	private final long id;
	private final String platformProfile;
	private final String platformVersion;
	private final String platformName;
	private final String platformVendor;
	private final String platformExtensions;
	private final String platformICDSuffixKHR;
	private final CLCapabilities platformCaps;

	@Override
	public String toString() {

		return "\n-------------------------\n" +
				"PLATFORM: " + Long.toHexString(id) + "\n\n" +
				info("CL_PLATFORM_PROFILE", platformProfile) +
				info("CL_PLATFORM_VERSION", platformVersion) +
				info("CL_PLATFORM_NAME", platformName) +
				info("CL_PLATFORM_VENDOR", platformVendor) +
				info("CL_PLATFORM_EXTENSIONS", platformExtensions) +
				info("CL_PLATFORM_ICD_SUFFIX_KHR", platformICDSuffixKHR) +
				"\n" +
				Arrays.toString(devices.toArray());

	}

	private static String info(final String name, final String value) {
		return "\t" + name + " = " + value + "\n";
	}

	private CLPlatform(final long id) {
		this.id = id;
		final IntBuffer pi = BufferUtils.createIntBuffer(1);


		platformCaps = CL.createPlatformCapabilities(this.id);

		this.platformProfile = getPlatformInfoStringUTF8(this.id, CL_PLATFORM_PROFILE);
		this.platformVersion = getPlatformInfoStringUTF8(this.id, CL_PLATFORM_VERSION);
		this.platformName = getPlatformInfoStringUTF8(this.id, CL_PLATFORM_NAME);
		this.platformVendor = getPlatformInfoStringUTF8(this.id, CL_PLATFORM_VENDOR);
		this.platformExtensions = getPlatformInfoStringUTF8(this.id, CL_PLATFORM_EXTENSIONS);
		if (platformCaps.cl_khr_icd)
			this.platformICDSuffixKHR = getPlatformInfoStringUTF8(this.id, CL_PLATFORM_ICD_SUFFIX_KHR);
		else
			this.platformICDSuffixKHR = "null";

		checkCLError(clGetDeviceIDs(this.id, CL_DEVICE_TYPE_ALL, null, pi));

		final PointerBuffer deviceIds = PointerBuffer.allocateDirect(pi.get(0));
		checkCLError(clGetDeviceIDs(this.id, CL_DEVICE_TYPE_ALL, deviceIds, (IntBuffer) null));
		this.devices = CLDevice.fromIdList(deviceIds, this);
		this.AcceleratorDevices = new ArrayList<>();
		this.GPUDevices = new ArrayList<>();
		this.CPUDevices = new ArrayList<>();
		this.DefaultDevices = new ArrayList<>();
		this.devices.forEach(device -> {
			if (device.getDeviceType() == CL_DEVICE_TYPE_CPU) CPUDevices.add(device);
			if (device.getDeviceType() == CL_DEVICE_TYPE_GPU) GPUDevices.add(device);
			if (device.getDeviceType() == CL_DEVICE_TYPE_ACCELERATOR) AcceleratorDevices.add(device);
			if (device.getDeviceType() == CL_DEVICE_TYPE_DEFAULT) DefaultDevices.add(device);
		});
	}

	public List<CLDevice> getDevices() {
		return devices;
	}

	public List<CLDevice> getGPUDevices() {
		return GPUDevices;
	}

	public List<CLDevice> getCPUDevices() {
		return CPUDevices;
	}

	public List<CLDevice> getAcceleratorDevices() {
		return AcceleratorDevices;
	}

	public List<CLDevice> getDefaultDevices() {
		return DefaultDevices;
	}

	public long getId() {
		return id;
	}

	public String getPlatformProfile() {
		return platformProfile;
	}

	public String getPlatformVersion() {
		return platformVersion;
	}

	public String getPlatformName() {
		return platformName;
	}

	public String getPlatformVendor() {
		return platformVendor;
	}

	public String getPlatformExtensions() {
		return platformExtensions;
	}

	public String getPlatformICDSuffixKHR() {
		return platformICDSuffixKHR;
	}

	public CLCapabilities getPlatformCaps() {
		return platformCaps;
	}

	private static List<CLPlatform> fromIdList(final PointerBuffer platforms) {
		final ArrayList<CLPlatform> returnList = new ArrayList<>();
		for (int p = 0; p < platforms.capacity(); p++) {
			final CLPlatform platform = new CLPlatform(platforms.get(p));
			returnList.add(platform);
		}
		return returnList;
	}

	public static List<CLPlatform> getAllPlatforms() {
		final IntBuffer pi = BufferUtils.createIntBuffer(1);
		checkCLError(clGetPlatformIDs(null, pi));
		if (pi.get(0) == 0)
			throw new RuntimeException("No OpenCL platforms found.");
		final PointerBuffer platforms = PointerBuffer.allocateDirect(pi.get(0));
		checkCLError(clGetPlatformIDs(platforms, (IntBuffer) null));
		return CLPlatform.fromIdList(platforms);
	}

	public static CLPlatform fromId(final long id) {
		return new CLPlatform(id);
	}
}
