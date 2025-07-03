package de.team33.sudoku.ui;

import de.team33.messaging.simplex.Relay;
import de.team33.sudoku.HiliteMessage;
import de.team33.sudoku.Potential;
import de.team33.sudoku.Setup;

import javax.swing.*;
import java.awt.*;

public class InfoCell extends JPanel {
    private static final long serialVersionUID = 9182387339119319899L;
    private final PotentialGrid m_PotentialGrid;

    public InfoCell(final Potential p, final Relay<HiliteMessage> rel, final Setup s) {
        super(new BorderLayout());
        this.m_PotentialGrid = new PotentialGrid(p, rel, s);
        add(m_PotentialGrid, "Center");
    }

    public final PotentialGrid getPotentialGrid() {
        return m_PotentialGrid;
    }
}
