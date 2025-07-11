package de.team33.sudoku.ui;

import de.team33.sphinx.alpha.visual.JPanels;
import de.team33.sudoku.Board;
import de.team33.sudoku.HiliteRelayPool;
import de.team33.sudoku.Setup;

import javax.swing.*;
import java.util.function.Consumer;
import java.util.stream.IntStream;

final class RowInfoGrid {

    private RowInfoGrid() {
    }

    static JPanel panel(final Board board, final HiliteRelayPool pool, final Setup setup) {
        return JPanels.builder(() -> new BasicInfoGrid(board.radix(), 1))
                      .setup(addAreas(board, pool, setup, board.radix()))
                      .build();
    }

    private static Consumer<JPanel> addAreas(final Board board, final HiliteRelayPool pool, final Setup setup, final int radix) {
        return panel -> {
            IntStream.range(0, radix)
                     .mapToObj(y -> area(board, y, radix, pool, setup))
                     .forEach(panel::add);
        };
    }

    private static BasicInfoGrid.Area area(final Board board, final int y0, final int radix, final HiliteRelayPool pool, final Setup setup) {
        return JPanels.builder(() -> new BasicInfoGrid.Area(radix, 1))
                      .setup(addCells(board, y0, radix, pool, setup))
                      .build();
    }

    private static Consumer<BasicInfoGrid.Area> addCells(final Board board, final int y0, final int radix, final HiliteRelayPool pool, final Setup setup) {
        return area -> IntStream.range(0, radix)
                                .mapToObj(dy -> cell(board, pool, setup, radix * y0 + dy))
                                .forEach(area::add);
    }

    private static InfoCell cell(final Board board, final HiliteRelayPool pool, final Setup setup, final int y) {
        return new InfoCell(board.getRowGrp(y).getPotential(), pool.getRowRelay(y), setup);
    }
}
