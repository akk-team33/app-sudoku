package de.team33.sudoku;

import de.team33.messaging.Listener;
import de.team33.messaging.simplex.Relay;
import de.team33.messaging.simplex.Router;

public class HiliteRelayPool {
    private GROUP_ROUTER[] router = new GROUP_ROUTER[3 * Numbers.getCount()];

    public HiliteRelayPool() {
    }

    public Relay<HiliteMessage> getColRelay(int x) {
        return this.getGrpRelay(0, x);
    }

    public Relay<HiliteMessage> getRowRelay(int y) {
        return this.getGrpRelay(1, y);
    }

    public Relay<HiliteMessage> getAreaRelay(int z) {
        return this.getGrpRelay(2, z);
    }

    public Relay<HiliteMessage> getCellRelay(int x, int y) {
        int rdx = Numbers.getRadix();
        return new CELL_ROUTER(x, y, y / rdx * rdx + x / rdx);
    }

    private GROUP_ROUTER getGrpRelay(int i, int k) {
        int n = i * Numbers.getCount() + k;
        if (this.router[n] == null) {
            this.router[n] = new GROUP_ROUTER();
        }

        return this.router[n];
    }

    private class CELL_ROUTER implements Relay<HiliteMessage> {
        private int m_x;
        private int m_y;
        private int m_z;

        public CELL_ROUTER(int x, int y, int z) {
            this.m_x = x;
            this.m_y = y;
            this.m_z = z;
        }

        public void add(Listener<HiliteMessage> l) {
            HiliteRelayPool.this.getColRelay(this.m_x).add(l);
            HiliteRelayPool.this.getRowRelay(this.m_y).add(l);
            HiliteRelayPool.this.getAreaRelay(this.m_z).add(l);
        }

        public void remove(Listener<HiliteMessage> l) {
            HiliteRelayPool.this.getColRelay(this.m_x).remove(l);
            HiliteRelayPool.this.getRowRelay(this.m_y).remove(l);
            HiliteRelayPool.this.getAreaRelay(this.m_z).remove(l);
        }

        public void route(HiliteMessage message) {
            HiliteRelayPool.this.getColRelay(this.m_x).route(message);
            HiliteRelayPool.this.getRowRelay(this.m_y).route(message);
            HiliteRelayPool.this.getAreaRelay(this.m_z).route(message);
        }
    }

    private static class GROUP_ROUTER extends Router<HiliteMessage> {
        private GROUP_ROUTER() {
        }
    }
}
