package de.team33.sudoku.ui;

import de.team33.messaging.simplex.Relay;
import de.team33.sudoku.Number;
import de.team33.sudoku.*;

import javax.swing.*;
import java.awt.*;
import java.util.function.Consumer;

public class PotentialGrid extends JPanel {
    private static final long serialVersionUID = -3142888299633458277L;
    private final PotentialCell[] m_Cells;

    public PotentialGrid(final Potential p, final Relay<HiliteMessage> rel, final Setup s) {
        super(new GridLayout(Numbers.getRadix(), Numbers.getRadix(), 1, 1));
        setBorder(BorderFactory.createEmptyBorder(1, 1, 1, 1));
        this.m_Cells = new PotentialCell[Numbers.getCount()];

        for(int i = 0; i < m_Cells.length; ++i) {
            m_Cells[i] = new PotentialCell(p, Numbers.get(i), rel, s);
            add(m_Cells[i]);
        }

    }

    public final void addCellListener(final Consumer<PotentialCell.SelectMessage> l) {
        for(int i = 0; i < m_Cells.length; ++i) {
            m_Cells[i].getRegister().add(l);
        }

    }

    public final void setVisible(final boolean aFlag) {
        for(int i = 0; i < m_Cells.length; ++i) {
            m_Cells[i].setVisible(aFlag);
        }

        super.setVisible(aFlag);
    }

    public final PotentialCell getPotentialCell(final Number number) {
        return m_Cells[number.getIdentity()];
    }
}
