package de.team33.sudoku.ui;

import de.team33.sudoku.Board;
import de.team33.sudoku.HiliteRelayPool;
import de.team33.sudoku.Numbers;
import de.team33.sudoku.Setup;

public class AreaInfoGrid extends BasicInfoGrid {
    public AreaInfoGrid(final Board s, final HiliteRelayPool frp, final Setup su) {
        super(1, 1);
        add(new AREA(s, Numbers.getRadix(), frp, su));
    }

    private static class AREA extends BasicInfoGrid.Area {
        public AREA(final Board s, final int radix, final HiliteRelayPool frp, final Setup su) {
            super(radix, radix);
            int z = 0;

            for(final int Z = radix * radix; z < Z; ++z) {
                add(new CELL(s, z, frp, su));
            }

        }
    }

    private static class CELL extends InfoCell {
        public CELL(final Board s, final int z, final HiliteRelayPool frp, final Setup su) {
            super(s.getAreaGrp(z).getPotential(), frp.getAreaRelay(z), su);
        }
    }
}
