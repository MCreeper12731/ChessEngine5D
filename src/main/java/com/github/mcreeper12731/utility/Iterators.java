package com.github.mcreeper12731.utility;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.function.Supplier;

public class Iterators {

    public static <T, E extends Collection<T>> E consumeRemaining(Iterator<T> iterator, Supplier<E> collectionFactory) {

        E result = collectionFactory.get();
        while (iterator.hasNext())
            result.add(iterator.next());
        return result;
    }

    public static <T> List<T> consumeRemaining(Iterator<T> iterator) {
        return consumeRemaining(iterator, ArrayList::new);
    }
}
