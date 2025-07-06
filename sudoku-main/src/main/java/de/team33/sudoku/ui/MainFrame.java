package de.team33.sudoku.ui;

import de.team33.messaging.Listener;
import de.team33.sphinx.alpha.activity.Event;
import de.team33.sphinx.alpha.visual.JButtons;
import de.team33.sphinx.alpha.visual.JFrames;
import de.team33.sphinx.alpha.visual.JPanels;
import de.team33.sudoku.Choice;
import de.team33.sudoku.Number;
import de.team33.sudoku.*;
import de.team33.swinx.XCheckBox;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class MainFrame extends JFrame {

    public static final Insets INSETS = new Insets(0, 0, 0, 0);

    private final Setup setup;
    private final HiliteRelayPool hiliteRelayPool;
    private final Board board;
    private final ChoiceGrid choiceGrid;

    private MainFrame(final Core core) {
        setup = core.setup;
        hiliteRelayPool = core.hiliteRelayPool;
        board = core.board;
        choiceGrid = core.choiceGrid;
    }

    public static MainFrame showing(final Board board) {
        final Core core = new Core(new Setup(), new HiliteRelayPool(), board, null).withChoiceGrid();
        return JFrames.builder(() -> new MainFrame(core))
                      .setTitle("Sudoku")
                      .setDefaultCloseOperation(DISPOSE_ON_CLOSE)
                      .add(centerPane(core), BorderLayout.CENTER)
                      .add(eastPane(core), BorderLayout.EAST)
                      .pack()
                      .build();
    }

    private record Core(Setup setup, HiliteRelayPool hiliteRelayPool, Board board, ChoiceGrid choiceGrid) {
        private Core withChoiceGrid() {
            return new Core(setup, hiliteRelayPool, board, new ChoiceGrid(board,hiliteRelayPool, setup));
        }
    }

    private class ACTN_AUTOHINT extends XCheckBox implements Listener<Setup> {
        public ACTN_AUTOHINT() {
            super("Vorschläge");
            addActionListener(this);
            setup.getRegister().add(this);
        }

        public final void actionPerformed(final ActionEvent e) {
            setup.setAutoHinting(isSelected());
        }

        public final void pass(final Setup sender) {
            setSelected(sender.getAutoHinting());
        }
    }

    private static JButton actionClone(final Core core) {
        return JButtons.builder()
                       .setText("Klonen")
                       .on(Event.ACTION_PERFORMED, e -> showing(new Board(core.board)).setVisible(true))
                       .build();
    }

    private static JPanel actionGrid(final Core core) {
        return JPanels.builder()
                      .setLayout(new GridLayout(0, 1, 1, 1))
                      .setBorder(BorderFactory.createEmptyBorder(1, 1, 1, 1))
                      .add(actionClone(core))
                      .add(actionReset(core))
                      .add(actionHint(core))
                      .add(new ACTN_HILITE())
                      .add(new ACTN_AUTOHINT())
                      .build();
    }

    private class ACTN_HILITE extends XCheckBox implements Listener<Setup> {
        public ACTN_HILITE() {
            super("Gruppenhinweis");
            addActionListener(this);
            setup.getRegister().add(this);
        }

        public final void actionPerformed(final ActionEvent e) {
            setup.setGroupHiliting(isSelected());
        }

        public final void pass(final Setup sender) {
            setSelected(sender.getGroupHiliting());
        }
    }

    private static JButton actionHint(final Core core) {
        return JButtons.builder()
                       .setText("Vorschläge")
                       .on(Event.ACTION_PERFORMED, e -> actionHintPerformed(core))
                       .build();
    }

    private static void actionHintPerformed(final Core core) {
        for (final Hint h : core.board.getHints()) {
            core.choiceGrid.getPotentialCell(h.getChoice(), h.getNumber())
                           .setHinting(true);
        }
    }

    private static JButton actionReset(final Core core) {
        return JButtons.builder()
                       .setText("Reset")
                       .on(Event.ACTION_PERFORMED, e -> core.board.reset())
                       .build();
    }

    private static JPanel centerPane(final Core core) {
        return JPanels.builder()
                      .setLayout(new GridBagLayout())
                      .add(new ColInfoGrid(core.board, core.hiliteRelayPool, core.setup), constraints(3, 1, 9, 1))
                      .add(new RowInfoGrid(core.board, core.hiliteRelayPool, core.setup), constraints(1, 3, 1, 9))
                      .add(new AreaInfoGrid(core.board, core.hiliteRelayPool, core.setup), constraints(13, 3, 3, 3))
                      .add(new JPanel(), constraints(0, 0, 1, 1))
                      .add(new JPanel(), constraints(2, 2, 1, 1))
                      .add(new JPanel(), constraints(12, 2, 1, 1))
                      .add(new JPanel(), constraints(16, 13, 1, 1))
                      .add(core.choiceGrid, constraints(3, 3, 9, 9))
                      .build();
    }

    private static GridBagConstraints constraints(final int gridx, final int gridy,
                                                  final int gridwidth, final int gridheight) {
        return new GridBagConstraints(gridx, gridy, gridwidth, gridheight, 0.0, 0.0, 10, 1, INSETS, 0, 0);
    }

    private static JPanel eastPane(final Core core) {
        return JPanels.builder()
                      .setLayout(new BorderLayout())
                      .setBorder(BorderFactory.createRaisedBevelBorder())
                      .add(actionGrid(core), BorderLayout.NORTH)
                      .add(new INFO_GRID(core), BorderLayout.SOUTH)
                      .build();
    }

    private class INFO_GRID extends JPanel implements Listener<Choice.Message> {
        private final INFO_LBL[] m_Labels;
        private int[] m_Values = new int[10];

        public INFO_GRID(final Board s) {
            super(new GridLayout(0, 2, 1, 1));
            setBorder(BorderFactory.createEmptyBorder(1, 1, 1, 1));
            this.m_Values = new int[Numbers.getCount() + 1];
            this.m_Labels = new INFO_LBL[Numbers.getCount() + 1];

            int y;
            for(y = 1; y < m_Labels.length; ++y) {
                m_Labels[y] = MainFrame.this.new INFO_LBL(0);
                add(MainFrame.this.new INFO_LBL(y));
                add(m_Labels[y]);
            }

            m_Labels[0] = MainFrame.this.new INFO_LBL(0);
            add(MainFrame.this.new INFO_LBL("Gesamt"));
            add(m_Labels[0]);

            for(y = 0; y < Numbers.getCount(); ++y) {
                for(int x = 0; x < Numbers.getCount(); ++x) {
                    s.getChoice(x, y).getRegister().add(this);
                }
            }

        }

        public final void pass(final Choice.Message message) {
            final de.team33.sudoku.Number newNumber = message.getSender().getNumber();
            final Number oldNumber = message.getOldNumber();
            if (oldNumber != newNumber) {
                int var10002;
                final int i;
                if (oldNumber == null) {
                    i = newNumber.getIdentity() + 1;
                    var10002 = m_Values[i]++;
                    m_Labels[i].setText("" + m_Values[i]);
                    var10002 = m_Values[0]++;
                    m_Labels[0].setText("" + m_Values[0]);
                } else if (newNumber == null) {
                    i = oldNumber.getIdentity() + 1;
                    var10002 = m_Values[i]--;
                    m_Labels[i].setText("" + m_Values[i]);
                    var10002 = m_Values[0]--;
                    m_Labels[0].setText("" + m_Values[0]);
                }
            }

        }
    }

    private class INFO_LBL extends JLabel {
        private static final long serialVersionUID = 612779410579559915L;

        public INFO_LBL(final String s) {
            super(s);
            setHorizontalAlignment(0);
            setVerticalAlignment(0);
            setOpaque(true);
            setBackground(Color.WHITE);
        }

        public INFO_LBL(final int i) {
            this("" + i);
        }
    }
}
