package de.team33.sudoku.ui;

import de.team33.sudoku.HiliteRelayPool;
import de.team33.sudoku.Numbers;
import de.team33.sudoku.Setup;
import de.team33.sudoku.Sudoku;

public class RowInfoGrid extends BasicInfoGrid {
    private static final long serialVersionUID = -5679215683601638666L;

    public RowInfoGrid(final Sudoku s, final HiliteRelayPool frp, final Setup su) {
        this(s, Numbers.getRadix(), frp, su);
    }

    private RowInfoGrid(final Sudoku s, final int radix, final HiliteRelayPool frp, final Setup su) {
        super(radix, 1);

        for(int y = 0; y < radix; ++y) {
            add(new AREA(s, y, radix, frp, su));
        }

    }

    private static class AREA extends BasicInfoGrid.Area {
        private static final long serialVersionUID = 7246799809664980919L;

        public AREA(final Sudoku s, final int y0, final int radix, final HiliteRelayPool frp, final Setup su) {
            super(radix, 1);

            for(int dy = 0; dy < radix; ++dy) {
                add(new CELL(s, radix * y0 + dy, frp, su));
            }

        }
    }

    private static class CELL extends InfoCell {
        private static final long serialVersionUID = 3240851759436494284L;

        public CELL(final Sudoku s, final int y, final HiliteRelayPool frp, final Setup su) {
            super(s.getRowGrp(y).getPotential(), frp.getRowRelay(y), su);
        }
    }
}
