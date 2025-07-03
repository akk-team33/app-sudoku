//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package de.team33.sudoku.main;

import de.team33.sudoku.Sudoku;
import de.team33.sudoku.ui.MainFrame;

import javax.swing.*;

public class Main implements Runnable {
    public Main() {
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Main());
    }

    public void run() {
        (new MainFrame(new Sudoku())).setVisible(true);
    }
}
