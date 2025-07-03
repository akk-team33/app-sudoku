//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package de.team33.sudoku;

import de.team33.messaging.simplex.Sender;

public class Setup extends Sender<Setup> {
    private boolean m_AutoHinting = false;
    private boolean m_GroupHiliting = false;

    public Setup() {
    }

    public boolean getAutoHinting() {
        return this.m_AutoHinting;
    }

    public boolean getGroupHiliting() {
        return this.m_GroupHiliting;
    }

    public void setAutoHinting(boolean b) {
        if (this.m_AutoHinting != b) {
            this.m_AutoHinting = b;
            this.fire(this);
        }

    }

    public void setGroupHiliting(boolean b) {
        if (this.m_GroupHiliting != b) {
            this.m_GroupHiliting = b;
            this.fire(this);
        }

    }
}
