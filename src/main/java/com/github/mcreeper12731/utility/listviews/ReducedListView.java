package com.github.mcreeper12731.utility.listviews;

import java.util.AbstractList;
import java.util.List;

public class ReducedListView<E> extends AbstractList<E> {

    private final List<E> list;
    private final int startIndex;
    private final int endIndex;

    /**
     * Creates a list view with inclusive lower bound and exclusive upper bound
     * @param list the original list
     * @param startIndex lower bound, defined by specifying an index from the original list
     * @param endIndex upper bound, defined by specifying an index from the original list
     */
    public ReducedListView(List<E> list, int startIndex, int endIndex) {
        this.list = list;
        this.startIndex = startIndex;
        this.endIndex = endIndex;
    }

    @Override
    public E get(int index) {
        if (index < 0 || index >= endIndex - startIndex) throw new IndexOutOfBoundsException();
        index += startIndex;
        return list.get(index);
    }

    @Override
    public int size() {
        return endIndex - startIndex;
    }
}
