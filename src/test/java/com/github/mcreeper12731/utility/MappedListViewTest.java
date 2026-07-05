package com.github.mcreeper12731.utility;

import com.github.mcreeper12731.utility.listviews.MappedListView;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MappedListViewTest {

    @Test
    public void elementsCorrectWhenMapperSupplied() {

        List<String> strings = List.of("0", "1", "2", "3", "4", "5", "6", "7", "8", "9");

        List<Integer> mappedList = new MappedListView<>(strings, Integer::valueOf);

        assertEquals(strings.size(), mappedList.size());
        mappedList.forEach(element -> assertInstanceOf(Integer.class, element));
    }
}