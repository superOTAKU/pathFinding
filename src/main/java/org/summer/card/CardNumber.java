package org.summer.card;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * 数字
 */
public enum CardNumber {
    TWO(2),
    THREE(3),
    FOUR(4),
    FINE(5),
    SIX(6),
    SEVEN(7),
    EIGHT(8),
    NINE(9),
    TEN(10),
    JACK(11),
    QUEEN(12),
    KING(13),
    ACE(14),
    JOKER(15)
    ;
    private final int value;

    CardNumber(int value) {
        this.value = value;
    }

    public int value() {
        return value;
    }

    private static final Map<Integer, CardNumber> indexMap = new HashMap<>();

    static {
        for (var cardNumber : values()) {
            indexMap.put(cardNumber.value(), cardNumber);
        }
    }

    public static Optional<CardNumber> valueOf(int number) {
        return Optional.ofNullable(indexMap.get(number));
    }

}
