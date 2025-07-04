package de.team33.sudoku;

import de.team33.messaging.Listener;
import de.team33.messaging.simplex.Sender;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public class Choice extends Sender<Choice.Message> {
    private final Group m_ColGrp;
    private final Group m_RowGrp;
    private final Group m_AreaGrp;
    private final POTENTIAL m_Potential;
    private Number m_Number = null;

    public Choice(final Group colPot, final Group rowPot, final Group areaPot) {
        this.m_ColGrp = colPot;
        this.m_RowGrp = rowPot;
        this.m_AreaGrp = areaPot;
        this.m_Potential = new POTENTIAL();
        getRegister().add(m_Potential.getChoiceListener());
    }

    public final Number getNumber() {
        return m_Number;
    }

    public final void setNumber(final Number n) {
        if (n == null || m_Potential.includes(n)) {
            final Message msg = new REPORT(m_Number);
            this.m_Number = n;
            fire(msg);
        }

    }

    public final Potential getPotential() {
        return m_Potential;
    }

    public final Collection<Hint> getHints() {
        final List<Hint> ret = new ArrayList();
        final Iterator var3 = getPotential().getHints().iterator();

        while(var3.hasNext()) {
            final Number n = (Number)var3.next();
            ret.add(new Hint(this, n));
        }

        return ret;
    }

    private boolean _isNullNumber() {
        return getNumber() == null;
    }

    public interface Message {
        Choice getSender();

        Number getOldNumber();
    }

    private class POTENTIAL extends Potential {
        private final Listener<Message> m_PotentialListener = new PotentialListener();
        private final Listener<Choice.Message> m_ChoiceListener = new ChoiceListener();

        public POTENTIAL() {
            m_ColGrp.getPotential().getRegister().add(m_PotentialListener);
            m_RowGrp.getPotential().getRegister().add(m_PotentialListener);
            m_AreaGrp.getPotential().getRegister().add(m_PotentialListener);
        }

        public final boolean includes(final Number n) {
            return _isNullNumber() && super.includes(n) && m_ColGrp.getPotential().includes(n) && m_RowGrp.getPotential().includes(n) && m_AreaGrp.getPotential().includes(n);
        }

        public final Listener<Choice.Message> getChoiceListener() {
            return m_ChoiceListener;
        }

        private class ChoiceListener implements Listener<Choice.Message> {

            public final void pass(final Choice.Message message) {
                fire(new Potential.REPORT());
            }
        }

        private class PotentialListener implements Listener<Potential.Message> {

            public final void pass(final Potential.Message message) {
                fire(new Potential.REPORT());
            }
        }
    }

    private class REPORT implements Message {
        private final Number oldNumber;

        private REPORT(final Number oldNumber) {
            this.oldNumber = oldNumber;
        }

        public final Number getOldNumber() {
            return oldNumber;
        }

        public final Choice getSender() {
            return Choice.this;
        }
    }
}
