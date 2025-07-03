package de.team33.sudoku;

import de.team33.messaging.simplex.Sender;

import java.util.ArrayList;
import java.util.Collection;

public abstract class Potential extends Sender<Potential.Message> {
    private int m_ValueMask = 0;

    public boolean includes(final Number n) {
        return (m_ValueMask & 1 << n.getIdentity()) == 0;
    }

    public final void include(final Number n, final boolean include) {
        if (include) {
            this.m_ValueMask &= ~(1 << n.getIdentity());
        } else {
            this.m_ValueMask |= 1 << n.getIdentity();
        }

        fire(new REPORT());
    }

    public final Collection<Number> getNumbers() {
        final ArrayList<Number> ret = new ArrayList();
        int i = 0;

        for(final int n = Numbers.getCount(); i < n; ++i) {
            if (includes(Numbers.get(i))) {
                ret.add(Numbers.get(i));
            }
        }

        return ret;
    }

    public final Collection<Number> getHints() {
        final Collection<Number> ret = getNumbers();
        if (ret.size() != 1) {
            ret.clear();
        }

        return ret;
    }

    public final void reset() {
        this.m_ValueMask = 0;
        fire(new REPORT());
    }

    public interface Message {
        Potential getSender();
    }

    protected class REPORT implements Message {

        public final Potential getSender() {
            return Potential.this;
        }
    }
}
