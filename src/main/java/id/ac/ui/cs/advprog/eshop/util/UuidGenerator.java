package id.ac.ui.cs.advprog.eshop.util;

import java.util.UUID;

public class UuidGenerator {
    public static String generateId() {
        return UUID.randomUUID().toString();
    }
}