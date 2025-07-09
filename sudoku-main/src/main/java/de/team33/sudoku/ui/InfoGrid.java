package de.team33.sudoku.ui;

import de.team33.sphinx.alpha.visual.JLabels;
import de.team33.sphinx.alpha.visual.JPanels;
import de.team33.sudoku.Board;
import de.team33.sudoku.Choice;
import de.team33.sudoku.Number;
import de.team33.sudoku.Numbers;

import javax.swing.*;
import java.awt.*;

class InfoGrid extends JPanel {

    private final int[] m_Values;
    private final JLabel[] m_Labels;

    private InfoGrid(final int baseCount) {
        final int length = baseCount + 1;
        m_Values = new int[length];
        m_Labels = new JLabel[length];
    }

    static InfoGrid by(final Board board) {
        return JPanels.builder(() -> new InfoGrid(board.base()))
                      .setLayout(new GridLayout(0, 2, 1, 1))
                      .setBorder(BorderFactory.createEmptyBorder(1, 1, 1, 1))
                      .setup(InfoGrid::addLabels)
                      .setup(infoGrid -> infoGrid.register(board))
                      .build();
    }

    private void addLabels() {
        for (int y = 1; y < m_Labels.length; ++y) {
            m_Labels[y] = infoLabel(0);
            add(infoLabel(y));
            add(m_Labels[y]);
        }
        m_Labels[0] = infoLabel(0);
        add(infoLabel("Total"));
        add(m_Labels[0]);
    }

    private void register(final Board board) {
        for (int y = 0; y < Numbers.getCount(); ++y) {
            for (int x = 0; x < Numbers.getCount(); ++x) {
                board.getChoice(x, y)
                     .getRegister()
                     .add(this::accept);
            }
        }
    }

    public final void accept(final Choice.Message message) {
        final Number newNumber = message.getSender().getNumber();
        final Number oldNumber = message.getOldNumber();
        if (oldNumber != newNumber) {
            final int i;
            if (null == oldNumber) {
                i = newNumber.getIdentity() + 1;
                m_Values[i]++;
                m_Labels[i].setText("" + m_Values[i]);
                m_Values[0]++;
                m_Labels[0].setText("" + m_Values[0]);
            } else if (null == newNumber) {
                i = oldNumber.getIdentity() + 1;
                m_Values[i]--;
                m_Labels[i].setText("" + m_Values[i]);
                m_Values[0]--;
                m_Labels[0].setText("" + m_Values[0]);
            }
        }
    }

    private static JLabel infoLabel(final String text) {
        return JLabels.builder()
                      .setText(text)
                      .setHorizontalAlignment(SwingConstants.CENTER)
                      .setVerticalAlignment(SwingConstants.CENTER)
                      .setOpaque(true)
                      .setBackground(Color.WHITE)
                      .build();
    }

    private static JLabel infoLabel(final int value) {
        return infoLabel("" + value);
    }
}
