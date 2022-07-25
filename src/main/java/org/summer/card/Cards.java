package org.summer.card;

import java.util.*;

/**
 * 提供扑克通用的逻辑
 */
public class Cards {

    private static final Map<CardSuite, Map<CardNumber, Card>> cardIndex = new HashMap<>();
    private static final List<Card> cards = new ArrayList<>();
    private static final List<Card> cardsWithoutJoker = new ArrayList<>();

    static {
        for (CardSuite suite : CardSuite.values()) {
            Map<CardNumber, Card> map = cardIndex.computeIfAbsent(suite, ignored -> new HashMap<>());
            for (CardNumber number : CardNumber.values()) {
                if (number == CardNumber.JOKER) {
                    continue;
                }
                Card card = new Card(suite, number);
                map.put(number, card);
                cards.add(card);
                cardsWithoutJoker.add(card);
            }
        }
        Card littleJoker = new Card(CardSuite.HEART, CardNumber.JOKER);
        cardIndex.get(CardSuite.HEART).put(CardNumber.JOKER, littleJoker);
        cards.add(littleJoker);
        Card bigJoker = new Card(CardSuite.SPADE, CardNumber.JOKER);
        cardIndex.get(CardSuite.SPADE).put(CardNumber.JOKER, bigJoker);
        cards.add(bigJoker);
    }

    public static List<Card> newCards() {
        return new ArrayList<>(cards);
    }

    public static List<Card> newRandomCards() {
        ArrayList<Card> cards = new ArrayList<>(Cards.cards);
        Collections.shuffle(cards);
        return cards;
    }

    public static List<Card> newCardsWithoutJoker() {
        return new ArrayList<>(cardsWithoutJoker);
    }

    public static List<Card> newRandomCardsWithoutJoker() {
        ArrayList<Card> cards = new ArrayList<>(Cards.cardsWithoutJoker);
        Collections.shuffle(cards);
        return cards;
    }

}
