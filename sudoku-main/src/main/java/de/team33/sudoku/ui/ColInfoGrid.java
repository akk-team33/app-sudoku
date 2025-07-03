//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package de.team33.sudoku.ui;

import de.team33.sudoku.HiliteRelayPool;
import de.team33.sudoku.Numbers;
import de.team33.sudoku.Setup;
import de.team33.sudoku.Sudoku;

public class ColInfoGrid extends BasicInfoGrid {
    public ColInfoGrid(Sudoku s, HiliteRelayPool frp, Setup su) {
        this(s, Numbers.getRadix(), frp, su);
    }

    private ColInfoGrid(Sudoku s, int radix, HiliteRelayPool frp, Setup su) {
        super(1, radix);

        for(int x = 0; x < radix; ++x) {
            this.add(new AREA(s, x, radix, frp, su));
        }

    }

    private static class AREA extends BasicInfoGrid.Area {
        public AREA(Sudoku s, int x0, int radix, HiliteRelayPool frp, Setup su) {
            super(1, radix);

            for(int dx = 0; dx < radix; ++dx) {
                this.add(new CELL(s, radix * x0 + dx, frp, su));
            }

        }
    }

    private static class CELL extends InfoCell {
        public CELL(Sudoku s, int x, HiliteRelayPool frp, Setup su) {
            super(s.getColGrp(x).getPotential(), frp.getColRelay(x), su);
        }
    }
}
