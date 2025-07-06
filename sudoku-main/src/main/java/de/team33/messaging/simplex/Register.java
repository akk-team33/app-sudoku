package de.team33.messaging.simplex;

import java.util.function.Consumer;

public interface Register<MSG> {

    void add(Consumer<MSG> var1);

    void remove(Consumer<MSG> var1);
}
