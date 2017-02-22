package com.wicpar.sinking_simulator.engine.opencl.core;

import org.lwjgl.PointerBuffer;
import org.lwjgl.opencl.CL;
import org.lwjgl.opencl.CLCapabilities;

import java.util.ArrayList;
import java.util.List;

import static com.wicpar.sinking_simulator.engine.opencl.util.InfoUtil.*;
import static org.lwjgl.opencl.CL10.*;
import static org.lwjgl.opencl.CL11.CL_DEVICE_OPENCL_C_VERSION;

/**
 * Created by Frederic on 28/11/2016.
 */
public class CLDevice {

	private final long id;

	private final CLCapabilities capabilities;

	private final long deviceType;
	private final int vendorId;
	private final int maxComputeUnits;
	private final int maxWorkItemDimensions;
	private final long maxWorkGroupSize;
	private final int maxClockFrequency;
	private final int addressBits;
	private final boolean available;
	private final boolean compilerAvailable;

	private final CLPlatform platform;

	private final String name;
	private final String vendor;
	private final String driverVersion;
	private final String profile;
	private final String version;
	private final String extensions;
	private final String cVersion;

	@Override
	public String toString() {

		return "\n-------------------------\n" +
				"DEVICE: " + Long.toHexString(id) + "\n\n" +
				"\tCL_DEVICE_TYPE = " + deviceType + "\n" +
				"\tCL_DEVICE_VENDOR_ID = " + vendorId + "\n" +
				"\tCL_DEVICE_MAX_COMPUTE_UNITS = " + maxComputeUnits + "\n" +
				"\tCL_DEVICE_MAX_WORK_ITEM_DIMENSIONS = " + maxWorkItemDimensions + "\n" +
				"\tCL_DEVICE_MAX_WORK_GROUP_SIZE = " + maxWorkGroupSize + "\n" +
				"\tCL_DEVICE_MAX_CLOCK_FREQUENCY = " + maxClockFrequency + "\n" +
				"\tCL_DEVICE_ADDRESS_BITS = " + addressBits + "\n" +
				"\tCL_DEVICE_AVAILABLE = " + available + "\n" +
				"\tCL_DEVICE_COMPILER_AVAILABLE = " + compilerAvailable + "\n" +
				info("CL_DEVICE_NAME", name) +
				info("CL_DEVICE_VENDOR", vendor) +
				info("CL_DRIVER_VERSION", driverVersion) +
				info("CL_DEVICE_PROFILE", profile) +
				info("CL_DEVICE_VERSION", version) +
				info("CL_DEVICE_EXTENSIONS", extensions) +
				info("CL_DEVICE_OPENCL_C_VERSION", cVersion);

	}

	private static String info(final String name, final String value) {
		return "\t" + name + " = " + value + "\n";
	}


	private CLDevice(final long id, final CLPlatform platform) {
		this.platform = platform;
		this.id = id;
		capabilities = CL.createDeviceCapabilities(this.id, platform.getPlatformCaps());

		System.out.printf("\n\t** DEVICE: [0x%X]\n", this.id);

		deviceType = getDeviceInfoLong(this.id, CL_DEVICE_TYPE);
		vendorId = getDeviceInfoInt(this.id, CL_DEVICE_VENDOR_ID);
		maxComputeUnits = getDeviceInfoInt(this.id, CL_DEVICE_MAX_COMPUTE_UNITS);
		maxWorkItemDimensions = getDeviceInfoInt(this.id, CL_DEVICE_MAX_WORK_ITEM_DIMENSIONS);
		maxWorkGroupSize = getDeviceInfoPointer(this.id, CL_DEVICE_MAX_WORK_GROUP_SIZE);
		maxClockFrequency = getDeviceInfoInt(this.id, CL_DEVICE_MAX_CLOCK_FREQUENCY);
		addressBits = getDeviceInfoInt(this.id, CL_DEVICE_ADDRESS_BITS);
		available = (getDeviceInfoInt(this.id, CL_DEVICE_AVAILABLE) != 0);
		compilerAvailable = (getDeviceInfoInt(this.id, CL_DEVICE_COMPILER_AVAILABLE) != 0);

		name = getDeviceInfoStringUTF8(this.id, CL_DEVICE_NAME);
		vendor = getDeviceInfoStringUTF8(this.id, CL_DEVICE_VENDOR);
		driverVersion = getDeviceInfoStringUTF8(this.id, CL_DRIVER_VERSION);
		profile = getDeviceInfoStringUTF8(this.id, CL_DEVICE_PROFILE);
		version = getDeviceInfoStringUTF8(this.id, CL_DEVICE_VERSION);
		extensions = getDeviceInfoStringUTF8(this.id, CL_DEVICE_EXTENSIONS);
		if (capabilities.OpenCL11) cVersion = getDeviceInfoStringUTF8(this.id, CL_DEVICE_OPENCL_C_VERSION);
		else cVersion = "null";
	}

	private CLDevice(final long id) {
		this(id, CLPlatform.fromId(getDeviceInfoLong(id, CL_DEVICE_PLATFORM)));
	}

	public long getId() {
		return id;
	}

	public CLCapabilities getCapabilities() {
		return capabilities;
	}

	public long getDeviceType() {
		return deviceType;
	}

	public int getVendorId() {
		return vendorId;
	}

	public int getMaxComputeUnits() {
		return maxComputeUnits;
	}

	public int getMaxWorkItemDimensions() {
		return maxWorkItemDimensions;
	}

	public long getMaxWorkGroupSize() {
		return maxWorkGroupSize;
	}

	public int getMaxClockFrequency() {
		return maxClockFrequency;
	}

	public int getAddressBits() {
		return addressBits;
	}

	public boolean isAvailable() {
		return available;
	}

	public boolean isCompilerAvailable() {
		return compilerAvailable;
	}

	public String getName() {
		return name;
	}

	public String getVendor() {
		return vendor;
	}

	public String getDriverVersion() {
		return driverVersion;
	}

	public String getProfile() {
		return profile;
	}

	public String getVersion() {
		return version;
	}

	public String getExtensions() {
		return extensions;
	}

	public String getcVersion() {
		return cVersion;
	}

	public CLPlatform getPlatform() {
		return platform;
	}

	public static List<CLDevice> fromIdList(final PointerBuffer devices, final CLPlatform clPlatform) {
		final ArrayList<CLDevice> returnList = new ArrayList<>();
		for (int p = 0; p < devices.capacity(); p++) {
			final long id = devices.get(p);
			if (id != 0)
				returnList.add(new CLDevice(devices.get(p), clPlatform));
		}

		return returnList;
	}

	public static CLDevice fromId(final long id) {
		return new CLDevice(id);
	}
}
