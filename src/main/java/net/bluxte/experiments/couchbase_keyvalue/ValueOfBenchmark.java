/*
 * Copyright (c) 2005, 2014, Oracle and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.  Oracle designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Oracle in the LICENSE file that accompanied this code.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Oracle, 500 Oracle Parkway, Redwood Shores, CA 94065 USA
 * or visit www.oracle.com if you need additional information or have any
 * questions.
 */

package net.bluxte.experiments.couchbase_keyvalue;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;

@State(Scope.Benchmark)
public class ValueOfBenchmark {

    @Param({
            "0", // 0x00, Success
            "1", // 0x01, Not Found
            "134", // 0x86 Temporary Failure
            "255", // undefined
            "1024" // undefined, out of bounds
    })
    public short code;

    @Benchmark
    public KeyValueStatus loopNoFastPath() {
        return KeyValueStatus.valueOfLoop(code);
    }

    @Benchmark
    public KeyValueStatus loopFastPath() {
        return KeyValueStatus.valueOf(code);
    }

    @Benchmark
    public KeyValueStatus loopOnConstantArray() {
        return KeyValueStatus.valueOfLoopOnConstantArray(code);
    }

    @Benchmark
    public KeyValueStatus lookupMap() {
        return KeyValueStatus.valueOfLookupMap(code);
    }

    @Benchmark
    public KeyValueStatus lookupArray() {
        return KeyValueStatus.valueOfLookupArray(code);
    }

    @Benchmark
    public KeyValueStatus lookupArrayUnchecked() {
        return KeyValueStatus.valueOfLookupArrayUnchecked(code);
    }

    @Benchmark
    public KeyValueStatus lookupBigSwitch() {
        return KeyValueStatus.valueOfBigSwitch(code);
    }
}

/*

Benchmark                                        (code)  Mode  Samples   Score  Score error  Units
n.b.e.c.ValueOfBenchmark.lookupBigSwitch              0  avgt       10   3.791        0.070  ns/op
n.b.e.c.ValueOfBenchmark.lookupBigSwitch              1  avgt       10   3.760        0.128  ns/op
n.b.e.c.ValueOfBenchmark.lookupBigSwitch            134  avgt       10   4.410        0.164  ns/op
n.b.e.c.ValueOfBenchmark.lookupBigSwitch            255  avgt       10   3.390        0.090  ns/op
n.b.e.c.ValueOfBenchmark.lookupBigSwitch           1024  avgt       10   3.431        0.119  ns/op

n.b.e.c.ValueOfBenchmark.lookupArray                  0  avgt       10   3.061        0.126  ns/op
n.b.e.c.ValueOfBenchmark.lookupArray                  1  avgt       10   3.048        0.127  ns/op
n.b.e.c.ValueOfBenchmark.lookupArray                134  avgt       10   3.070        0.084  ns/op
n.b.e.c.ValueOfBenchmark.lookupArray                255  avgt       10   3.035        0.113  ns/op
n.b.e.c.ValueOfBenchmark.lookupArray               1024  avgt       10   3.034        0.113  ns/op

n.b.e.c.ValueOfBenchmark.lookupArrayUnchecked         0  avgt       10   3.055        0.079  ns/op
n.b.e.c.ValueOfBenchmark.lookupArrayUnchecked         1  avgt       10   3.056        0.094  ns/op
n.b.e.c.ValueOfBenchmark.lookupArrayUnchecked       134  avgt       10   3.055        0.091  ns/op
n.b.e.c.ValueOfBenchmark.lookupArrayUnchecked       255  avgt       10   3.051        0.105  ns/op
n.b.e.c.ValueOfBenchmark.lookupArrayUnchecked      1024  avgt       10   3.054        0.106  ns/op

n.b.e.c.ValueOfBenchmark.lookupMap                    0  avgt       10   4.954        0.134  ns/op
n.b.e.c.ValueOfBenchmark.lookupMap                    1  avgt       10   4.036        0.125  ns/op
n.b.e.c.ValueOfBenchmark.lookupMap                  134  avgt       10   5.597        0.157  ns/op
n.b.e.c.ValueOfBenchmark.lookupMap                  255  avgt       10   4.006        0.144  ns/op
n.b.e.c.ValueOfBenchmark.lookupMap                 1024  avgt       10   6.752        0.228  ns/op

n.b.e.c.ValueOfBenchmark.loopFastPath                 0  avgt       10   3.044        0.092  ns/op
n.b.e.c.ValueOfBenchmark.loopFastPath                 1  avgt       10   3.040        0.051  ns/op
n.b.e.c.ValueOfBenchmark.loopFastPath               134  avgt       10  25.070        0.637  ns/op
n.b.e.c.ValueOfBenchmark.loopFastPath               255  avgt       10  31.215        1.089  ns/op
n.b.e.c.ValueOfBenchmark.loopFastPath              1024  avgt       10  32.464        0.930  ns/op

n.b.e.c.ValueOfBenchmark.loopNoFastPath               0  avgt       10  19.383        0.331  ns/op
n.b.e.c.ValueOfBenchmark.loopNoFastPath               1  avgt       10  19.243        0.376  ns/op
n.b.e.c.ValueOfBenchmark.loopNoFastPath             134  avgt       10  24.855        0.651  ns/op
n.b.e.c.ValueOfBenchmark.loopNoFastPath             255  avgt       10  30.587        0.833  ns/op
n.b.e.c.ValueOfBenchmark.loopNoFastPath            1024  avgt       10  30.619        1.209  ns/op

n.b.e.c.ValueOfBenchmark.loopOnConstantArray          0  avgt       10   2.975        0.086  ns/op
n.b.e.c.ValueOfBenchmark.loopOnConstantArray          1  avgt       10   3.035        0.080  ns/op
n.b.e.c.ValueOfBenchmark.loopOnConstantArray        134  avgt       10  10.215        0.269  ns/op
n.b.e.c.ValueOfBenchmark.loopOnConstantArray        255  avgt       10  16.856        0.679  ns/op
n.b.e.c.ValueOfBenchmark.loopOnConstantArray       1024  avgt       10  17.015        0.577  ns/op

*/
