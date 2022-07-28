package org.summer.card;

import org.apache.commons.lang3.Validate;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class TexasHandsEvaluator {

    /**
     *
     * @param hands 手牌 （自己的牌加上公牌，计算自己持有的最大价值）
     * @return 分数
     */
    public static int evaluate(List<Card> hands) {
        Validate.isTrue(hands.size() >= 5 && hands.size() <= 7, "hands length must between %s and %s", 5, 7);
        List<Card> handsCopy = new ArrayList<>(hands);
        handsCopy.sort(CardComparator.INSTANCE);
        //用于判断是否有同花
        Map<CardSuite, List<Card>> suiteCountMap = new HashMap<>(4);
        //先判断是否有同花顺！！
        for (var card : handsCopy) {
            List<Card> suiteCards = suiteCountMap.computeIfAbsent(card.getSuite(), ignored -> new ArrayList<>());
            suiteCards.add(card);
        }
        Optional<CardSuite> flushOptional = suiteCountMap.keySet().stream().filter(suite -> suiteCountMap.get(suite).size() >= 5).findFirst();
        if (flushOptional.isPresent()) {
            List<Card> cards = suiteCountMap.get(flushOptional.get());
            List<Card> straight = getMaxStraight(cards);
            if (!straight.isEmpty()) {
                //同花顺，计算分值
                if (straight.get(0).getNumber() == CardNumber.ACE) {
                    return TexasRank.STRAIGHT_FLUSH.getRank() + 1;
                } else {
                    if (straight.get(4).getNumber() == CardNumber.ACE) {
                        return TexasRank.ROYAL_FLUSH.getRank();
                    } else {
                        return TexasRank.STRAIGHT_FLUSH.getRank() + straight.get(0).getNumber().value();
                    }
                }
            }
        }
        //已知同花是否存在，还需要统计四条，葫芦
        CardNumber fourOfAKind = null;
        List<CardNumber> threeOfAKinds = new ArrayList<>(2);
        List<CardNumber> pairs = new ArrayList<>(4);
        Map<CardNumber, Integer> numberCountMap = new HashMap<>();

        for (var card: handsCopy) {
            Integer count = numberCountMap.getOrDefault(card.getNumber(), 0);
            if (count == 0) {
                numberCountMap.put(card.getNumber(), 1);
            } else {
                numberCountMap.put(card.getNumber(), ++count);
            }
            if (count == 2) {
                pairs.add(card.getNumber());
            } else if (count == 3) {
                threeOfAKinds.add(card.getNumber());
                pairs.remove(card.getNumber());
            } else if (count == 4) {
                fourOfAKind = card.getNumber();
                threeOfAKinds.remove(card.getNumber());
            }
        }
        Set<CardNumber> numbers = new HashSet<>();
        List<Card> cards = new ArrayList<>();
        for (var card : handsCopy) {
            if (numbers.add(card.getNumber())) {
                cards.add(card);
            }
        }
        if (fourOfAKind != null) {
            return TexasRank.FOUR_OF_A_KIND.getRank() + fourOfAKind.value() * 4 + getHighCards(cards, Set.of(fourOfAKind), 1).get(0).value();
        }
        if (threeOfAKinds.size() > 0 && pairs.size() > 0) {
            return TexasRank.FULL_HOUSE.getRank() + threeOfAKinds.get(threeOfAKinds.size() - 1).value() * 3 + pairs.get(pairs.size() - 1).value() * 2;
        }
        if (flushOptional.isPresent()) {
            List<Card> suiteCards = suiteCountMap.get(flushOptional.get());
            return TexasRank.FLUSH.getRank()
                    + suiteCards.subList(suiteCards.size() - 5, suiteCards.size())
                        .stream().mapToInt(card -> card.getNumber().value()).sum();
        }
        //顺子
        List<Card> maxStraight = getMaxStraight(cards);
        if (!maxStraight.isEmpty()) {
            if (maxStraight.get(0).getNumber() == CardNumber.ACE) {
                return TexasRank.STRAIGHT.getRank() + 1;
            } else {
                return TexasRank.STRAIGHT.getRank() + maxStraight.get(0).getNumber().value();
            }
        }
        //三条，还要把两高张算进去
        if (threeOfAKinds.size() > 0) {
            CardNumber threeNumber = threeOfAKinds.get(threeOfAKinds.size() - 1);
            List<CardNumber> twoHighCard = getHighCards(cards, Set.of(threeNumber), 2);
            return TexasRank.THREE_OF_A_KIND.getRank() + threeNumber.value() * 3 + twoHighCard.stream().mapToInt(CardNumber::value).sum();
        }
        if (pairs.size() >= 2) {
            List<CardNumber> twoPairs = pairs.subList(pairs.size() - 2, pairs.size());
            List<CardNumber> highCards = getHighCards(cards, new HashSet<>(twoPairs), 1);
            return TexasRank.TWO_PAIR.getRank() + twoPairs.stream().mapToInt(CardNumber::value).sum() * 2 + highCards.get(0).value();
        }
        if (pairs.size() == 1) {
            List<CardNumber> highCards = getHighCards(cards, Set.of(pairs.get(0)), 3);
            return TexasRank.ONE_PAIR.getRank() + pairs.get(0).value() * 2 + highCards.stream().mapToInt(CardNumber::value).sum();
        }
        //五高张
        return TexasRank.HIGH_CARD.getRank() + getHighCards(cards, Collections.emptySet(), 5).stream().mapToInt(CardNumber::value).sum();
    }

    private static List<CardNumber> getHighCards(List<Card> cards, Set<CardNumber> except, int size) {
        List<CardNumber> result = new ArrayList<>(2);
        for (int i = cards.size() - 1; i >= 0 && result.size() < size; i--) {
            if (!except.contains(cards.get(i).getNumber())) {
                result.add(cards.get(i).getNumber());
            }
        }
        return result;
    }

    /**
     * @param cards 至少5张牌，不含相同数字，且从小到达排好序
     */
    private static List<Card> getMaxStraight(List<Card> cards) {
        List<Card> straight = new ArrayList<>();
        //特殊判断A2345
        if (cards.get(0).getNumber() == CardNumber.TWO
                && cards.get(1).getNumber() == CardNumber.THREE
                && cards.get(2).getNumber() == CardNumber.FOUR
                && cards.get(3).getNumber() == CardNumber.FINE
                && cards.get(cards.size() - 1).getNumber() == CardNumber.ACE) {
            straight.addAll(List.of(cards.get(cards.size() - 1), cards.get(0), cards.get(1), cards.get(2), cards.get(3)));
        }

        int counter = 0;
        int straightEnd = -1;
        Card prev = null;
        for (int i = 0; i < cards.size(); i++) {
            Card card = cards.get(i);
            if (prev == null) {
                counter++;
            } else {
                if (card.getNumber().value() - prev.getNumber().value() == 1) {
                    counter++;
                    if (counter >= 5) {
                        straightEnd = i;
                    }
                } else {
                    counter = 1;
                }
            }
            prev = card;
        }
        if (straightEnd != -1) {
            straight.clear();
            straight.addAll(cards.subList(straightEnd - 4, straightEnd + 1));
        }
        return straight;
    }


    public static void main(String[] args) {
        for (int j = 0; j < 10000; j++) {
            List<Card> cards = Cards.newRandomCardsWithoutJoker();
            //随机取7张
            List<Card> target = new ArrayList<>();
            for (int i = 0; i < 7; i++) {
                Card remove = cards.remove(ThreadLocalRandom.current().nextInt(0, cards.size()));
                target.add(remove);
            }
            target.sort(CardComparator.INSTANCE);
            int value = TexasHandsEvaluator.evaluate(target);
            if (value > 30000) {
                System.out.println("cards: " + target);
                System.out.println("value: " + value);
            }
        }
    }


}
