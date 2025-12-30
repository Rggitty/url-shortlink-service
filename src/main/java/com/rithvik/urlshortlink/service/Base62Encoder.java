package com.rithvik.urlshortlink.service;

public final class Base62Encoder {

    private static final String ALPHABET = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final int BASE = ALPHABET.length();

    private Base62Encoder() {}

    public static String encode(long value) {
        if (value < 0) {
            throw new IllegalArgumentException("value must be non-negative");
        }
        if (value == 0) {
            return "0";
        }

        StringBuilder sb = new StringBuilder();
        long v = value;
        while (v > 0) {
            int r = (int) (v % BASE);
            sb.append(ALPHABET.charAt(r));
            v /= BASE;
        }
        return sb.reverse().toString();
    }
}
