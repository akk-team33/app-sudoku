package de.team33.messaging.simplex;

public interface Relay<MSG> extends Register<MSG> {
    void route(MSG var1);
}
