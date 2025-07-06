package de.team33.sudoku;

import de.team33.messaging.Consumer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public class Group {
    private final POTENTIAL m_Potential = new POTENTIAL();
    private final List<Choice> m_Choices = new ArrayList();

    public final Potential getPotential() {
        return m_Potential;
    }

    public final void add(final Choice c) {
        m_Choices.add(c);
        c.getRegister().add(m_Potential.getChoiceListener());
    }

    public final Collection<Hint> getHints() {
        final List<Hint> ret = new ArrayList();
        int i = 0;

        for(final int n = Numbers.getCount(); i < n; ++i) {
            final ArrayList<Choice> choices = new ArrayList();
            final Iterator var6 = m_Choices.iterator();

            while(var6.hasNext()) {
                final Choice c = (Choice)var6.next();
                if (c.getPotential().includes(Numbers.get(i))) {
                    choices.add(c);
                }
            }

            if (choices.size() == 1) {
                ret.add(new Hint((Choice)choices.get(0), Numbers.get(i)));
            }
        }

        return ret;
    }

    private class POTENTIAL extends Potential {
        private final SUB_POT m_SubPotential = new SUB_POT();

        public POTENTIAL() {
            m_SubPotential.getRegister().add(new PotentialListener());
        }

        public final boolean includes(final Number n) {
            return super.includes(n) && m_SubPotential.includes(n);
        }

        public final Consumer<Choice.Message> getChoiceListener() {
            return m_SubPotential;
        }

        private class PotentialListener implements Consumer<Message> {

            public final void accept(final Potential.Message message) {
                fire(new Potential.REPORT());
            }
        }

        private class SUB_POT extends Potential implements Consumer<Choice.Message> {

            public final void accept(final Choice.Message message) {
                final Number newNumber = message.getSender().getNumber();
                if (message.getOldNumber() != newNumber) {
                    if (message.getOldNumber() != null) {
                        include(message.getOldNumber(), true);
                    }

                    if (newNumber != null) {
                        include(newNumber, false);
                    }
                }

            }
        }
    }
}
