package de.team33.sudoku.ui;

import de.team33.messaging.Listener;
import de.team33.sudoku.Choice;
import de.team33.sudoku.Number;
import de.team33.sudoku.*;
import de.team33.swinx.XButton;
import de.team33.swinx.XCheckBox;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.Iterator;

public class MainFrame extends JFrame {
    private static final long serialVersionUID = 1831349627175488437L;
    private Sudoku m_Sudoku;
    private ChoiceGrid m_ChoiceGrid;
    private Setup m_Setup = new Setup();
    private HiliteRelayPool m_FlagRouterPool = new HiliteRelayPool();

    public MainFrame(Sudoku s) {
        super("Sudoku");
        this.m_Sudoku = s;
        this.setDefaultCloseOperation(2);
        this.add(new CENTER_PANE(s), "Center");
        this.add(new EAST_PANE(s), "East");
        this.pack();
    }

    private class ACTN_AUTOHINT extends XCheckBox implements Listener<Setup> {
        public ACTN_AUTOHINT() {
            super("Vorschläge");
            this.addActionListener(this);
            MainFrame.this.m_Setup.getRegister().add(this);
        }

        public void actionPerformed(ActionEvent e) {
            MainFrame.this.m_Setup.setAutoHinting(this.isSelected());
        }

        public void pass(Setup sender) {
            this.setSelected(sender.getAutoHinting());
        }
    }

    private class ACTN_CLONE extends XButton {
        private static final long serialVersionUID = 1790144176265133751L;

        public ACTN_CLONE() {
            super("Klonen");
        }

        public void actionPerformed(ActionEvent e) {
            (new MainFrame(new Sudoku(MainFrame.this.m_Sudoku))).setVisible(true);
        }
    }

    private class ACTN_GRID extends JPanel {
        private static final long serialVersionUID = 4308135792072114290L;

        public ACTN_GRID() {
            super(new GridLayout(0, 1, 1, 1));
            this.setBorder(BorderFactory.createEmptyBorder(1, 1, 1, 1));
            this.add(MainFrame.this.new ACTN_CLONE());
            this.add(MainFrame.this.new ACTN_RST());
            this.add(MainFrame.this.new ACTN_HINT());
            this.add(MainFrame.this.new ACTN_HILITE());
            this.add(MainFrame.this.new ACTN_AUTOHINT());
        }
    }

    private class ACTN_HILITE extends XCheckBox implements Listener<Setup> {
        public ACTN_HILITE() {
            super("Gruppenhinweis");
            this.addActionListener(this);
            MainFrame.this.m_Setup.getRegister().add(this);
        }

        public void actionPerformed(ActionEvent e) {
            MainFrame.this.m_Setup.setGroupHiliting(this.isSelected());
        }

        public void pass(Setup sender) {
            this.setSelected(sender.getGroupHiliting());
        }
    }

    private class ACTN_HINT extends XButton {
        private static final long serialVersionUID = -4563457510867828343L;

        public ACTN_HINT() {
            super("Vorschläge");
        }

        public void actionPerformed(ActionEvent e) {
            Iterator var3 = MainFrame.this.m_Sudoku.getHints().iterator();

            while(var3.hasNext()) {
                Hint h = (Hint)var3.next();
                MainFrame.this.m_ChoiceGrid.getPotentialCell(h.getChoice(), h.getNumber()).setHinting(true);
            }

        }
    }

    private class ACTN_RST extends XButton {
        private static final long serialVersionUID = -2896338482672752542L;

        public ACTN_RST() {
            super("Reset");
        }

        public void actionPerformed(ActionEvent e) {
            MainFrame.this.m_Sudoku.reset();
        }
    }

    private class CENTER_PANE extends JPanel {
        private static final long serialVersionUID = -3672158492746235449L;

        public CENTER_PANE(Sudoku s) {
            super(new GridBagLayout());
            this.add(new ColInfoGrid(s, MainFrame.this.m_FlagRouterPool, MainFrame.this.m_Setup), MainFrame.this.new CONSTRAINTS(3, 1, 9, 1));
            this.add(new RowInfoGrid(s, MainFrame.this.m_FlagRouterPool, MainFrame.this.m_Setup), MainFrame.this.new CONSTRAINTS(1, 3, 1, 9));
            this.add(new AreaInfoGrid(s, MainFrame.this.m_FlagRouterPool, MainFrame.this.m_Setup), MainFrame.this.new CONSTRAINTS(13, 3, 3, 3));
            this.add(new JPanel(), MainFrame.this.new CONSTRAINTS(0, 0, 1, 1));
            this.add(new JPanel(), MainFrame.this.new CONSTRAINTS(2, 2, 1, 1));
            this.add(new JPanel(), MainFrame.this.new CONSTRAINTS(12, 2, 1, 1));
            this.add(new JPanel(), MainFrame.this.new CONSTRAINTS(16, 13, 1, 1));
            ChoiceGrid var10003 = new ChoiceGrid(s, MainFrame.this.m_FlagRouterPool, MainFrame.this.m_Setup);
            MainFrame.this.m_ChoiceGrid = var10003;
            this.add(var10003, MainFrame.this.new CONSTRAINTS(3, 3, 9, 9));
        }
    }

    private class CONSTRAINTS extends GridBagConstraints {
        private static final long serialVersionUID = 7841150408814921710L;

        public CONSTRAINTS(int gridx, int gridy, int gridwidth, int gridheight) {
            super(gridx, gridy, gridwidth, gridheight, 0.0, 0.0, 10, 1, new Insets(0, 0, 0, 0), 0, 0);
        }
    }

    private class EAST_PANE extends JPanel {
        private static final long serialVersionUID = -1633863694402998797L;

        public EAST_PANE(Sudoku s) {
            super(new BorderLayout());
            this.setBorder(BorderFactory.createRaisedBevelBorder());
            this.add(MainFrame.this.new ACTN_GRID(), "North");
            this.add(MainFrame.this.new INFO_GRID(s), "South");
        }
    }

    private class INFO_GRID extends JPanel implements Listener<Choice.Message> {
        private INFO_LBL[] m_Labels;
        private int[] m_Values = new int[10];

        public INFO_GRID(Sudoku s) {
            super(new GridLayout(0, 2, 1, 1));
            this.setBorder(BorderFactory.createEmptyBorder(1, 1, 1, 1));
            this.m_Values = new int[Numbers.getCount() + 1];
            this.m_Labels = new INFO_LBL[Numbers.getCount() + 1];

            int y;
            for(y = 1; y < this.m_Labels.length; ++y) {
                this.m_Labels[y] = MainFrame.this.new INFO_LBL(0);
                this.add(MainFrame.this.new INFO_LBL(y));
                this.add(this.m_Labels[y]);
            }

            this.m_Labels[0] = MainFrame.this.new INFO_LBL(0);
            this.add(MainFrame.this.new INFO_LBL("Gesamt"));
            this.add(this.m_Labels[0]);

            for(y = 0; y < Numbers.getCount(); ++y) {
                for(int x = 0; x < Numbers.getCount(); ++x) {
                    s.getChoice(x, y).getRegister().add(this);
                }
            }

        }

        public void pass(Choice.Message message) {
            de.team33.sudoku.Number newNumber = message.getSender().getNumber();
            Number oldNumber = message.getOldNumber();
            if (oldNumber != newNumber) {
                int var10002;
                int i;
                if (oldNumber == null) {
                    i = newNumber.getIdentity() + 1;
                    var10002 = this.m_Values[i]++;
                    this.m_Labels[i].setText("" + this.m_Values[i]);
                    var10002 = this.m_Values[0]++;
                    this.m_Labels[0].setText("" + this.m_Values[0]);
                } else if (newNumber == null) {
                    i = oldNumber.getIdentity() + 1;
                    var10002 = this.m_Values[i]--;
                    this.m_Labels[i].setText("" + this.m_Values[i]);
                    var10002 = this.m_Values[0]--;
                    this.m_Labels[0].setText("" + this.m_Values[0]);
                }
            }

        }
    }

    private class INFO_LBL extends JLabel {
        private static final long serialVersionUID = 612779410579559915L;

        public INFO_LBL(String s) {
            super(s);
            this.setHorizontalAlignment(0);
            this.setVerticalAlignment(0);
            this.setOpaque(true);
            this.setBackground(Color.WHITE);
        }

        public INFO_LBL(int i) {
            this("" + i);
        }
    }
}
