package ru.topjava.basejava.streams;

import java.util.LinkedList;
import java.util.List;

public class StreamAccumulator {
    private final List<Integer> odds = new LinkedList<>();
    private final List<Integer> evens = new LinkedList<>();
    private long sum = 0;

    public List<Integer> getOdds() {
        return odds;
    }

    public List<Integer> getEvens() {
        return evens;
    }

    public long getSum() {
        return sum;
    }

    public void add(Integer value) {
        if (value % 2 == 0) {
            evens.add(value);
        } else {
            odds.add(value);
        }
        sum += value;
    }

    public void addAll(StreamAccumulator r) {
        odds.addAll(r.getOdds());
        evens.addAll(r.getEvens());
        sum += r.getSum();
    }
}
