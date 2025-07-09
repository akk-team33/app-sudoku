package de.team33.sudoku;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Board {

    private final int base;
    private final Choice[] m_Choice;
    private final GROUP[] m_Group;

    public Board() {
        final int radix = Numbers.getRadix();
        this.base = Numbers.getCount();
        this.m_Group = new GROUP[3 * base];

        int i;
        for(i = 0; i < m_Group.length; ++i) {
            m_Group[i] = new GROUP();
        }

        this.m_Choice = new Choice[base * base];

        for(i = 0; i < m_Choice.length; ++i) {
            final int y = i / base;
            final int x = i % base;
            final int z = y / radix * radix + x / radix;
            m_Choice[i] = new Choice(getColGrp(x), getRowGrp(y), getAreaGrp(z));
            getColGrp(x).add(m_Choice[i]);
            getRowGrp(y).add(m_Choice[i]);
            getAreaGrp(z).add(m_Choice[i]);
        }

    }

    public Board(final Board jig) {
        this();

        for(int i = 0; i < m_Choice.length; ++i) {
            m_Choice[i].setNumber(jig.m_Choice[i].getNumber());
        }
    }

    public final int base() {
        return base;
    }

    public final void reset() {
        for(int i = 0; i < m_Choice.length; ++i) {
            m_Choice[i].setNumber(null);
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
        final List<Hint> ret = new ArrayList<>();

        for (final Choice choice : m_Choice) {
            ret.addAll(choice.getHints());
        }

        for (final GROUP group : m_Group) {
            ret.addAll(group.getHints());
        }

        return ret;
    }

    private GROUP _getGroup(final int spec, final int i) {
        return m_Group[3 * i + spec % 3];
    }

    private static class GROUP extends Group {
    }
}
