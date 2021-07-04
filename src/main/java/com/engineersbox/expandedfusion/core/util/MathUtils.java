package com.engineersbox.expandedfusion.core.util;

import java.util.Random;

public final class MathUtils {
    private static final double DOUBLES_EQUAL_PRECISION = 0.000000001;
    private static final Random RANDOM = new Random();

    private MathUtils() {
        throw new IllegalAccessError("Utility class");
    }

    public static double clamp(double value, double lowerBound, double upperBound) {
        return value < lowerBound ? lowerBound : value > upperBound ? upperBound : value;
    }

    public static float clamp(float value, float lowerBound, float upperBound) {
        return value < lowerBound ? lowerBound : value > upperBound ? upperBound : value;
    }

    public static int clamp(int value, int lowerBound, int upperBound) {
        return value < lowerBound ? lowerBound : value > upperBound ? upperBound : value;
    }

    /**
     * Compare if two doubles are equal, using precision constant {@link #DOUBLES_EQUAL_PRECISION}.
     */
    public static boolean doublesEqual(double a, double b) {
        return doublesEqual(a, b, DOUBLES_EQUAL_PRECISION);
    }

    /**
     * Compare if two doubles are equal, within the given level of precision.
     *
     * @param precision Should be a small, positive number (like {@link #DOUBLES_EQUAL_PRECISION})
     */
    public static boolean doublesEqual(double a, double b, double precision) {
        return Math.abs(b - a) < precision;
    }

    /**
     * Check if {@code value} is equal to {@code value} casted to an int.
     *
     * @param value The value
     * @return True if and only if {@code value} is equal to {@code (int) value}
     */
    public static boolean doubleIsInt(double value) {
        return MathUtils.doublesEqual(value, (int) value);
    }

    /**
     * Compare if two floats are equal, using precision constant {@link #DOUBLES_EQUAL_PRECISION}.
     */
    public static boolean floatsEqual(float a, float b) {
        return floatsEqual(a, b, (float) DOUBLES_EQUAL_PRECISION);
    }

    /**
     * Check if {@code value} is equal to {@code value} casted to an int.
     *
     * @param value The value
     * @return True if and only if {@code value} is equal to {@code (int) value}
     */
    public static boolean floatIsInt(float value) {
        return floatsEqual(value, (int) value);
    }

    /**
     * Compare if two floats are equal, within the given level of precision.
     *
     * @param precision Should be a small, positive number (like {@link #DOUBLES_EQUAL_PRECISION})
     */
    public static boolean floatsEqual(float a, float b, float precision) {
        return Math.abs(b - a) < precision;
    }

    public static boolean inRangeExclusive(double value, double min, double max) {
        return value < max && value > min;
    }

    public static boolean inRangeExclusive(int value, int min, int max) {
        return value < max && value > min;
    }

    public static boolean inRangeInclusive(double value, double min, double max) {
        return value <= max && value >= min;
    }

    public static boolean inRangeInclusive(int value, int min, int max) {
        return value <= max && value >= min;
    }

    public static int min(final int a, final int b) {
        return a < b ? a : b;
    }

    public static int min(int a, final int b, final int c) {
        if (b < a) a = b;
        if (c < a) a = c;
        return a;
    }

    public static int min(int a, final int b, final int c, final int d) {
        if (b < a) a = b;
        if (c < a) a = c;
        if (d < a) a = d;
        return a;
    }

    public static int min(int a, final int b, final int c, final int d, int... rest) {
        int min = min(a, b, c, d);
        for (int i : rest)
            if (i < min)
                min = i;
        return min;
    }

    public static int max(final int a, final int b) {
        return a > b ? a : b;
    }

    public static int max(int a, final int b, final int c) {
        if (b > a) a = b;
        if (c > a) a = c;
        return a;
    }

    public static int max(int a, final int b, final int c, final int d) {
        if (b > a) a = b;
        if (c > a) a = c;
        if (d > a) a = d;
        return a;
    }

    public static int max(int a, final int b, final int c, final int d, int... rest) {
        int max = max(a, b, c, d);
        for (int i : rest)
            if (i > max)
                max = i;
        return max;
    }

    public static double nextGaussian(double mean, double deviation) {
        return deviation * RANDOM.nextGaussian() + mean;
    }

    public static double nextGaussian(Random random, double mean, double deviation) {
        return deviation * random.nextGaussian() + mean;
    }

    public static int nextInt(int bound) {
        return RANDOM.nextInt(bound);
    }

    public static int nextIntInclusive(int min, int max) {
        return RANDOM.nextInt(max - min + 1) + min;
    }

    public static int nextIntInclusive(Random random, int min, int max) {
        return random.nextInt(max - min + 1) + min;
    }

    public static boolean tryPercentage(double percent) {
        return RANDOM.nextDouble() < percent;
    }

    public static boolean tryPercentage(Random random, double percent) {
        return random.nextDouble() < percent;
    }
}