package com.github.mcreeper12731.utility;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Iterators {

    public static <T> List<T> consumeRemaining(Iterator<T> iterator) {
        List<T> result = new ArrayList<>();
        while (iterator.hasNext())
            result.add(iterator.next());
        return result;
    }
}
