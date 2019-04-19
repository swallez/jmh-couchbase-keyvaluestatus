package net.bluxte.experiments.talk;

import net.bluxte.experiments.couchbase_keyvalue.KeyValueStatus;

import java.util.List;
import java.util.Random;

public class MissionControlMain {

    public String foo(int a, char[] b, List<Integer> c, boolean d) {
        return "blah";
    }

    // I often forget the running process after demos...
    private static long MAX_NANOS = 120 * 60 * 1_000_000_000L;

    private static Random rnd = new Random();

    public static KeyValueStatus resolved;

    // Run with -XX:+UnlockCommercialFeatures -XX:+FlightRecorder
    public static void main(String[] args) throws Exception {

        long start = System.nanoTime();

        while(System.nanoTime() - start < MAX_NANOS) {
            for (int i = 0; i < 1_000_000; i++) {
                resolved = resolve((short)rnd.nextInt(0x10));
            }
            Thread.sleep(100);
        }
    }

    private static KeyValueStatus resolve(short value) {

        // Iteration on enum.values()
        return KeyValueStatus.valueOfLoop(value);

        // Iteration on enum.values() with fast path
        //return KeyValueStatus.valueOf(value);

        // No fast path, use a static array to avoid allocation when calling values()
        //return KeyValueStatus.valueOfLoopOnConstantArray(value);

        // Lookup map: code (Short) -> KeyValueStatus
        //return KeyValueStatus.valueOfLookupMap(value);

        // Lookup array: code -> KeyValueStatus
        //return KeyValueStatus.valueOfLookupArray(value);

    }

    public String foo(long a, char[] b, List<Integer> c, boolean d) {
        return "";
    }
}
