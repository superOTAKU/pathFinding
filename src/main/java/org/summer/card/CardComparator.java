package org.summer.card;

import java.util.Comparator;

public class CardComparator implements Comparator<Card> {

    public static CardComparator INSTANCE = new CardComparator();

    @Override
    public int compare(Card card1, Card card2) {
        int numberCompare = card1.getNumber().value() - card2.getNumber().value();
        if (numberCompare == 0) {
            return card1.getSuite().getValue() - card2.getSuite().getValue();
        } else {
            return numberCompare;
        }
    }
}
