package de.team33.sudoku;

public class Numbers {
    private static final int m_Radix = 3;
    private static Number[] m_Number = null;

    public static Number get(final int id) {
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
        private final int m_Identity;

        public NUMBER(final int identity) {
            this.m_Identity = identity;
        }

        public final int getIdentity() {
            return m_Identity;
        }

        public final String getDisplay() {
            if (Numbers.getCount() < 10) {
                return String.format("%d", m_Identity + 1);
            } else if (m_Identity < 10) {
                return String.format("%d", m_Identity);
            } else {
                final int d = 65 + m_Identity - 10;
                final char c = (char)d;
                return String.format("%c", c);
            }
        }
    }
}
