package Mentalist.utils;

import javax.websocket.Session;

public class GeneralNegotiator {
    private String name;

    private Session session;

    private GeneralNegotiator adversary;

    protected GameSpec game;

    private History history = new History();

    public GeneralNegotiator(String username, GameSpec game, Session session) {
        this.name = username;
        this.game = game;
        this.session = session;
    }

    public String getName() {
        return this.name;
    }

    public GeneralNegotiator getAdversary() {
        return this.adversary;
    }

    protected void setAdversary(GeneralNegotiator negotiator) {
        this.adversary = negotiator;
    }

    protected void pairWith(GeneralNegotiator negotiator) {
        this.adversary = negotiator;
        negotiator.setAdversary(this);
    }

    public Session getSession() {
        return this.session;
    }

    public History getHistory() {
        return this.history;
    }

    protected void setHistory(History history) {
        this.history = history;
    }

    public void setGameSpec(GameSpec spec) {
        this.game = spec;
    }
}