package net.bluxte.experiments.couchbase_keyvalue;

public class BigSwitchGenerator {
    public static void main(String[] args) {
        for (KeyValueStatus v: KeyValueStatus.values()) {
            System.out.printf("            case 0x%02x: return %s;%n", v.code(), v.toString());
        }
    }
}
