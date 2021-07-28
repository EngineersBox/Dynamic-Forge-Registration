package com.engineersbox.expandedfusion.core.util.math;

import java.util.Random;

public final class MathUtils {
    private static final double DOUBLES_EQUAL_PRECISION = 0.000000001;
    private static final Random RANDOM = new Random();

    private MathUtils() {
        throw new IllegalAccessError("Utility class");
    }

    public static double clamp(final double value, final double lowerBound, final double upperBound) {
        return value < lowerBound ? lowerBound : Math.min(value, upperBound);
    }

    public static float clamp(final float value, final float lowerBound, final float upperBound) {
        return value < lowerBound ? lowerBound : Math.min(value, upperBound);
    }

    public static int clamp(final int value, final int lowerBound, final int upperBound) {
        return value < lowerBound ? lowerBound : Math.min(value, upperBound);
    }

    /**
     * Compare if two doubles are equal, using precision constant {@link #DOUBLES_EQUAL_PRECISION}.
     */
    public static boolean doublesEqual(final double a, final double b) {
        return doublesEqual(a, b, DOUBLES_EQUAL_PRECISION);
    }

    /**
     * Compare if two doubles are equal, within the given level of precision.
     *
     * @param precision Should be a small, positive number (like {@link #DOUBLES_EQUAL_PRECISION})
     */
    public static boolean doublesEqual(final double a, final double b, final double precision) {
        return Math.abs(b - a) < precision;
    }

    /**
     * Check if {@code value} is equal to {@code value} casted to an int.
     *
     * @param value The value
     * @return True if and only if {@code value} is equal to {@code (int) value}
     */
    public static boolean doubleIsInt(final double value) {
        return MathUtils.doublesEqual(value, (int) value);
    }

    /**
     * Compare if two floats are equal, using precision constant {@link #DOUBLES_EQUAL_PRECISION}.
     */
    public static boolean floatsEqual(final float a, final float b) {
        return floatsEqual(a, b, (float) DOUBLES_EQUAL_PRECISION);
    }

    /**
     * Check if {@code value} is equal to {@code value} casted to an int.
     *
     * @param value The value
     * @return True if and only if {@code value} is equal to {@code (int) value}
     */
    public static boolean floatIsInt(final float value) {
        return floatsEqual(value, (int) value);
    }

    /**
     * Compare if two floats are equal, within the given level of precision.
     *
     * @param precision Should be a small, positive number (like {@link #DOUBLES_EQUAL_PRECISION})
     */
    public static boolean floatsEqual(final float a, final float b, final float precision) {
        return Math.abs(b - a) < precision;
    }

    public static boolean inRangeExclusive(final double value, final double min, final double max) {
        return value < max && value > min;
    }

    public static boolean inRangeExclusive(final int value, final int min, final int max) {
        return value < max && value > min;
    }

    public static boolean inRangeInclusive(final double value, final double min, final double max) {
        return value <= max && value >= min;
    }

    public static boolean inRangeInclusive(final int value, final int min, final int max) {
        return value <= max && value >= min;
    }

    public static int min(final int a, final int b) {
        return Math.min(a, b);
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

    public static int min(final int a, final int b, final int c, final int d, final int... rest) {
        int min = min(a, b, c, d);
        for (int i : rest)
            if (i < min)
                min = i;
        return min;
    }

    public static int max(final int a, final int b) {
        return Math.max(a, b);
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

    public static int max(final int a, final int b, final int c, final int d, final int... rest) {
        int max = max(a, b, c, d);
        for (int i : rest)
            if (i > max)
                max = i;
        return max;
    }

    public static double nextGaussian(final double mean, final double deviation) {
        return deviation * RANDOM.nextGaussian() + mean;
    }

    public static double nextGaussian(final Random random, final double mean, final double deviation) {
        return deviation * random.nextGaussian() + mean;
    }

    public static int nextInt(final int bound) {
        return RANDOM.nextInt(bound);
    }

    public static int nextIntInclusive(final int min, final int max) {
        return RANDOM.nextInt(max - min + 1) + min;
    }

    public static int nextIntInclusive(final Random random, final int min, final int max) {
        return random.nextInt(max - min + 1) + min;
    }

    public static boolean tryPercentage(final double percent) {
        return RANDOM.nextDouble() < percent;
    }

    public static boolean tryPercentage(final Random random, final double percent) {
        return random.nextDouble() < percent;
    }
}