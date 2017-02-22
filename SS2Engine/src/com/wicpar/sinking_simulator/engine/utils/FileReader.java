package com.wicpar.sinking_simulator.engine.utils;

import org.lwjgl.BufferUtils;
import org.lwjgl.stb.STBImage;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.*;
import java.util.stream.StreamSupport;


/**
 * Created by Frederic on 11/03/2016 at 21:45.
 */
public class FileReader {
    public static String readFile(final String path) {
        final StringBuilder file = new StringBuilder();
        try {
            final String p = path.replace(File.separator, "/")
                    .replace("src/", "/");
            final InputStream in = FileReader.class.getResourceAsStream(p);
            BufferedReader reader;
            final File f = new File(path);
            if (in == null || (f.exists() && !f.isDirectory()) && f.canRead())
                try {
                    reader = new BufferedReader(new java.io.FileReader(path));
                } catch (final FileNotFoundException e) {
                    reader = new BufferedReader(new InputStreamReader(in));
                }
            else
                reader = new BufferedReader(new InputStreamReader(in));
            String line;
            while ((line = reader.readLine()) != null)
                file.append(line)
                        .append("\n");
            reader.close();
        } catch (final IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Could not read file: " + path);
        }
        return file.toString();
    }

    public static ByteBuffer readImage(final File f, final IntBuffer width, final IntBuffer height, final IntBuffer components, final int desiredChannels) throws IOException {

        final InputStream in = f.exists() ? new FileInputStream(f.getPath()) : FileReader.class.getResourceAsStream(f.getPath()
                .replace(File.separator, "/")
                .replace("src/", "/"));
        final ArrayList<Byte> rawData = StreamSupport.stream(Spliterators.spliteratorUnknownSize(new Iterator<ArrayList<Byte>>() {
            @Override
            public boolean hasNext() {
                try {
                    return in.available() > 0;
                } catch (final IOException e) {
                    return false;
                }
            }

            @Override
            public ArrayList<Byte> next() {
                try {
                    final byte[] bytes = new byte[in.available()];
                    final int read = in.read(bytes);
                    final ArrayList<Byte> arr = new ArrayList<>(read);
                    for (int i = 0; i < read; i++) {
                        arr.add(bytes[i]);
                    }
                    return arr;
                } catch (final IOException e) {
                    return new ArrayList<>();
                }
            }
        }, Spliterator.ORDERED), false)
                .reduce((bytes, bytes2) -> {
                    bytes.addAll(bytes2);
                    return bytes;
                })
                .get();
        if (rawData == null)
            throw new IOException("failed to read image");
        final ByteBuffer mem = BufferUtils.createByteBuffer(rawData.size());
        rawData.forEach(mem::put);
        mem.flip();
        if (!STBImage.stbi_info_from_memory(mem, width, height, components))
            throw new RuntimeException("Failed to read image information: " + STBImage.stbi_failure_reason());
        final ByteBuffer image = STBImage.stbi_load_from_memory(mem, width, height, components, desiredChannels);
        if (image == null)
            throw new RuntimeException("Failed to load image: " + STBImage.stbi_failure_reason());
        return image;
    }


}
