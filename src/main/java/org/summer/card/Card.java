package org.summer.card;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@AllArgsConstructor
@NoArgsConstructor
@Data
public class Card {
    private CardSuite suite;
    private CardNumber number;
}
