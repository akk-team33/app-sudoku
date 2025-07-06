package de.team33.sudoku.ui;

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
import java.util.function.Consumer;

public class PotentialCell extends JLabel implements Originator<PotentialCell.SelectMessage> {
    private final Setup setup;
    private final Number number;
    private int flags;
    private final Potential potential;
    private final Relay<HiliteMessage> hiliteRelay;
    private final Router<SelectMessage> selectRouter = new Router();
    private static final int FONT_SIZE = 11;
    private static final int EXCLUDED = 1;
    private static final int TOUCHED = 2;
    private static final int HILITED = 4;
    private static final int HINTING = 8;

    public PotentialCell(final Potential p, final Number n, final Relay<HiliteMessage> relay, final Setup s) {
        super(n.getDisplay());
        this.setup = s;
        setHorizontalAlignment(0);
        setVerticalAlignment(0);
        _updateFace();
        this.number = n;
        (this.potential = p).getRegister().add(new PotentialListener());
        (this.hiliteRelay = relay).add(new HiliteListener());
        addMouseListener(new ADAPTER());
    }

    public final Register<SelectMessage> getRegister() {
        return selectRouter;
    }

    public final Dimension getPreferredSize() {
        final Dimension retValue = super.getPreferredSize();
        retValue.width = retValue.height;
        return retValue;
    }

    public final Dimension getMinimumSize() {
        return getPreferredSize();
    }

    public final Dimension getMaximumSize() {
        return getPreferredSize();
    }

    public final void setVisible(final boolean aFlag) {
        if (!aFlag && _isTouched()) {
            _setTouched(false);
        }

        super.setVisible(aFlag);
    }

    public final void setHinting(final boolean hinting) {
        _setHinting(hinting);
    }

    private boolean _isFlag(final int flag) {
        return (flags & flag) != 0;
    }

    private void _setFlag(final int flag, final boolean set) {
        if (set) {
            this.flags |= flag;
        } else {
            this.flags &= ~flag;
        }

    }

    private boolean _isExcluded() {
        return _isFlag(1);
    }

    private boolean _isTouched() {
        return _isFlag(2);
    }

    private boolean _isHilited() {
        return _isFlag(4);
    }

    private boolean _isHinting() {
        return _isFlag(8);
    }

    private void _updateFace() {
        setOpaque(!_isExcluded());
        setForeground(_isExcluded() ? Color.WHITE : Color.BLACK);
        final boolean hilited = !_isExcluded() && (_isHilited() || _isHinting());
        setFont(new Font(getFont().getName(), hilited ? 1 : 0, 11));
        setBackground(hilited ? new Color(255, 255, 128) : new Color(255, 255, 255));
    }

    private void _setExcluded(final boolean excluded) {
        if (_isExcluded() != excluded) {
            _setFlag(1, excluded);
            _updateFace();
        }

    }

    private void _setTouched(final boolean touched) {
        if (_isTouched() != touched) {
            _setFlag(2, touched);
            if (touched) {
                _setHinting(false);
            }

            _setHilited(touched);
            if (setup.getGroupHiliting()) {
                hiliteRelay.route(new HiliteMessage(number, touched));
            }
        }

    }

    private void _setHilited(final boolean hilited) {
        if (_isHilited() != hilited) {
            _setFlag(4, hilited);
            _updateFace();
        }

    }

    private void _setHinting(final boolean hinting) {
        if (_isHinting() != hinting) {
            _setFlag(8, hinting);
            _updateFace();
        }

    }

    private class ADAPTER extends MouseAdapter {

        public final void mouseReleased(final MouseEvent e) {
            if (e.getButton() == 1) {
                if (!_isExcluded()) {
                    selectRouter.route(PotentialCell.this.new MESSAGE());
                }
            } else if (e.getButton() == 3) {
                potential.include(number, _isExcluded());
                _setTouched(!_isExcluded());
            }

        }

        public final void mouseEntered(final MouseEvent e) {
            if (!_isExcluded()) {
                _setTouched(true);
            }

        }

        public final void mouseExited(final MouseEvent e) {
            if (!_isExcluded()) {
                _setTouched(false);
            }

        }
    }

    private class HiliteListener implements Consumer<HiliteMessage> {

        public final void accept(final HiliteMessage message) {
            if (message.getSpec().equals(number)) {
                _setHilited(message.getHilited());
            }

        }
    }

    private class MESSAGE implements SelectMessage {

        public final Number getNumber() {
            return number;
        }

        public final PotentialCell getSender() {
            return PotentialCell.this;
        }
    }

    private class PotentialListener implements Consumer<Potential.Message> {

        public final void accept(final Potential.Message message) {
            _setExcluded(!message.getSender().includes(number));
        }
    }

    public interface SelectMessage {
        PotentialCell getSender();

        Number getNumber();
    }
}
