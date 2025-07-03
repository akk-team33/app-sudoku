package de.team33.sudoku.ui;

import de.team33.messaging.simplex.Relay;
import de.team33.sudoku.HiliteMessage;
import de.team33.sudoku.Potential;
import de.team33.sudoku.Setup;

import javax.swing.*;
import java.awt.*;

public class InfoCell extends JPanel {
    private static final long serialVersionUID = 9182387339119319899L;
    private PotentialGrid m_PotentialGrid;

    public InfoCell(Potential p, Relay<HiliteMessage> rel, Setup s) {
        super(new BorderLayout());
        this.m_PotentialGrid = new PotentialGrid(p, rel, s);
        this.add(this.m_PotentialGrid, "Center");
    }

    public PotentialGrid getPotentialGrid() {
        return this.m_PotentialGrid;
    }
}
