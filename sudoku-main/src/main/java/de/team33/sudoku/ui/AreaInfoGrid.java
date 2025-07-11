package de.team33.sudoku.ui;

import de.team33.sphinx.alpha.visual.JPanels;
import de.team33.sudoku.Board;
import de.team33.sudoku.HiliteRelayPool;
import de.team33.sudoku.Setup;

import javax.swing.*;
import java.util.function.Consumer;
import java.util.stream.IntStream;

final class AreaInfoGrid {

    private AreaInfoGrid() {
    }

    static JPanel panel(final Board board, final HiliteRelayPool pool, final Setup setup) {
        return JPanels.builder(() -> new BasicInfoGrid(1, 1))
                      .add(area(board, board.radix(), pool, setup))
                      .build();
    }

    private static BasicInfoGrid.Area area(final Board board, final int radix, final HiliteRelayPool hiliteRelayPool, final Setup setup) {
        return JPanels.builder(() -> new BasicInfoGrid.Area(radix, radix))
                      .setup(addCells(board, hiliteRelayPool, setup, radix * radix))
                      .build();
    }

    private static Consumer<BasicInfoGrid.Area> addCells(final Board board, final HiliteRelayPool pool, final Setup setup, final int zMax) {
        return area -> IntStream.range(0, zMax)
                                .mapToObj(z -> cell(z, board, pool, setup))
                                .forEach(area::add);
    }

    private static InfoCell cell(final int z, final Board board, final HiliteRelayPool hiliteRelayPool, final Setup setup) {
        return new InfoCell(board.getAreaGrp(z).getPotential(), hiliteRelayPool.getAreaRelay(z), setup);
    }
}
