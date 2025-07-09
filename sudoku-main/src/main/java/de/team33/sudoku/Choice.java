package de.team33.sudoku;

import de.team33.messaging.simplex.Sender;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;

public class Choice extends Sender<Choice.Message> {
    private final Group colGrp;
    private final Group rowGrp;
    private final Group areaGrp;
    private final POTENTIAL m_Potential;
    private Number m_Number = null;

    public Choice(final Group colPot, final Group rowPot, final Group areaPot) {
        this.colGrp = colPot;
        this.rowGrp = rowPot;
        this.areaGrp = areaPot;
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

    private final class POTENTIAL extends Potential {

        private final Consumer<Choice.Message> choiceListener = message -> fire(new Potential.REPORT());

        private POTENTIAL() {
            final Consumer<Message> listener = message -> fire(new REPORT());
            for (final Group group: List.of(colGrp, rowGrp, areaGrp)) {
                group.getPotential().getRegister().add(listener);
            }
        }

        @Override
        public final boolean includes(final Number n) {
            return _isNullNumber() && super.includes(n) && colGrp.getPotential().includes(n) && rowGrp.getPotential().includes(n) && areaGrp.getPotential().includes(n);
        }

        public final Consumer<Choice.Message> getChoiceListener() {
            return choiceListener;
        }
    }

    private class REPORT implements Message {
        private final Number oldNumber;

        private REPORT(final Number oldNumber) {
            this.oldNumber = oldNumber;
        }

        @Override
        public final Number getOldNumber() {
            return oldNumber;
        }

        @Override
        public final Choice getSender() {
            return Choice.this;
        }
    }
}
