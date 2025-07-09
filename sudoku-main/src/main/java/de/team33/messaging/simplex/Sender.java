package de.team33.messaging.simplex;

public class Sender<MSG> implements Originator<MSG> {
    private final Router<MSG> router = new Router<>();

    @Override
    public final Register<MSG> getRegister() {
        return router;
    }

    protected final void setInitial(final MSG initial) {
        router.setInitial(initial);
    }

    protected final void fire(final MSG message) {
        router.route(message);
    }
}
