package org.summer.card;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * 花色
 */
public enum CardSuite {
    DIAMOND(1),
    CLUB(2),
    HEART(3),
    SPADE(4);


    private final int value;

    CardSuite(int rank) {
        this.value = rank;
    }

    public int getValue() {
        return value;
    }

    private static Map<Integer, CardSuite> indexMap = new HashMap<>();

    static {
        for (var suite : values()) {
            indexMap.put(suite.getValue(), suite);
        }
    }

    public static Optional<CardSuite> valueOf(int suite) {
        return Optional.ofNullable(indexMap.get(suite));
    }

}
