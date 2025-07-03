package de.team33.sudoku.ui;

import javax.swing.*;
import java.awt.*;

public class BasicInfoGrid extends JPanel {
    private static final long serialVersionUID = 1857409737454594021L;

    public BasicInfoGrid(int rows, int cols) {
        super(new GridLayout(rows, cols, 2, 2));
        this.setBorder(BorderFactory.createEmptyBorder(1, 1, 1, 1));
        this.setBackground(Color.BLACK);
    }

    public static class Area extends JPanel {
        private static final long serialVersionUID = -8795311479959120614L;

        public Area(int rows, int cols) {
            super(new GridLayout(rows, cols, 1, 1));
            this.setBackground(Color.DARK_GRAY);
        }
    }
}
