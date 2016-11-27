/*
 * Copyright (c) 2016 Couchbase, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.bluxte.experiments.couchbase_keyvalue;

// Original at https://github.com/couchbase/couchbase-jvm-core/blob/master/src/main/java/com/couchbase/client/core/endpoint/kv/KeyValueStatus.java
// Additional implementations of valueOf at the bottom of this class

import java.util.Arrays;
import java.util.HashMap;

/**
 * Enum describing all known response status codes that could be seen on the KeyValue protocol.
 *
 * Based on include/memcached/protocol_binary.h from memcached repository.
 *
 * @author Sergey Avseyev
 * @author Michael Nitschinger
 * @since 1.2.0
 * @see com.couchbase.client.core.message.ResponseStatus
 * @see com.couchbase.client.core.endpoint.ResponseStatusConverter
 */
public enum KeyValueStatus {

    /* This value describes unmatched code */
    UNKNOWN((short) -1, "Unknown code (dummy value)"),

    SUCCESS((short) 0x00,
            "The operation completed successfully"),
    ERR_NOT_FOUND((short) 0x01,
            "The key does not exists"),
    ERR_EXISTS((short) 0x02,
            "The key exists in the cluster (with another CAS value)"),
    ERR_TOO_BIG((short) 0x03,
            "The document exceeds the maximum size"),
    ERR_INVALID((short) 0x04,
            "Invalid request"),
    ERR_NOT_STORED((short) 0x05,
            "The document was not stored for some reason"),
    ERR_DELTA_BADVAL((short) 0x06,
            "Non-numeric server-side value for incr or decr"),
    ERR_NOT_MY_VBUCKET((short) 0x07,
            "The server is not responsible for the requested vbucket"),
    ERR_NO_BUCKET((short) 0x08,
            "Not connected to a bucket"),
    ERR_AUTH_STALE((short) 0x1f,
            "The authentication context is stale, reauthentication should be performed"),
    ERR_AUTH_ERROR((short) 0x20,
            "Authentication failure"),
    ERR_AUTH_CONTINUE((short) 0x21,
            "Authentication OK, but further action needed"),
    ERR_RANGE((short) 0x22,
            "The requested value is outside the legal range"),
    ERR_ROLLBACK((short) 0x23,
            "Roll back to an earlier version of the vbucket UUID"),
    ERR_ACCESS((short) 0x24,
            "No access"),
    ERR_NOT_INITIALIZED((short) 0x25,
            "The server is currently initializing this node"),
    ERR_UNKNOWN_COMMAND((short) 0x81,
            "The server does not know this command"),
    ERR_NO_MEM((short) 0x82,
            "Not enough memory"),
    ERR_NOT_SUPPORTED((short) 0x83,
            "The server does not support this command"),
    ERR_INTERNAL((short) 0x84,
            "An internal error in the server"),
    ERR_BUSY((short) 0x85,
            "The system is currently too busy to handle the request"),
    ERR_TEMP_FAIL((short) 0x86,
            "A temporary error condition occurred. Retrying the operation may resolve the problem."),

    /* Sub-document specific responses */
    ERR_SUBDOC_PATH_NOT_FOUND((short) 0xc0,
            "The provided path does not exist in the document"),
    ERR_SUBDOC_PATH_MISMATCH((short) 0xc1,
            "One of path components treats a non-dictionary as a dictionary, or a non-array as an array, or value the path points to is not a number"),
    ERR_SUBDOC_PATH_INVALID((short) 0xc2,
            "The path's syntax was incorrect"),
    ERR_SUBDOC_PATH_TOO_BIG((short) 0xc3,
            "The path provided is too large: either the string is too long, or it contains too many components"),
    ERR_SUBDOC_DOC_TOO_DEEP((short) 0xc4,
            "The document has too many levels to parse"),
    ERR_SUBDOC_VALUE_CANTINSERT((short) 0xc5,
            "The value provided will invalidate the JSON if inserted"),
    ERR_SUBDOC_DOC_NOT_JSON((short) 0xc6,
            "The existing document is not valid JSON"),
    ERR_SUBDOC_NUM_RANGE((short) 0xc7,
            "The existing number is out of the valid range for arithmetic operations"),
    ERR_SUBDOC_DELTA_RANGE((short) 0xc8,
            "The operation would result in a number outside the valid range"),
    ERR_SUBDOC_PATH_EXISTS((short) 0xc9,
            "The requested operation requires the path to not already exist, but it exists"),
    ERR_SUBDOC_VALUE_TOO_DEEP((short) 0xca,
            "Inserting the value would cause the document to be too deep"),
    ERR_SUBDOC_INVALID_COMBO((short) 0xcb,
            "An invalid combination of commands was specified"),
    ERR_SUBDOC_MULTI_PATH_FAILURE((short) 0xcc,
            "Specified key was successfully found, but one or more path operations failed");


    private final short code;
    private final String description;

    KeyValueStatus(short code, String description) {
        this.code = code;
        this.description = description;
    }

    /**
     * Determine the right {@link KeyValueStatus} for the given status code.
     *
     * Certain status codes are checked upfront since they are most commonly converted (this avoids iterating
     * through the full enum values list, especially in the non-corner or failure case variants).
     *
     * @param code the status code to check.
     * @return the matched code, or unknown if none is found.
     */
    public static KeyValueStatus valueOf(final short code) {
        if (code == SUCCESS.code) {
            return SUCCESS;
        } else if (code == ERR_NOT_FOUND.code) {
            return ERR_NOT_FOUND;
        } else if (code == ERR_EXISTS.code) {
            return ERR_EXISTS;
        } else if (code == ERR_NOT_MY_VBUCKET.code) {
            return ERR_NOT_MY_VBUCKET;
        }

        for (KeyValueStatus keyValueStatus : values()) {
            if (keyValueStatus.code() == code) {
                return keyValueStatus;
            }
        }
        return UNKNOWN;
    }

    public short code() {
        return code;
    }

    public String description() {
        return description;
    }

    //---------------------------------------------------------------------------------------------
    // Original implementation, with no fast path

    public static KeyValueStatus valueOfLoop(final short code) {
        for (KeyValueStatus value: values()) {
            if (value.code() == code) return value;
        }
        return UNKNOWN;
    }

    //---------------------------------------------------------------------------------------------
    // No fast path, use a static array to avoid allocation when calling values()

    private static final KeyValueStatus[] VALUES = values();

    public static KeyValueStatus valueOfLoopOnConstantArray(final short code) {
        for (KeyValueStatus value: VALUES) {
            if (value.code() == code) return value;
        }
        return UNKNOWN;
    }

    //---------------------------------------------------------------------------------------------
    // Using a map

    private static final HashMap<Short, KeyValueStatus> code2statusMap = new HashMap<>();

    static {
        for (KeyValueStatus value: values()) {
            code2statusMap.put(value.code(), value);
        }
    }

    public static KeyValueStatus valueOfLookupMap(final short code) {
        return code2statusMap.getOrDefault(code, UNKNOWN);
    }

    //---------------------------------------------------------------------------------------------
    // Using a lookup table

    // Lookup table: code -> KeyValueStatus
    private static final KeyValueStatus[] code2status = new KeyValueStatus[0x100];

    static {
        Arrays.fill(code2status, UNKNOWN);
        for (KeyValueStatus keyValueStatus : values()) {
            if (keyValueStatus != UNKNOWN) {
                code2status[keyValueStatus.code()] = keyValueStatus;
            }
        }
    }

    public static KeyValueStatus valueOfLookupArray(final short code) {
        if (code >= 0 && code < code2status.length) {
            return code2status[code];
        } else {
            return UNKNOWN;
        }
    }

    //---------------------------------------------------------------------------------------------
    // Variant with unchecked array bounds, to see the effect of replacing bounds checking with a try/catch
    //
    // Benchmark shows no difference. We can assume the HotSpot does range check elimination
    // in valueOfLookupArray while it keeps it here.
    public static KeyValueStatus valueOfLookupArrayUnchecked(final short code) {
        try {
            return code2status[code];
        } catch(ArrayIndexOutOfBoundsException e) {
            return UNKNOWN;
        }
    }

    //---------------------------------------------------------------------------------------------
    // Using a big switch. Looking at the bytecode, the compiler generates a jump table.
    //
    // The benchmark shows it's bit slower than direct array lookup, with an outlier value.
    // Wild guess: large jump tables are transformed into a native hashmap, hence the little
    // increase in execution time, and the outlier is a collision resolution?

    public static KeyValueStatus valueOfBigSwitch(final short code) {
        switch (code) {
            case 0x00: return SUCCESS;
            case 0x01: return ERR_NOT_FOUND;
            case 0x02: return ERR_EXISTS;
            case 0x03: return ERR_TOO_BIG;
            case 0x04: return ERR_INVALID;
            case 0x05: return ERR_NOT_STORED;
            case 0x06: return ERR_DELTA_BADVAL;
            case 0x07: return ERR_NOT_MY_VBUCKET;
            case 0x08: return ERR_NO_BUCKET;
            case 0x1f: return ERR_AUTH_STALE;
            case 0x20: return ERR_AUTH_ERROR;
            case 0x21: return ERR_AUTH_CONTINUE;
            case 0x22: return ERR_RANGE;
            case 0x23: return ERR_ROLLBACK;
            case 0x24: return ERR_ACCESS;
            case 0x25: return ERR_NOT_INITIALIZED;
            case 0x81: return ERR_UNKNOWN_COMMAND;
            case 0x82: return ERR_NO_MEM;
            case 0x83: return ERR_NOT_SUPPORTED;
            case 0x84: return ERR_INTERNAL;
            case 0x85: return ERR_BUSY;
            case 0x86: return ERR_TEMP_FAIL;
            case 0xc0: return ERR_SUBDOC_PATH_NOT_FOUND;
            case 0xc1: return ERR_SUBDOC_PATH_MISMATCH;
            case 0xc2: return ERR_SUBDOC_PATH_INVALID;
            case 0xc3: return ERR_SUBDOC_PATH_TOO_BIG;
            case 0xc4: return ERR_SUBDOC_DOC_TOO_DEEP;
            case 0xc5: return ERR_SUBDOC_VALUE_CANTINSERT;
            case 0xc6: return ERR_SUBDOC_DOC_NOT_JSON;
            case 0xc7: return ERR_SUBDOC_NUM_RANGE;
            case 0xc8: return ERR_SUBDOC_DELTA_RANGE;
            case 0xc9: return ERR_SUBDOC_PATH_EXISTS;
            case 0xca: return ERR_SUBDOC_VALUE_TOO_DEEP;
            case 0xcb: return ERR_SUBDOC_INVALID_COMBO;
            case 0xcc: return ERR_SUBDOC_MULTI_PATH_FAILURE;

            default: return UNKNOWN;
        }
    }
}
