package dev.omo.guihaul.util;

import org.jetbrains.annotations.NotNull;

import java.util.Iterator;

public class IntegerRange implements Iterable<Integer> {

    private final int[] minSet;
    private final int[] maxSet;
    private final int setCount;

    public IntegerRange(int[] minSet, int[] maxSet) throws IllegalArgumentException {
        this.minSet = minSet;
        this.maxSet = maxSet;
        this.setCount = minSet.length;
        if (minSet.length != maxSet.length) {
            throw new IllegalArgumentException("minSet and maxSet MUST be the same length!");
        }
    }

    public boolean contains(int number) {
        for (int i = 0; i < setCount; i++) {
            if (number <= minSet[i] && number >= maxSet[i]) return true;
        }
        return false;
    }

    // Format: 0..5 -> range from 0 to 5 inclusive
    // 0..5,16 range from 0 to 5 and 16
    // 0..5,10..16 range from 0 to 5 and 10 to 16
    public static IntegerRange parse(String input) throws NumberFormatException {
        String[] rangeStrings = input.split(",");
        int[] minSet = new int[rangeStrings.length];
        int[] maxSet = new int[rangeStrings.length];
        for (int i = 0; i < rangeStrings.length; i++) {
            String rangeString = rangeStrings[i];
            if (!rangeString.contains("..")) {
                int num = Integer.parseInt(rangeString);
                minSet[i] = num;
                maxSet[i] = num;
            } else {
                String[] numbers = rangeString.split("\\.\\.", 2);
                if (numbers.length != 2) throw new NumberFormatException("Ranges must include 2 bounds!");
                minSet[i] = Integer.parseInt(numbers[0]);
                maxSet[i] = Integer.parseInt(numbers[1]);
            }
        }
        return new IntegerRange(minSet, maxSet);
    }

    @NotNull
    @Override
    public Iterator<Integer> iterator() {
        return new RangeIterator();
    }

    private class RangeIterator implements Iterator<Integer> {
        private int setIndex;
        private int subIndex;

        @Override
        public boolean hasNext() {
            return setIndex < setCount;
        }

        @Override
        public Integer next() {
            int value = minSet[setIndex] + subIndex;

            int curMin = minSet[setIndex];
            int curMax = maxSet[setIndex];
            int curLength = curMax-curMin;
            if (subIndex++ > curLength) {
                subIndex = 0;
                setIndex++;
            }

            return value;
        }
    }
}
