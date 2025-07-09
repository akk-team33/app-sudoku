package de.team33.sudoku;

import de.team33.messaging.simplex.Relay;
import de.team33.messaging.simplex.Router;

import java.util.function.Consumer;

public class HiliteRelayPool {
    private final GROUP_ROUTER[] router = new GROUP_ROUTER[3 * Numbers.getCount()];

    public final Relay<HiliteMessage> getColRelay(final int x) {
        return getGrpRelay(0, x);
    }

    public final Relay<HiliteMessage> getRowRelay(final int y) {
        return getGrpRelay(1, y);
    }

    public final Relay<HiliteMessage> getAreaRelay(final int z) {
        return getGrpRelay(2, z);
    }

    public final Relay<HiliteMessage> getCellRelay(final int x, final int y) {
        final int rdx = Numbers.getRadix();
        return new CELL_ROUTER(x, y, y / rdx * rdx + x / rdx);
    }

    private GROUP_ROUTER getGrpRelay(final int i, final int k) {
        final int n = i * Numbers.getCount() + k;
        if (router[n] == null) {
            router[n] = new GROUP_ROUTER();
        }

        return router[n];
    }

    private class CELL_ROUTER implements Relay<HiliteMessage> {
        private final int m_x;
        private final int m_y;
        private final int m_z;

        public CELL_ROUTER(final int x, final int y, final int z) {
            this.m_x = x;
            this.m_y = y;
            this.m_z = z;
        }

        @Override
        public final void add(final Consumer<HiliteMessage> l) {
            getColRelay(m_x).add(l);
            getRowRelay(m_y).add(l);
            getAreaRelay(m_z).add(l);
        }

        @Override
        public final void remove(final Consumer<HiliteMessage> l) {
            getColRelay(m_x).remove(l);
            getRowRelay(m_y).remove(l);
            getAreaRelay(m_z).remove(l);
        }

        @Override
        public final void route(final HiliteMessage message) {
            getColRelay(m_x).route(message);
            getRowRelay(m_y).route(message);
            getAreaRelay(m_z).route(message);
        }
    }

    private static class GROUP_ROUTER extends Router<HiliteMessage> {
    }
}
