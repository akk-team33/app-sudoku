package de.team33.messaging.simplex;

import de.team33.messaging.Consumer;

public interface Register<MSG> {

    void add(Consumer<MSG> var1);

    void remove(Consumer<MSG> var1);
}
