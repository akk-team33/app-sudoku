package de.team33.messaging.simplex;

@FunctionalInterface
public interface Originator<MSG> {
    Register<MSG> getRegister();
}
