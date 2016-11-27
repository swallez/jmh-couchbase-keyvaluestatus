package net.bluxte.experiments.couchbase_keyvalue;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;

/**
 * A benchmark to see the effects of Short boxing in map lookups.
 * Results are not conclusive.
 */
@State(Scope.Benchmark)
public class BoxingBenchmark {
    @Param({
            "0",
            "127",
            "128",
            "255",
            "256",
            "511",
            "512",
            "1023",
            "1024"
    })
    public short code;

    @Benchmark
    public KeyValueStatus valueOfLookupMap() {
        return KeyValueStatus.valueOfLookupMap(code);
    }
}

/*
Results:

Benchmark                                   (code)  Mode  Samples  Score  Score error  Units
n.b.e.c.BoxingBenchmark.valueOfLookupMap         0  avgt       10  4.706        0.123  ns/op
n.b.e.c.BoxingBenchmark.valueOfLookupMap       127  avgt       10  3.080        0.067  ns/op
n.b.e.c.BoxingBenchmark.valueOfLookupMap       128  avgt       10  6.733        0.239  ns/op
n.b.e.c.BoxingBenchmark.valueOfLookupMap       255  avgt       10  4.115        0.121  ns/op
n.b.e.c.BoxingBenchmark.valueOfLookupMap       256  avgt       10  7.003        0.189  ns/op
n.b.e.c.BoxingBenchmark.valueOfLookupMap       511  avgt       10  4.031        0.130  ns/op
n.b.e.c.BoxingBenchmark.valueOfLookupMap       512  avgt       10  6.949        1.128  ns/op
n.b.e.c.BoxingBenchmark.valueOfLookupMap      1023  avgt       10  4.020        0.175  ns/op
n.b.e.c.BoxingBenchmark.valueOfLookupMap      1024  avgt       10  6.752        0.178  ns/op
*/
