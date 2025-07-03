//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package de.team33.sudoku;

import de.team33.messaging.simplex.Sender;

import java.util.ArrayList;
import java.util.Collection;

public abstract class Potential extends Sender<Potential.Message> {
    private int m_ValueMask = 0;

    public Potential() {
    }

    public boolean includes(Number n) {
        return (this.m_ValueMask & 1 << n.getIdentity()) == 0;
    }

    public void include(Number n, boolean include) {
        if (include) {
            this.m_ValueMask &= ~(1 << n.getIdentity());
        } else {
            this.m_ValueMask |= 1 << n.getIdentity();
        }

        this.fire(new REPORT());
    }

    public Collection<Number> getNumbers() {
        ArrayList<Number> ret = new ArrayList();
        int i = 0;

        for(int n = Numbers.getCount(); i < n; ++i) {
            if (this.includes(Numbers.get(i))) {
                ret.add(Numbers.get(i));
            }
        }

        return ret;
    }

    public Collection<Number> getHints() {
        Collection<Number> ret = this.getNumbers();
        if (ret.size() != 1) {
            ret.clear();
        }

        return ret;
    }

    public void reset() {
        this.m_ValueMask = 0;
        this.fire(new REPORT());
    }

    public interface Message {
        Potential getSender();
    }

    protected class REPORT implements Message {
        protected REPORT() {
        }

        public Potential getSender() {
            return Potential.this;
        }
    }
}
