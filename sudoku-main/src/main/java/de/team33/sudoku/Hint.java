//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package de.team33.sudoku;

public class Hint {
    private Choice m_Choice;
    private Number m_Number;

    public Hint(Choice c, Number n) {
        this.m_Choice = c;
        this.m_Number = n;
    }

    public Choice getChoice() {
        return this.m_Choice;
    }

    public Number getNumber() {
        return this.m_Number;
    }
}
