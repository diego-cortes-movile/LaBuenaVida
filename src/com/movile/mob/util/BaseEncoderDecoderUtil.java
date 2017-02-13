package com.movile.mob.util;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang.NotImplementedException;

public class BaseEncoderDecoderUtil {
    private static final char[] MAPPING_ARRAY = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v',
            'w', 'x', 'y', 'z', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y',
            'Z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};

    public static final int MAX_MAPPING_BASE = MAPPING_ARRAY.length;

    /**
     * Just perform a base conversion
     * Turn the source number (base ten) to another base
     */
    public static String encode(final Long value, final int base) {
        return encode(value, base, 0);
    }

    /**
     * Just perform a base conversion
     * Turn the source number (base ten) to another base with
     */
    public static String encode(final Long value, final int base, final int salt) {

        if (base < 0 && base > MAX_MAPPING_BASE) {
            throw new NotImplementedException("Base must be between 0 and " + MAX_MAPPING_BASE);
        }

        List<Integer> indexes = new LinkedList<Integer>();

        long subject = value + salt;
        int mod;

        while (subject > 0) {

            mod = (int) (subject % base);
            indexes.add(mod);
            subject = subject / base;
        }

        Collections.reverse(indexes);

        StringBuilder builder = new StringBuilder();

        for (Integer index : indexes) {

            builder.append(MAPPING_ARRAY[index]);
        }

        return builder.toString();
    }

    /**
     * Perform base conversion back to base ten
     */

    public static Long decode(final String value, final int base) {
        return decode(value, base, 0);
    }

    /**
     * Perform base conversion back to base ten with salt
     */
    public static Long decode(final String value, final int base, final int salt) {

        if (base < 0 && base > MAX_MAPPING_BASE) {
            throw new NotImplementedException("Base must be between 0 and " + MAX_MAPPING_BASE);
        }

        Long id = 0L;

        char[] indexes = value.toCharArray();

        for (int i = indexes.length - 1; i >= 0; i--) {

            double evaluatedPow = Math.pow(base, getPow(i, indexes.length));
            Long increment = Double.valueOf(evaluatedPow).longValue();
            increment *= indexOf(indexes[i]);
            id += increment;
        }

        return id - salt;
    }

    private static int getPow(int position, int arrayLength) {

        return Math.abs(position - (arrayLength - 1));
    }

    private static int indexOf(char c) {

        for (int i = 0; i < MAX_MAPPING_BASE; i++) {

            if (MAPPING_ARRAY[i] == c) {
                return i;
            }
        }

        throw new IllegalArgumentException("Invalid mapped char: " + c);
    }

    public static void main(String[] args) {

        final Long resourceId = 138l;
        System.out.println("Source: " + resourceId);

        String gen = encode(resourceId, MAX_MAPPING_BASE, 0);
        System.out.println("Shortened: " + gen);

        Long reverted = decode(gen, MAX_MAPPING_BASE, 0);
        System.out.println("Reconverted: " + reverted);
    }
}
