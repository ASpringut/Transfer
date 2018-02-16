package com.polarizedraven.transfer;
import com.polarizedraven.transfer.R;
/**
 * Created by aaron on 2/14/18.
 */

public enum Line {
    ONE(0xFFEE352E),
    TWO(0xFFEE352E),
    THREE(0xFFEE352E),

    FOUR(0xFF00933C),
    FIVE(0xFF00933C),
    SIX(0xFF00933C),

    SEVEN(0xFFB933AD),
    SHUTTLE(0xFF808183),

    A(0xFF2850AD),
    C(0xFF2850AD),
    E(0xFF2850AD),

    B(0xFFFF6319),
    D(0xFFFF6319),
    F(0xFFFF6319),
    M(0xFFFF6319),

    J(0xFF000963),
    Z(0xFF000963),

    G(0xFF6CBE45),

    N(0xFFFCCC0A),
    Q(0xFFFCCC0A),
    R(0xFFFCCC0A),
    W(0xFFFCCC0A),

    L(0xFFA7A9AC);

    private int c;

    Line(int color) {
        c = color;
    }

    public int getColor(){
        return c;
    }
}
