import com.wicpar.sinking_simulator.engine.crashes.CrashApp;
import com.wicpar.sinking_simulator.engine.opencl.core.CLCommandQueue;
import com.wicpar.sinking_simulator.engine.opencl.core.CLContext;
import com.wicpar.sinking_simulator.engine.opencl.core.OpenCL;
import com.wicpar.sinking_simulator.engine.opencl.memory.CLBuffer;
import com.wicpar.sinking_simulator.engine.opencl.program.CLKernel;
import com.wicpar.sinking_simulator.utils.CLProgramHandler;
import org.lwjgl.BufferUtils;
import org.lwjgl.PointerBuffer;

import java.nio.IntBuffer;
import java.util.Random;

import static org.lwjgl.opencl.CL10.CL_CONTEXT_PLATFORM;
import static org.lwjgl.opencl.CL10.CL_MEM_READ_WRITE;
import static org.lwjgl.opencl.CL10.clEnqueueReadBuffer;

/**
 * Created by Frederic on 16/08/2016.
 */
public class Main {

    public static void main(final String[] args) {
        try {
            System.out.println(OpenCL.platforms);

            final PointerBuffer ctxProps = BufferUtils.createPointerBuffer(3);
            ctxProps.put(CL_CONTEXT_PLATFORM)
                    .put(OpenCL.platforms.get(0).getId())
                    .put(0).flip();
            CLContext ctx = new CLContext(ctxProps, OpenCL.platforms.get(0).getDevices(), OpenCL.clContextCallback, 0);
            final CLProgramHandler programHandler = new CLProgramHandler(ctx, ctx.getDevices().get(0));
            final CLCommandQueue queue = new CLCommandQueue(ctx);
            final CLKernel make = programHandler.getProgram("keeloq_gen");
            final CLKernel find = programHandler.getProgram("keeloq");
            final CLBuffer encrypted = new CLBuffer(ctx, 3 * 4, CL_MEM_READ_WRITE);
            final CLBuffer decrypted = new CLBuffer(ctx, 2 * 4, CL_MEM_READ_WRITE);
            final IntBuffer keys = BufferUtils.createIntBuffer(2);
            keys.put(new Random().nextInt());
            keys.put(new Random().nextInt());
            keys.flip();
            final IntBuffer ret = BufferUtils.createIntBuffer(2);
            make.setArg(0, encrypted);
            make.setArg(1, keys.get(0), keys.get(1));
            make.setArg(2, 1);

            find.setArg(0, encrypted);
            find.setArg(1, decrypted);

            make.enqueue(queue, 1).awaitCompletion();
            System.out.println(keys.get(0) + " : " + keys.get(1));

            final int cores = 2048;
            final int iters = 2048;
            final int start = keys.get(0);
            int i = start - 1;
            while (++i < start + 1) {
                int j = -1;
                while (++j < 0x100000000L / cores / iters) {
                    find.setArg(2, i, i);
                    find.setArg(3, j * cores * iters, j * cores * iters + iters);
                    find.setArg(4, cores);
                    find.enqueue(queue, cores).awaitCompletion();
                    clEnqueueReadBuffer(queue.getId(), decrypted.getId(), 1, 0, ret, null, null);
                    if (ret.get(0) == keys.get(0) && ret.get(1) == keys.get(1)) {
                        System.out.println(ret.get(0) + " : " + ret.get(1));
                        return;
                    }
                    System.out.println(i + " : " + j + " / " + (0x100000000L / cores / iters));
                }
            }
        } catch (final Exception e) {
            e.printStackTrace();
            CrashApp.catchMessageWithGUI(e);
        }
    }
}
