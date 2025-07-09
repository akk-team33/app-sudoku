package de.team33.sudoku.ui;

import de.team33.sphinx.alpha.activity.Event;
import de.team33.sphinx.alpha.visual.JButtons;
import de.team33.sphinx.alpha.visual.JCheckBoxes;
import de.team33.sphinx.alpha.visual.JPanels;
import de.team33.sudoku.Board;
import de.team33.sudoku.HiliteRelayPool;
import de.team33.sudoku.Hint;
import de.team33.sudoku.Setup;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class MainFrame extends JFrame {

    private static final Insets INSETS_0000 = new Insets(0, 0, 0, 0);

    private final Setup setup;
    private final HiliteRelayPool hiliteRelayPool;
    private final Board board;
    private final ChoiceGrid choiceGrid;

    public MainFrame(final Board board) {
        super("Sudoku");

        final Requisite requisite = requisite(board);
        this.setup = requisite.setup;
        this.hiliteRelayPool = requisite.hiliteRelayPool;
        this.board = requisite.board;
        this.choiceGrid = requisite.choiceGrid;

        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        add(new CENTER_PANE(requisite), BorderLayout.CENTER);
        add(new EAST_PANE(requisite), BorderLayout.EAST);
        pack();
    }

    private static Requisite requisite(final Board board) {
        return new Requisite(new Setup(), new HiliteRelayPool(), board, null).withChoiceGrid();
    }

    private static GridBagConstraints constraints(final int x, final int y, final int width, final int height) {
        return new GridBagConstraints(x, y, width, height, 0.0, 0.0, 10, 1, INSETS_0000, 0, 0);
    }

    private static JButton cloneButton(final Requisite req) {
        return JButtons.builder()
                       .setText("Clone")
                       .on(Event.ACTION_PERFORMED, e -> (new MainFrame(new Board(req.board))).setVisible(true))
                       .build();
    }

    private static JCheckBox hiliteCheckBox(final Requisite req) {
        return JCheckBoxes.builder()
                          .setText("Group Hints")
                          .on(Event.ACTION_PERFORMED, e -> onHiliteCheckBox(e, req))
                          .setup(cb -> req.setup.getRegister()
                                                .add(setup -> cb.setSelected(setup.getGroupHiliting())))
                          .build();
    }

    private static void onHiliteCheckBox(final ActionEvent event, final Requisite req) {
        final JCheckBox source = (JCheckBox) event.getSource();
        req.setup.setGroupHiliting(source.isSelected());
    }

    private static JButton proposalButton(final Requisite req) {
        return JButtons.builder()
                       .setText("Proposals")
                       .on(Event.ACTION_PERFORMED, e -> onProposalButton(req))
                       .build();
    }

    private static void onProposalButton(final Requisite req) {
        for (final Hint h : req.board.getHints()) {
            req.choiceGrid.getPotentialCell(h.getChoice(), h.getNumber())
                          .setHinting(true);
        }
    }

    private static JButton resetButton(final Requisite req) {
        return JButtons.builder()
                       .setText("Reset")
                       .on(Event.ACTION_PERFORMED, e -> req.board.reset())
                       .build();
    }

    private static JCheckBox autoHintCheckBox(final Requisite req) {
        return JCheckBoxes.builder()
                          .setText("Auto Proposals")
                          .on(Event.ACTION_PERFORMED, e -> onAutoHintCheckBox(e, req.setup))
                          .setup(cb -> req.setup.getRegister()
                                                .add(setup -> cb.setSelected(setup.getAutoHinting())))
                          .build();
    }

    private static void onAutoHintCheckBox(final ActionEvent event, final Setup setup) {
        final JCheckBox checkBox = (JCheckBox) event.getSource();
        setup.setAutoHinting(checkBox.isSelected());
    }

    private record Requisite(Setup setup, HiliteRelayPool hiliteRelayPool, Board board, ChoiceGrid choiceGrid) {

        private Requisite withChoiceGrid() {
            return new Requisite(setup, hiliteRelayPool, board, new ChoiceGrid(board, hiliteRelayPool, setup));
        }
    }

    private static class CENTER_PANE extends JPanel {
        private static final long serialVersionUID = -3672158492746235449L;

        public CENTER_PANE(final Requisite req) {
            super(new GridBagLayout());
            add(new ColInfoGrid(req.board, req.hiliteRelayPool, req.setup), constraints(3, 1, 9, 1));
            add(new RowInfoGrid(req.board, req.hiliteRelayPool, req.setup), constraints(1, 3, 1, 9));
            add(new AreaInfoGrid(req.board, req.hiliteRelayPool, req.setup), constraints(13, 3, 3, 3));
            add(new JPanel(), constraints(0, 0, 1, 1));
            add(new JPanel(), constraints(2, 2, 1, 1));
            add(new JPanel(), constraints(12, 2, 1, 1));
            add(new JPanel(), constraints(16, 13, 1, 1));
            add(req.choiceGrid, constraints(3, 3, 9, 9));
            // add(new ChoiceGrid(req.board, req.hiliteRelayPool, req.setup), constraints(3, 3, 9, 9));
        }
    }

    private static JPanel actionGrid(final Requisite req) {
        return JPanels.builder()
                      .setLayout(new GridLayout(0, 1, 1, 1))
                      .setBorder(BorderFactory.createEmptyBorder(1, 1, 1, 1))
                      .add(cloneButton(req))
                      .add(resetButton(req))
                      .add(proposalButton(req))
                      .add(hiliteCheckBox(req))
                      .add(autoHintCheckBox(req))
                      .build();
    }

    private class EAST_PANE extends JPanel {
        private static final long serialVersionUID = -1633863694402998797L;

        public EAST_PANE(final Requisite req) {
            super(new BorderLayout());
            setBorder(BorderFactory.createRaisedBevelBorder());
            add(actionGrid(req), BorderLayout.NORTH);
            add(InfoGrid.by(req.board), BorderLayout.SOUTH);
        }
    }
}
