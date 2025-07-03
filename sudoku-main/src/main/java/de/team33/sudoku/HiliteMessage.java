package de.team33.sudoku;

public class HiliteMessage {
    private final Object m_Spec;
    private final boolean m_Hilited;

    public HiliteMessage(final Object spec, final boolean hilited) {
        this.m_Spec = spec;
        this.m_Hilited = hilited;
    }

    public final Object getSpec() {
        return m_Spec;
    }

    public final boolean getHilited() {
        return m_Hilited;
    }
}
