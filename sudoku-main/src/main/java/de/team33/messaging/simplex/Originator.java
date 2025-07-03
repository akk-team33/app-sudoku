package de.team33.messaging.simplex;

public interface Originator<MSG> {
    Register<MSG> getRegister();
}
