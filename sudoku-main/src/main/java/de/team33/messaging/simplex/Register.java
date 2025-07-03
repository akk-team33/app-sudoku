package de.team33.messaging.simplex;

import de.team33.messaging.Listener;

public interface Register<MSG> {
    void add(Listener<MSG> var1);

    void remove(Listener<MSG> var1);
}
