//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package de.team33.sudoku;

public class HiliteMessage {
    private Object m_Spec;
    private boolean m_Hilited;

    public HiliteMessage(Object spec, boolean hilited) {
        this.m_Spec = spec;
        this.m_Hilited = hilited;
    }

    public Object getSpec() {
        return this.m_Spec;
    }

    public boolean getHilited() {
        return this.m_Hilited;
    }
}
