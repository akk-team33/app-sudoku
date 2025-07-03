package de.team33.messaging.simplex;

public class Sender<MSG> implements Originator<MSG> {
    private Router<MSG> router = new Router<>();

    public Sender() {
    }

    public Register<MSG> getRegister() {
        return this.router;
    }

    protected void setInitial(MSG initial) {
        this.router.setInitial(initial);
    }

    protected void fire(MSG message) {
        this.router.route(message);
    }
}
