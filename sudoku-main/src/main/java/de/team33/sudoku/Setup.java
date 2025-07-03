package de.team33.sudoku;

import de.team33.messaging.simplex.Sender;

public class Setup extends Sender<Setup> {
    private boolean m_AutoHinting = false;
    private boolean m_GroupHiliting = false;

    public final boolean getAutoHinting() {
        return m_AutoHinting;
    }

    public final boolean getGroupHiliting() {
        return m_GroupHiliting;
    }

    public final void setAutoHinting(final boolean b) {
        if (m_AutoHinting != b) {
            this.m_AutoHinting = b;
            fire(this);
        }

    }

    public final void setGroupHiliting(final boolean b) {
        if (m_GroupHiliting != b) {
            this.m_GroupHiliting = b;
            fire(this);
        }

    }
}
