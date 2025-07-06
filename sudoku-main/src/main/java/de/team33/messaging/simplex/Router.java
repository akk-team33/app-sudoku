package de.team33.messaging.simplex;

import de.team33.messaging.Consumer;

import java.util.HashSet;
import java.util.Iterator;

public class Router<M> implements Relay<M> {
    private final Router<M>.REGISTRY registry = new REGISTRY();
    private M initial;

    @SuppressWarnings("WeakerAccess")
    public final void setInitial(final M initial) {
        this.initial = initial;
    }

    public final void add(final Consumer<M> listener) {
        synchronized(registry) {
            if (registry.add(listener) && null != initial) {
                listener.accept(initial);
            }
        }
    }

    public final void remove(final Consumer<M> lstnr) {
        synchronized(registry) {
            registry.remove(lstnr);
        }
    }

    public final void route(final M message) {
        final HashSet listeners;
        synchronized(registry) {
            listeners = new HashSet(registry);
        }

        final Iterator var4 = listeners.iterator();

        while(var4.hasNext()) {
            final Consumer<M> listener = (Consumer)var4.next();
            listener.accept(message);
        }

    }

    private class REGISTRY extends HashSet<Consumer<M>> {
    }
}
