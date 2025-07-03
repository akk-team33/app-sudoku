package de.team33.messaging.simplex;

import de.team33.messaging.Listener;

import java.util.HashSet;
import java.util.Iterator;

public class Router<MSG> implements Relay<MSG> {
    private Router<MSG>.REGISTRY registry = new REGISTRY();
    private MSG initial;

    public Router() {
    }

    public void setInitial(MSG initial) {
        this.initial = initial;
    }

    public void add(Listener<MSG> listener) {
        synchronized(this.registry) {
            if (this.registry.add(listener) && this.initial != null) {
                listener.pass(this.initial);
            }

        }
    }

    public void remove(Listener<MSG> lstnr) {
        synchronized(this.registry) {
            this.registry.remove(lstnr);
        }
    }

    public void route(MSG message) {
        HashSet listeners;
        synchronized(this.registry) {
            listeners = new HashSet(this.registry);
        }

        Iterator var4 = listeners.iterator();

        while(var4.hasNext()) {
            Listener<MSG> listener = (Listener)var4.next();
            listener.pass(message);
        }

    }

    private class REGISTRY extends HashSet<Listener<MSG>> {
        private REGISTRY() {
        }
    }
}
