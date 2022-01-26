package io.kontur.keycloak.provider;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

@RequiredArgsConstructor
class ListUtilTest {

    private static Stream<Arguments> startingFromIndexArgs() {
        return Stream.of(
            //empty result
            Arguments.of(0, 0, 0, null, null), //empty source
            Arguments.of(20, 20, 0, null, null), //fromIndex = size
            Arguments.of(20, 25, 0, null, null), //fromIndex > size

            Arguments.of(20, -1, 20, 1, 20), //fromIndex < 0 --- ignored (return source list)
            Arguments.of(20, 0, 20, 1, 20), //fromIndex = 0 --- ignored (return source list)

            Arguments.of(20, 10, 10, 11, 20) //fromIndex < size --- filtering is applied
        );
    }

    private static Stream<Arguments> beforeIndexArgs() {
        return Stream.of(
            //empty result
            Arguments.of(0, 0, 0, null, null), //empty source
            Arguments.of(20, 0, 0, null, null), //beforeIndex = 0

            Arguments.of(20, -1, 20, 1, 20), //beforeIndex < 0 --- ignored (return source list)
            Arguments.of(20, 20, 20, 1, 20), //beforeIndex = size --- ignored (return source list)
            Arguments.of(20, 25, 20, 1, 20), //beforeIndex > size --- ignored (return source list)

            Arguments.of(20, 10, 10, 1, 10) //beforeIndex < size --- filtering is applied
        );
    }

    @Test
    void getFirstEntriesTest() {
        List<Integer> source = createList(100);
        List<Integer> result = DatabaseUserStorageProvider.ListUtil.getEntriesFromList(source,
            10, 20);
        testFilter(result, 20, 11, 30);
    }

    @ParameterizedTest
    @MethodSource(value = "startingFromIndexArgs")
    void getEntriesStartingFromIndexTest(int sourceSize, int fromIndex, int expectedSize,
                                         Integer minExpected, Integer maxExpected) {
        List<Integer> source = createList(sourceSize);
        List<Integer> result =
            DatabaseUserStorageProvider.ListUtil.getEntriesStartingFromIndex(source, fromIndex);

        testFilter(result, expectedSize, minExpected, maxExpected);
    }

    @ParameterizedTest
    @MethodSource(value = "beforeIndexArgs")
    void getEntriesBeforeIndexTest(int sourceSize, int beforeIndex, int expectedSize,
                                   Integer minExpected, Integer maxExpected) {
        List<Integer> source = createList(sourceSize);
        List<Integer> result =
            DatabaseUserStorageProvider.ListUtil.getEntriesBeforeIndex(source, beforeIndex);

        testFilter(result, expectedSize, minExpected, maxExpected);
    }

    private void testFilter(List<Integer> result, int expectedSize, Integer minExpected,
                            Integer maxExpected) {
        assertEquals(expectedSize, result.size());
        if (expectedSize != 0) {
            assertEquals(minExpected, result.get(0));
            assertEquals(maxExpected, result.get(result.size() - 1));
        }
    }

    private List<Integer> createList(int size) {
        List<Integer> list = new ArrayList<>();
        for (int i = 1; i <= size; i++) {
            list.add(i);
        }
        return list;
    }
}