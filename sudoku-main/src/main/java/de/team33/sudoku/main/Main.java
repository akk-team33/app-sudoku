package de.team33.sudoku.main;

import net.team33.sudoku.Sudoku;
import net.team33.sudoku.ui.MainFrame;

import javax.swing.*;

public class Main implements Runnable {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Main());
    }

    public void run() {
        (new MainFrame(new Sudoku())).setVisible(true);
    }
}
