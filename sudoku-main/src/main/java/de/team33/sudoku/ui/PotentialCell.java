//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package de.team33.sudoku.ui;

import de.team33.messaging.Listener;
import de.team33.messaging.simplex.Originator;
import de.team33.messaging.simplex.Register;
import de.team33.messaging.simplex.Relay;
import de.team33.messaging.simplex.Router;
import de.team33.sudoku.HiliteMessage;
import de.team33.sudoku.Number;
import de.team33.sudoku.Potential;
import de.team33.sudoku.Setup;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class PotentialCell extends JLabel implements Originator<PotentialCell.SelectMessage> {
    private Setup setup;
    private Number number;
    private int flags;
    private Potential potential;
    private Relay<HiliteMessage> hiliteRelay;
    private Router<SelectMessage> selectRouter = new Router();
    private static final int FONT_SIZE = 11;
    private static final int EXCLUDED = 1;
    private static final int TOUCHED = 2;
    private static final int HILITED = 4;
    private static final int HINTING = 8;

    public PotentialCell(Potential p, Number n, Relay<HiliteMessage> relay, Setup s) {
        super(n.getDisplay());
        this.setup = s;
        this.setHorizontalAlignment(0);
        this.setVerticalAlignment(0);
        this._updateFace();
        this.number = n;
        (this.potential = p).getRegister().add(new PotentialListener());
        (this.hiliteRelay = relay).add(new HiliteListener());
        this.addMouseListener(new ADAPTER());
    }

    public Register<SelectMessage> getRegister() {
        return this.selectRouter;
    }

    public Dimension getPreferredSize() {
        Dimension retValue = super.getPreferredSize();
        retValue.width = retValue.height;
        return retValue;
    }

    public Dimension getMinimumSize() {
        return this.getPreferredSize();
    }

    public Dimension getMaximumSize() {
        return this.getPreferredSize();
    }

    public void setVisible(boolean aFlag) {
        if (!aFlag && this._isTouched()) {
            this._setTouched(false);
        }

        super.setVisible(aFlag);
    }

    public void setHinting(boolean hinting) {
        this._setHinting(hinting);
    }

    private boolean _isFlag(int flag) {
        return (this.flags & flag) != 0;
    }

    private void _setFlag(int flag, boolean set) {
        if (set) {
            this.flags |= flag;
        } else {
            this.flags &= ~flag;
        }

    }

    private boolean _isExcluded() {
        return this._isFlag(1);
    }

    private boolean _isTouched() {
        return this._isFlag(2);
    }

    private boolean _isHilited() {
        return this._isFlag(4);
    }

    private boolean _isHinting() {
        return this._isFlag(8);
    }

    private void _updateFace() {
        this.setOpaque(!this._isExcluded());
        this.setForeground(this._isExcluded() ? Color.WHITE : Color.BLACK);
        boolean hilited = !this._isExcluded() && (this._isHilited() || this._isHinting());
        this.setFont(new Font(this.getFont().getName(), hilited ? 1 : 0, 11));
        this.setBackground(hilited ? new Color(255, 255, 128) : new Color(255, 255, 255));
    }

    private void _setExcluded(boolean excluded) {
        if (this._isExcluded() != excluded) {
            this._setFlag(1, excluded);
            this._updateFace();
        }

    }

    private void _setTouched(boolean touched) {
        if (this._isTouched() != touched) {
            this._setFlag(2, touched);
            if (touched) {
                this._setHinting(false);
            }

            this._setHilited(touched);
            if (this.setup.getGroupHiliting()) {
                this.hiliteRelay.route(new HiliteMessage(this.number, touched));
            }
        }

    }

    private void _setHilited(boolean hilited) {
        if (this._isHilited() != hilited) {
            this._setFlag(4, hilited);
            this._updateFace();
        }

    }

    private void _setHinting(boolean hinting) {
        if (this._isHinting() != hinting) {
            this._setFlag(8, hinting);
            this._updateFace();
        }

    }

    private class ADAPTER extends MouseAdapter {
        private ADAPTER() {
        }

        public void mouseReleased(MouseEvent e) {
            if (e.getButton() == 1) {
                if (!PotentialCell.this._isExcluded()) {
                    PotentialCell.this.selectRouter.route(PotentialCell.this.new MESSAGE());
                }
            } else if (e.getButton() == 3) {
                PotentialCell.this.potential.include(PotentialCell.this.number, PotentialCell.this._isExcluded());
                PotentialCell.this._setTouched(!PotentialCell.this._isExcluded());
            }

        }

        public void mouseEntered(MouseEvent e) {
            if (!PotentialCell.this._isExcluded()) {
                PotentialCell.this._setTouched(true);
            }

        }

        public void mouseExited(MouseEvent e) {
            if (!PotentialCell.this._isExcluded()) {
                PotentialCell.this._setTouched(false);
            }

        }
    }

    private class HiliteListener implements Listener<HiliteMessage> {
        private HiliteListener() {
        }

        public void pass(HiliteMessage message) {
            if (message.getSpec().equals(PotentialCell.this.number)) {
                PotentialCell.this._setHilited(message.getHilited());
            }

        }
    }

    private class MESSAGE implements SelectMessage {
        private MESSAGE() {
        }

        public Number getNumber() {
            return PotentialCell.this.number;
        }

        public PotentialCell getSender() {
            return PotentialCell.this;
        }
    }

    private class PotentialListener implements Listener<Potential.Message> {
        private PotentialListener() {
        }

        public void pass(Potential.Message message) {
            PotentialCell.this._setExcluded(!message.getSender().includes(PotentialCell.this.number));
        }
    }

    public interface SelectMessage {
        PotentialCell getSender();

        Number getNumber();
    }
}
