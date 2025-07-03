package de.team33.sudoku;

public class Hint {
    private final Choice m_Choice;
    private final Number m_Number;

    public Hint(final Choice c, final Number n) {
        this.m_Choice = c;
        this.m_Number = n;
    }

    public final Choice getChoice() {
        return m_Choice;
    }

    public final Number getNumber() {
        return m_Number;
    }
}
