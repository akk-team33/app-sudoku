package de.team33.sudoku.ui;

import de.team33.messaging.Listener;
import de.team33.messaging.simplex.Relay;
import de.team33.sudoku.Choice;
import de.team33.sudoku.Number;
import de.team33.sudoku.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Iterator;

public class ChoiceGrid extends BasicInfoGrid {
    private final ArrayList<CELL> m_Cells;

    public ChoiceGrid(final Board s, final HiliteRelayPool frp, final Setup su) {
        this(s, Numbers.getRadix(), frp, su);
    }

    public ChoiceGrid(final Board s, final int radix, final HiliteRelayPool frp, final Setup su) {
        super(radix, radix);
        this.m_Cells = new ArrayList();

        for(int y = 0; y < radix; ++y) {
            for(int x = 0; x < radix; ++x) {
                add(new AREA(s, radix, frp, su, x, y));
            }
        }

    }

    public final PotentialCell getPotentialCell(final Choice choice, final de.team33.sudoku.Number number) {
        final Iterator var4 = m_Cells.iterator();

        while(var4.hasNext()) {
            final CELL c = (CELL)var4.next();
            final PotentialCell pc = c.getPotentialCell(choice, number);
            if (pc != null) {
                return pc;
            }
        }

        return null;
    }

    private class AREA extends BasicInfoGrid.Area {
        private static final long serialVersionUID = -6063977488067059587L;

        public AREA(final Board s, final int radix, final HiliteRelayPool frp, final Setup su, final int x0, final int y0) {
            super(radix, radix);

            for(int dy = 0; dy < radix; ++dy) {
                for(int dx = 0; dx < radix; ++dx) {
                    final int x = radix * x0 + dx;
                    final int y = radix * y0 + dy;
                    final CELL c = new CELL(s.getChoice(x, y), frp.getCellRelay(x, y), su);
                    add(c);
                    m_Cells.add(c);
                }
            }

        }
    }

    private static class CELL extends InfoCell {
        private final Choice m_Choice;
        private final LABEL m_Label = new LABEL();

        public CELL(final Choice ch, final Relay<HiliteMessage> rel, final Setup su) {
            super(ch.getPotential(), rel, su);
            (this.m_Choice = ch).getRegister().add(new ChoiceListener());
            getPotentialGrid().addCellListener(new PotentialCellListener());
            m_Label.addMouseListener(new ADAPTER());
        }

        public final PotentialCell getPotentialCell(final Choice choice, final de.team33.sudoku.Number number) {
            return m_Choice == choice ? getPotentialGrid().getPotentialCell(number) : null;
        }

        private class ADAPTER extends MouseAdapter {

            public final void mouseReleased(final MouseEvent e) {
                if (e.getButton() == 1) {
                    m_Choice.setNumber((de.team33.sudoku.Number)null);
                }

            }
        }

        private class ChoiceListener implements Listener<Choice.Message> {

            public final void pass(final Choice.Message message) {
                final de.team33.sudoku.Number newNumber = message.getSender().getNumber();
                final de.team33.sudoku.Number oldNumber = message.getOldNumber();
                if (oldNumber != newNumber) {
                    if (oldNumber == null) {
                        getPotentialGrid().setVisible(false);
                        remove(getPotentialGrid());
                        add(m_Label, "Center");
                        m_Label.setVisible(true);
                    } else if (newNumber == null) {
                        m_Label.setVisible(false);
                        remove(m_Label);
                        add(getPotentialGrid(), "Center");
                        getPotentialGrid().setVisible(true);
                    }

                    m_Label.setNumber(newNumber);
                }

            }
        }

        private static class LABEL extends JLabel {
            private static final long serialVersionUID = 549669744632117387L;

            public LABEL() {
                super("");
                setHorizontalAlignment(0);
                setVerticalAlignment(0);
                setOpaque(true);
                setBackground(Color.WHITE);
                setFont(new Font(getFont().getName(), 1, 32));
            }

            public final void setNumber(final Number n) {
                if (n == null) {
                    setText("");
                } else {
                    setText(n.getDisplay());
                }

            }
        }

        private class PotentialCellListener implements Listener<PotentialCell.SelectMessage> {

            public final void pass(final PotentialCell.SelectMessage message) {
                m_Choice.setNumber(message.getNumber());
            }
        }
    }
}
