//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package de.team33.sudoku;

import java.util.ArrayList;
import java.util.Collection;

public class Sudoku {
    private Choice[] m_Choice;
    private GROUP[] m_Group;

    public Sudoku() {
        int radix = Numbers.getRadix();
        int count = Numbers.getCount();
        this.m_Group = new GROUP[3 * count];

        int i;
        for(i = 0; i < this.m_Group.length; ++i) {
            this.m_Group[i] = new GROUP();
        }

        this.m_Choice = new Choice[count * count];

        for(i = 0; i < this.m_Choice.length; ++i) {
            int y = i / count;
            int x = i % count;
            int z = y / radix * radix + x / radix;
            this.m_Choice[i] = new Choice(this.getColGrp(x), this.getRowGrp(y), this.getAreaGrp(z));
            this.getColGrp(x).add(this.m_Choice[i]);
            this.getRowGrp(y).add(this.m_Choice[i]);
            this.getAreaGrp(z).add(this.m_Choice[i]);
        }

    }

    public Sudoku(Sudoku jig) {
        this();

        for(int i = 0; i < this.m_Choice.length; ++i) {
            this.m_Choice[i].setNumber(jig.m_Choice[i].getNumber());
        }

    }

    public void reset() {
        for(int i = 0; i < this.m_Choice.length; ++i) {
            this.m_Choice[i].setNumber((Number)null);
        }

    }

    public Choice getChoice(int x, int y) {
        return this.m_Choice[y * Numbers.getCount() + x];
    }

    public Group getColGrp(int x) {
        return this._getGroup(0, x);
    }

    public Group getRowGrp(int y) {
        return this._getGroup(1, y);
    }

    public Group getAreaGrp(int z) {
        return this._getGroup(2, z);
    }

    public Collection<Hint> getHints() {
        ArrayList<Hint> ret = new ArrayList();

        int i;
        Collection hints;
        for(i = 0; i < this.m_Choice.length; ++i) {
            hints = this.m_Choice[i].getHints();
            ret.addAll(hints);
        }

        for(i = 0; i < this.m_Group.length; ++i) {
            hints = this.m_Group[i].getHints();
            ret.addAll(hints);
        }

        return ret;
    }

    private GROUP _getGroup(int spec, int i) {
        return this.m_Group[3 * i + spec % 3];
    }

    private class GROUP extends Group {
        private GROUP() {
        }
    }
}
