package de.team33.sudoku.main;

import de.team33.sudoku.Board;
import de.team33.sudoku.ui.MainFrame;

import javax.swing.*;

public class Main implements Runnable {

    public static void main(final String[] args) {
        SwingUtilities.invokeLater(new Main());
    }

    public final void run() {
        (new MainFrame(new Board())).setVisible(true);
    }
}
