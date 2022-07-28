package org.summer.card;

/**
 *  德州扑克牌型
 */
public enum TexasRank {
    ROYAL_FLUSH(100000),
    STRAIGHT_FLUSH(90000),
    FOUR_OF_A_KIND(80000),
    FULL_HOUSE(70000),
    FLUSH(60000),
    STRAIGHT(50000),
    THREE_OF_A_KIND(40000),
    TWO_PAIR(30000),
    ONE_PAIR(20000),
    HIGH_CARD(10000),
    ;

    //分数
    //不同牌型的分数差放大，相同牌型比数字
    private final int rank;

    TexasRank(int rank) {
        this.rank = rank;
    }

    public int getRank() {
        return rank;
    }
}
