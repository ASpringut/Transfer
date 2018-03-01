package com.polarizedraven.transfer;
/**
 * Created by aaron on 2/14/18.
 */


public enum Line {
    ONE(com.polarizedraven.transfer.R.style.Theme123),
    TWO(com.polarizedraven.transfer.R.style.Theme123),
    THREE(com.polarizedraven.transfer.R.style.Theme123),

    FOUR(com.polarizedraven.transfer.R.style.Theme456),
    FIVE(com.polarizedraven.transfer.R.style.Theme456),
    SIX(com.polarizedraven.transfer.R.style.Theme456),

    SEVEN(com.polarizedraven.transfer.R.style.Theme7),

    SHUTTLE(com.polarizedraven.transfer.R.style.ThemeS),

    A(com.polarizedraven.transfer.R.style.ThemeACE),
    C(com.polarizedraven.transfer.R.style.ThemeACE),
    E(com.polarizedraven.transfer.R.style.ThemeACE),

    B(com.polarizedraven.transfer.R.style.ThemeBDFM),
    D(com.polarizedraven.transfer.R.style.ThemeBDFM),
    F(com.polarizedraven.transfer.R.style.ThemeBDFM),
    M(com.polarizedraven.transfer.R.style.ThemeBDFM),

    J(com.polarizedraven.transfer.R.style.ThemeJZ),
    Z(com.polarizedraven.transfer.R.style.ThemeJZ),

    G(com.polarizedraven.transfer.R.style.ThemeG),

    N(com.polarizedraven.transfer.R.style.ThemeNQRW),
    Q(com.polarizedraven.transfer.R.style.ThemeNQRW),
    R(com.polarizedraven.transfer.R.style.ThemeNQRW),
    W(com.polarizedraven.transfer.R.style.ThemeNQRW),

    L(com.polarizedraven.transfer.R.style.ThemeL);

    private int theme;

    Line(int theme) {
        this.theme = theme;
    }

    public int getTheme(){
        return theme;
    }
}
