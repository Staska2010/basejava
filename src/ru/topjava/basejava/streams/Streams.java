package ru.topjava.basejava.streams;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Streams {

    public static void main(String[] args) {
        System.out.println(minValue(new int[]{1, 2, 3, 3, 2, 3}));
        System.out.println(minValue(new int[]{9, 8}));
        System.out.println(oddOrEven(Arrays.asList(1, 2, 3, 4, 5, 6, 7)));
        System.out.println(oddOrEvenOptional(Arrays.asList(1, 2, 3, 4, 5, 6, 7)));
    }

    private static int minValue(int[] values) {
        return Arrays.stream(values)
                .distinct()
                .sorted()
                .reduce((x, y) -> x * 10 + y)
                .orElse(0);
    }

    private static List<Integer> oddOrEven(List<Integer> integers) {
        int sum = integers.stream()
                .reduce(Integer::sum)
                .orElse(0);
        return integers.stream()
                .filter(x -> x % 2 != sum % 2)
                .collect(Collectors.toList()); //Time complexity O(N+N)
    }

    private static List<Integer> oddOrEvenOptional(List<Integer> integers) {
        return integers.stream().collect(new TailoredCollector()); //Time complexity O(N), Space O(N)
    }
}