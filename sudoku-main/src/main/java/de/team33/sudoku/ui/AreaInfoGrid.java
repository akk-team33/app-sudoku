package de.team33.sudoku.ui;

import de.team33.sudoku.HiliteRelayPool;
import de.team33.sudoku.Numbers;
import de.team33.sudoku.Setup;
import de.team33.sudoku.Sudoku;

public class AreaInfoGrid extends BasicInfoGrid {
    public AreaInfoGrid(Sudoku s, HiliteRelayPool frp, Setup su) {
        super(1, 1);
        this.add(new AREA(s, Numbers.getRadix(), frp, su));
    }

    private static class AREA extends BasicInfoGrid.Area {
        public AREA(Sudoku s, int radix, HiliteRelayPool frp, Setup su) {
            super(radix, radix);
            int z = 0;

            for(int Z = radix * radix; z < Z; ++z) {
                this.add(new CELL(s, z, frp, su));
            }

        }
    }

    private static class CELL extends InfoCell {
        public CELL(Sudoku s, int z, HiliteRelayPool frp, Setup su) {
            super(s.getAreaGrp(z).getPotential(), frp.getAreaRelay(z), su);
        }
    }
}
