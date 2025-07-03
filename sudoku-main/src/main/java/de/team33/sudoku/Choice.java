//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package de.team33.sudoku;

import de.team33.messaging.Listener;
import de.team33.messaging.simplex.Sender;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

public class Choice extends Sender<Choice.Message> {
    private Group m_ColGrp;
    private Group m_RowGrp;
    private Group m_AreaGrp;
    private POTENTIAL m_Potential;
    private Number m_Number = null;

    public Choice(Group colPot, Group rowPot, Group areaPot) {
        this.m_ColGrp = colPot;
        this.m_RowGrp = rowPot;
        this.m_AreaGrp = areaPot;
        this.m_Potential = new POTENTIAL();
        this.getRegister().add(this.m_Potential.getChoiceListener());
    }

    public Number getNumber() {
        return this.m_Number;
    }

    public void setNumber(Number n) {
        if (n == null || this.m_Potential.includes(n)) {
            REPORT msg = new REPORT(this.m_Number);
            this.m_Number = n;
            this.fire(msg);
        }

    }

    public Potential getPotential() {
        return this.m_Potential;
    }

    public Collection<Hint> getHints() {
        ArrayList<Hint> ret = new ArrayList();
        Iterator var3 = this.getPotential().getHints().iterator();

        while(var3.hasNext()) {
            Number n = (Number)var3.next();
            ret.add(new Hint(this, n));
        }

        return ret;
    }

    private boolean _isNullNumber() {
        return this.getNumber() == null;
    }

    public interface Message {
        Choice getSender();

        Number getOldNumber();
    }

    private class POTENTIAL extends Potential {
        private Listener<Message> m_PotentialListener = new PotentialListener();
        private Listener<Choice.Message> m_ChoiceListener = new ChoiceListener();

        public POTENTIAL() {
            Choice.this.m_ColGrp.getPotential().getRegister().add(this.m_PotentialListener);
            Choice.this.m_RowGrp.getPotential().getRegister().add(this.m_PotentialListener);
            Choice.this.m_AreaGrp.getPotential().getRegister().add(this.m_PotentialListener);
        }

        public boolean includes(Number n) {
            return Choice.this._isNullNumber() && super.includes(n) && Choice.this.m_ColGrp.getPotential().includes(n) && Choice.this.m_RowGrp.getPotential().includes(n) && Choice.this.m_AreaGrp.getPotential().includes(n);
        }

        public Listener<Choice.Message> getChoiceListener() {
            return this.m_ChoiceListener;
        }

        private class ChoiceListener implements Listener<Choice.Message> {
            private ChoiceListener() {
            }

            public void pass(Choice.Message message) {
                fire(new Potential.REPORT());
            }
        }

        private class PotentialListener implements Listener<Potential.Message> {
            private PotentialListener() {
            }

            public void pass(Potential.Message message) {
                fire(new Potential.REPORT());
            }
        }
    }

    private class REPORT implements Message {
        private Number oldNumber;

        private REPORT(Number oldNumber) {
            this.oldNumber = oldNumber;
        }

        public Number getOldNumber() {
            return this.oldNumber;
        }

        public Choice getSender() {
            return Choice.this;
        }
    }
}
