package com.github.mcreeper12731.utility;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Permutations {

    public static <T> List<List<T>> of(List<T> list) {
        List<List<T>> result = new ArrayList<>();
        if (list.isEmpty()) {
            result.add(Collections.emptyList());
            return result;
        }
        heapPermutation(new ArrayList<>(list), list.size(), result);
        return result;
    }

    private static <T> void heapPermutation(List<T> a, int size, List<List<T>> out) {
        if (size == 1) {
            out.add(new ArrayList<>(a));
            return;
        }
        for (int i = 0; i < size; i++) {
            heapPermutation(a, size - 1, out);
            int j = (size % 2 == 1) ? 0 : i;
            Collections.swap(a, j, size - 1);
        }
    }
}
