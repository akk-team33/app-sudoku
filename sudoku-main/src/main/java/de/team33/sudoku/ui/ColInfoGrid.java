package de.team33.sudoku.ui;

import de.team33.sudoku.Board;
import de.team33.sudoku.HiliteRelayPool;
import de.team33.sudoku.Numbers;
import de.team33.sudoku.Setup;

public class ColInfoGrid extends BasicInfoGrid {
    public ColInfoGrid(final Board s, final HiliteRelayPool frp, final Setup su) {
        this(s, Numbers.getRadix(), frp, su);
    }

    private ColInfoGrid(final Board s, final int radix, final HiliteRelayPool frp, final Setup su) {
        super(1, radix);

        for(int x = 0; x < radix; ++x) {
            add(new AREA(s, x, radix, frp, su));
        }

    }

    private static class AREA extends BasicInfoGrid.Area {
        public AREA(final Board s, final int x0, final int radix, final HiliteRelayPool frp, final Setup su) {
            super(1, radix);

            for(int dx = 0; dx < radix; ++dx) {
                add(new CELL(s, radix * x0 + dx, frp, su));
            }

        }
    }

    private static class CELL extends InfoCell {
        public CELL(final Board s, final int x, final HiliteRelayPool frp, final Setup su) {
            super(s.getColGrp(x).getPotential(), frp.getColRelay(x), su);
        }
    }
}
