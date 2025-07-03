package de.team33.sudoku;

import java.util.ArrayList;
import java.util.Collection;

public class Sudoku {
    private final Choice[] m_Choice;
    private final GROUP[] m_Group;

    public Sudoku() {
        final int radix = Numbers.getRadix();
        final int count = Numbers.getCount();
        this.m_Group = new GROUP[3 * count];

        int i;
        for(i = 0; i < m_Group.length; ++i) {
            m_Group[i] = new GROUP();
        }

        this.m_Choice = new Choice[count * count];

        for(i = 0; i < m_Choice.length; ++i) {
            final int y = i / count;
            final int x = i % count;
            final int z = y / radix * radix + x / radix;
            m_Choice[i] = new Choice(getColGrp(x), getRowGrp(y), getAreaGrp(z));
            getColGrp(x).add(m_Choice[i]);
            getRowGrp(y).add(m_Choice[i]);
            getAreaGrp(z).add(m_Choice[i]);
        }

    }

    public Sudoku(final Sudoku jig) {
        this();

        for(int i = 0; i < m_Choice.length; ++i) {
            m_Choice[i].setNumber(jig.m_Choice[i].getNumber());
        }

    }

    public final void reset() {
        for(int i = 0; i < m_Choice.length; ++i) {
            m_Choice[i].setNumber((Number)null);
        }

    }

    public final Choice getChoice(final int x, final int y) {
        return m_Choice[y * Numbers.getCount() + x];
    }

    public final Group getColGrp(final int x) {
        return _getGroup(0, x);
    }

    public final Group getRowGrp(final int y) {
        return _getGroup(1, y);
    }

    public final Group getAreaGrp(final int z) {
        return _getGroup(2, z);
    }

    public final Collection<Hint> getHints() {
        final ArrayList<Hint> ret = new ArrayList();

        int i;
        Collection hints;
        for(i = 0; i < m_Choice.length; ++i) {
            hints = m_Choice[i].getHints();
            ret.addAll(hints);
        }

        for(i = 0; i < m_Group.length; ++i) {
            hints = m_Group[i].getHints();
            ret.addAll(hints);
        }

        return ret;
    }

    private GROUP _getGroup(final int spec, final int i) {
        return m_Group[3 * i + spec % 3];
    }

    private class GROUP extends Group {
    }
}
