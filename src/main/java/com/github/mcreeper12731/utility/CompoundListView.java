package com.github.mcreeper12731.utility;

import java.util.AbstractList;
import java.util.List;

public class CompoundListView<E> extends AbstractList<E> {

    private final List<E> list1;
    private final List<E> list2;

    public CompoundListView(List<E> list1, List<E> list2) {
        this.list1 = list1;
        this.list2 = list2;
    }

    @Override
    public E get(int index) {
        if (index < 0 || index >= this.size()) throw new IndexOutOfBoundsException();
        if (index >= list1.size()) return list2.get(index - list1.size());
        return list1.get(index);
    }

    @Override
    public int size() {
        return list1.size() + list2.size();
    }
}
