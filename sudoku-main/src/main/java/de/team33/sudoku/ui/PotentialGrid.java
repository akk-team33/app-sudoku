//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package de.team33.sudoku.ui;

import de.team33.messaging.Listener;
import de.team33.messaging.simplex.Relay;
import de.team33.sudoku.Number;
import de.team33.sudoku.*;

import javax.swing.*;
import java.awt.*;

public class PotentialGrid extends JPanel {
    private static final long serialVersionUID = -3142888299633458277L;
    private PotentialCell[] m_Cells;

    public PotentialGrid(Potential p, Relay<HiliteMessage> rel, Setup s) {
        super(new GridLayout(Numbers.getRadix(), Numbers.getRadix(), 1, 1));
        this.setBorder(BorderFactory.createEmptyBorder(1, 1, 1, 1));
        this.m_Cells = new PotentialCell[Numbers.getCount()];

        for(int i = 0; i < this.m_Cells.length; ++i) {
            this.m_Cells[i] = new PotentialCell(p, Numbers.get(i), rel, s);
            this.add(this.m_Cells[i]);
        }

    }

    public void addCellListener(Listener<PotentialCell.SelectMessage> l) {
        for(int i = 0; i < this.m_Cells.length; ++i) {
            this.m_Cells[i].getRegister().add(l);
        }

    }

    public void setVisible(boolean aFlag) {
        for(int i = 0; i < this.m_Cells.length; ++i) {
            this.m_Cells[i].setVisible(aFlag);
        }

        super.setVisible(aFlag);
    }

    public PotentialCell getPotentialCell(Number number) {
        return this.m_Cells[number.getIdentity()];
    }
}
