package ru.topjava.basejava.streams;

import java.util.EnumSet;
import java.util.List;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;

public class TailoredCollector implements Collector<Integer, StreamAccumulator, List<Integer>> {
    @Override
    public Supplier<StreamAccumulator> supplier() {
        return StreamAccumulator::new;
    }

    @Override
    public BiConsumer<StreamAccumulator, Integer> accumulator() {
        return StreamAccumulator::add;
    }

    @Override
    public BinaryOperator<StreamAccumulator> combiner() {
        return (l, r) -> {
            l.addAll(r);
            return l;
        };
    }

    @Override
    public Function<StreamAccumulator, List<Integer>> finisher() {
        return s -> s.getSum() % 2 == 0 ? s.getOdds() : s.getEvens();
    }

    @Override
    public Set<Characteristics> characteristics() {
        return EnumSet.of(Characteristics.CONCURRENT);
    }
}
