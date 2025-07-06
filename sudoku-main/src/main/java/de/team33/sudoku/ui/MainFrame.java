package de.team33.sudoku.ui;

import de.team33.sudoku.Choice;
import de.team33.sudoku.Number;
import de.team33.sudoku.*;
import de.team33.swinx.XButton;
import de.team33.swinx.XCheckBox;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.Iterator;
import java.util.function.Consumer;

public class MainFrame extends JFrame {
    private static final long serialVersionUID = 1831349627175488437L;
    private final Board m_Board;
    private ChoiceGrid m_ChoiceGrid;
    private final Setup m_Setup = new Setup();
    private final HiliteRelayPool m_FlagRouterPool = new HiliteRelayPool();

    public MainFrame(final Board s) {
        super("Sudoku");
        this.m_Board = s;
        setDefaultCloseOperation(2);
        add(new CENTER_PANE(s), "Center");
        add(new EAST_PANE(s), "East");
        pack();
    }

    private class ACTN_AUTOHINT extends XCheckBox implements Consumer<Setup> {
        public ACTN_AUTOHINT() {
            super("Vorschläge");
            addActionListener(this);
            m_Setup.getRegister().add(this);
        }

        public final void actionPerformed(final ActionEvent e) {
            m_Setup.setAutoHinting(isSelected());
        }

        public final void accept(final Setup sender) {
            setSelected(sender.getAutoHinting());
        }
    }

    private class ACTN_CLONE extends XButton {
        private static final long serialVersionUID = 1790144176265133751L;

        public ACTN_CLONE() {
            super("Klonen");
        }

        public final void actionPerformed(final ActionEvent e) {
            (new MainFrame(new Board(m_Board))).setVisible(true);
        }
    }

    private class ACTN_GRID extends JPanel {
        private static final long serialVersionUID = 4308135792072114290L;

        public ACTN_GRID() {
            super(new GridLayout(0, 1, 1, 1));
            setBorder(BorderFactory.createEmptyBorder(1, 1, 1, 1));
            add(MainFrame.this.new ACTN_CLONE());
            add(MainFrame.this.new ACTN_RST());
            add(MainFrame.this.new ACTN_HINT());
            add(MainFrame.this.new ACTN_HILITE());
            add(MainFrame.this.new ACTN_AUTOHINT());
        }
    }

    private class ACTN_HILITE extends XCheckBox implements Consumer<Setup> {
        public ACTN_HILITE() {
            super("Gruppenhinweis");
            addActionListener(this);
            m_Setup.getRegister().add(this);
        }

        public final void actionPerformed(final ActionEvent e) {
            m_Setup.setGroupHiliting(isSelected());
        }

        public final void accept(final Setup sender) {
            setSelected(sender.getGroupHiliting());
        }
    }

    private class ACTN_HINT extends XButton {
        private static final long serialVersionUID = -4563457510867828343L;

        public ACTN_HINT() {
            super("Vorschläge");
        }

        public final void actionPerformed(final ActionEvent e) {
            final Iterator var3 = m_Board.getHints().iterator();

            while(var3.hasNext()) {
                final Hint h = (Hint)var3.next();
                m_ChoiceGrid.getPotentialCell(h.getChoice(), h.getNumber()).setHinting(true);
            }

        }
    }

    private class ACTN_RST extends XButton {
        private static final long serialVersionUID = -2896338482672752542L;

        public ACTN_RST() {
            super("Reset");
        }

        public final void actionPerformed(final ActionEvent e) {
            m_Board.reset();
        }
    }

    private class CENTER_PANE extends JPanel {
        private static final long serialVersionUID = -3672158492746235449L;

        public CENTER_PANE(final Board s) {
            super(new GridBagLayout());
            add(new ColInfoGrid(s, m_FlagRouterPool, m_Setup), MainFrame.this.new CONSTRAINTS(3, 1, 9, 1));
            add(new RowInfoGrid(s, m_FlagRouterPool, m_Setup), MainFrame.this.new CONSTRAINTS(1, 3, 1, 9));
            add(new AreaInfoGrid(s, m_FlagRouterPool, m_Setup), MainFrame.this.new CONSTRAINTS(13, 3, 3, 3));
            add(new JPanel(), MainFrame.this.new CONSTRAINTS(0, 0, 1, 1));
            add(new JPanel(), MainFrame.this.new CONSTRAINTS(2, 2, 1, 1));
            add(new JPanel(), MainFrame.this.new CONSTRAINTS(12, 2, 1, 1));
            add(new JPanel(), MainFrame.this.new CONSTRAINTS(16, 13, 1, 1));
            final ChoiceGrid var10003 = new ChoiceGrid(s, m_FlagRouterPool, m_Setup);
            MainFrame.this.m_ChoiceGrid = var10003;
            add(var10003, MainFrame.this.new CONSTRAINTS(3, 3, 9, 9));
        }
    }

    private class CONSTRAINTS extends GridBagConstraints {
        private static final long serialVersionUID = 7841150408814921710L;

        public CONSTRAINTS(final int gridx, final int gridy, final int gridwidth, final int gridheight) {
            super(gridx, gridy, gridwidth, gridheight, 0.0, 0.0, 10, 1, new Insets(0, 0, 0, 0), 0, 0);
        }
    }

    private class EAST_PANE extends JPanel {
        private static final long serialVersionUID = -1633863694402998797L;

        public EAST_PANE(final Board s) {
            super(new BorderLayout());
            setBorder(BorderFactory.createRaisedBevelBorder());
            add(MainFrame.this.new ACTN_GRID(), "North");
            add(MainFrame.this.new INFO_GRID(s), "South");
        }
    }

    private class INFO_GRID extends JPanel implements Consumer<Choice.Message> {
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

        public final void accept(final Choice.Message message) {
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
