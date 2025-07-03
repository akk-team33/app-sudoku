//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package de.team33.sudoku;

public class Numbers {
    private static int m_Radix = 3;
    private static Number[] m_Number = null;

    public Numbers() {
    }

    public static Number get(int id) {
        if (m_Number == null) {
            m_Number = new Number[getCount()];
        }

        if (m_Number[id] == null) {
            m_Number[id] = new NUMBER(id);
        }

        return m_Number[id];
    }

    public static int getCount() {
        return getRadix() * getRadix();
    }

    public static int getRadix() {
        return m_Radix;
    }

    private static class NUMBER implements Number {
        private int m_Identity;

        public NUMBER(int identity) {
            this.m_Identity = identity;
        }

        public int getIdentity() {
            return this.m_Identity;
        }

        public String getDisplay() {
            if (Numbers.getCount() < 10) {
                return String.format("%d", this.m_Identity + 1);
            } else if (this.m_Identity < 10) {
                return String.format("%d", this.m_Identity);
            } else {
                int d = 65 + this.m_Identity - 10;
                char c = (char)d;
                return String.format("%c", c);
            }
        }
    }
}
