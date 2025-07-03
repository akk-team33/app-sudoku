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
    private ArrayList<CELL> m_Cells;

    public ChoiceGrid(Sudoku s, HiliteRelayPool frp, Setup su) {
        this(s, Numbers.getRadix(), frp, su);
    }

    public ChoiceGrid(Sudoku s, int radix, HiliteRelayPool frp, Setup su) {
        super(radix, radix);
        this.m_Cells = new ArrayList();

        for(int y = 0; y < radix; ++y) {
            for(int x = 0; x < radix; ++x) {
                this.add(new AREA(s, radix, frp, su, x, y));
            }
        }

    }

    public PotentialCell getPotentialCell(Choice choice, de.team33.sudoku.Number number) {
        Iterator var4 = this.m_Cells.iterator();

        while(var4.hasNext()) {
            CELL c = (CELL)var4.next();
            PotentialCell pc = c.getPotentialCell(choice, number);
            if (pc != null) {
                return pc;
            }
        }

        return null;
    }

    private class AREA extends BasicInfoGrid.Area {
        private static final long serialVersionUID = -6063977488067059587L;

        public AREA(Sudoku s, int radix, HiliteRelayPool frp, Setup su, int x0, int y0) {
            super(radix, radix);

            for(int dy = 0; dy < radix; ++dy) {
                for(int dx = 0; dx < radix; ++dx) {
                    int x = radix * x0 + dx;
                    int y = radix * y0 + dy;
                    CELL c = new CELL(s.getChoice(x, y), frp.getCellRelay(x, y), su);
                    this.add(c);
                    ChoiceGrid.this.m_Cells.add(c);
                }
            }

        }
    }

    private static class CELL extends InfoCell {
        private Choice m_Choice;
        private LABEL m_Label = new LABEL();

        public CELL(Choice ch, Relay<HiliteMessage> rel, Setup su) {
            super(ch.getPotential(), rel, su);
            (this.m_Choice = ch).getRegister().add(new ChoiceListener());
            this.getPotentialGrid().addCellListener(new PotentialCellListener());
            this.m_Label.addMouseListener(new ADAPTER());
        }

        public PotentialCell getPotentialCell(Choice choice, de.team33.sudoku.Number number) {
            return this.m_Choice == choice ? this.getPotentialGrid().getPotentialCell(number) : null;
        }

        private class ADAPTER extends MouseAdapter {
            private ADAPTER() {
            }

            public void mouseReleased(MouseEvent e) {
                if (e.getButton() == 1) {
                    CELL.this.m_Choice.setNumber((de.team33.sudoku.Number)null);
                }

            }
        }

        private class ChoiceListener implements Listener<Choice.Message> {
            private ChoiceListener() {
            }

            public void pass(Choice.Message message) {
                de.team33.sudoku.Number newNumber = message.getSender().getNumber();
                de.team33.sudoku.Number oldNumber = message.getOldNumber();
                if (oldNumber != newNumber) {
                    if (oldNumber == null) {
                        CELL.this.getPotentialGrid().setVisible(false);
                        CELL.this.remove(CELL.this.getPotentialGrid());
                        CELL.this.add(CELL.this.m_Label, "Center");
                        CELL.this.m_Label.setVisible(true);
                    } else if (newNumber == null) {
                        CELL.this.m_Label.setVisible(false);
                        CELL.this.remove(CELL.this.m_Label);
                        CELL.this.add(CELL.this.getPotentialGrid(), "Center");
                        CELL.this.getPotentialGrid().setVisible(true);
                    }

                    CELL.this.m_Label.setNumber(newNumber);
                }

            }
        }

        private static class LABEL extends JLabel {
            private static final long serialVersionUID = 549669744632117387L;

            public LABEL() {
                super("");
                this.setHorizontalAlignment(0);
                this.setVerticalAlignment(0);
                this.setOpaque(true);
                this.setBackground(Color.WHITE);
                this.setFont(new Font(this.getFont().getName(), 1, 32));
            }

            public void setNumber(Number n) {
                if (n == null) {
                    this.setText("");
                } else {
                    this.setText(n.getDisplay());
                }

            }
        }

        private class PotentialCellListener implements Listener<PotentialCell.SelectMessage> {
            private PotentialCellListener() {
            }

            public void pass(PotentialCell.SelectMessage message) {
                CELL.this.m_Choice.setNumber(message.getNumber());
            }
        }
    }
}
