package de.team33.sudoku;

import de.team33.messaging.Listener;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

public class Group {
    private POTENTIAL m_Potential = new POTENTIAL();
    private ArrayList<Choice> m_Choices = new ArrayList();

    public Group() {
    }

    public Potential getPotential() {
        return this.m_Potential;
    }

    public void add(Choice c) {
        this.m_Choices.add(c);
        c.getRegister().add(this.m_Potential.getChoiceListener());
    }

    public Collection<Hint> getHints() {
        ArrayList<Hint> ret = new ArrayList();
        int i = 0;

        for(int n = Numbers.getCount(); i < n; ++i) {
            ArrayList<Choice> choices = new ArrayList();
            Iterator var6 = this.m_Choices.iterator();

            while(var6.hasNext()) {
                Choice c = (Choice)var6.next();
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
        private SUB_POT m_SubPotential = new SUB_POT();

        public POTENTIAL() {
            this.m_SubPotential.getRegister().add(new PotentialListener());
        }

        public boolean includes(Number n) {
            return super.includes(n) && this.m_SubPotential.includes(n);
        }

        public Listener<Choice.Message> getChoiceListener() {
            return this.m_SubPotential;
        }

        private class PotentialListener implements Listener<Potential.Message> {
            private PotentialListener() {
            }

            public void pass(Potential.Message message) {
                fire(new Potential.REPORT());
            }
        }

        private class SUB_POT extends Potential implements Listener<Choice.Message> {
            private SUB_POT() {
            }

            public void pass(Choice.Message message) {
                Number newNumber = message.getSender().getNumber();
                if (message.getOldNumber() != newNumber) {
                    if (message.getOldNumber() != null) {
                        this.include(message.getOldNumber(), true);
                    }

                    if (newNumber != null) {
                        this.include(newNumber, false);
                    }
                }

            }
        }
    }
}
