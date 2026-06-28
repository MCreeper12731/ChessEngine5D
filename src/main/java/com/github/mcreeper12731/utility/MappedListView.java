package com.github.mcreeper12731.utility;

import java.util.AbstractList;
import java.util.List;
import java.util.function.Function;

public class MappedListView<V, E> extends AbstractList<E> {

    private final List<V> list;
    private final Function<V, E> mapper;

    public MappedListView(List<V> list, Function<V, E> mapper) {
        this.list = list;
        this.mapper = mapper;
    }

    @Override
    public E get(int index) {
        return this.mapper.apply(this.list.get(index));
    }

    @Override
    public int size() {
        return this.list.size();
    }
}
