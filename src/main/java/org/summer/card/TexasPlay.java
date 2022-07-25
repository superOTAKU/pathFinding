package org.summer.card;

import java.util.List;

/**
 * 德州扑克玩法
 *
 * 游戏状态机，描述一局游戏的状态轮转
 *
 */
public class TexasPlay {
    //通用参数
    public static final int MIN_PLAYER = 1;
    public static final int MAX_PLAYER = 10;

    public enum PlayState {
        PENDING_START,
    }

    private final List<Card> cards = Cards.newRandomCardsWithoutJoker();


}
