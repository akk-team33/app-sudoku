package de.team33.sudoku.main;

import de.team33.sudoku.Board;
import de.team33.sudoku.ui.MainFrame;

import javax.swing.*;

public class Sudoku implements Runnable {

    public static void main(final String[] args) {
        SwingUtilities.invokeLater(new Sudoku());
    }

    public final void run() {
        (new MainFrame(new Board())).setVisible(true);
    }
}
