package Mentalist.utils;

import java.util.concurrent.LinkedBlockingQueue;

public class Mediator {
    private LinkedBlockingQueue<Event> sendToAgent = new LinkedBlockingQueue<>();

    private boolean pageOpen;

    private boolean activePage = true;

    boolean gameInProgress = false;

    GameSpec spec;

    GameBridgeUtils utils;

    Mediator(GameBridgeUtils u) {
        this.utils = u;
    }

    void setGameSpec(GameSpec newSpec) {
        this.spec = newSpec;
    }

    synchronized void addToAgent(Event e) {
        setActivePage(true);
        if (!this.gameInProgress && e.getType() == Event.EventClass.GAME_START) {
            this.gameInProgress = true;
        } else if (this.gameInProgress && e.getType() == Event.EventClass.GAME_START) {
            ServletUtils.log("Uh oh, GAME_START overflow!", ServletUtils.DebugLevels.WARN);
            return;
        }
        if (this.gameInProgress) {
            try {
                boolean timedOut = (e.getType() == Event.EventClass.TIME && Integer.parseInt(e.getMessage()) >= this.spec.getTotalTime());
                boolean agreed = (this.utils.getAstate() == GameBridgeUtils.AcceptanceState.FULL_ACCEPTANCE);
                boolean quit = (this.utils.getQstate() == GameBridgeUtils.QuitState.FULL_QUIT);
                if (timedOut || agreed || quit) {
                    this.sendToAgent.clear();
                    this.gameInProgress = false;
                }
                if (e.isPriority())
                    flush(this.sendToAgent);
                this.sendToAgent.put(e);
            } catch (InterruptedException e1) {
                e1.printStackTrace();
            }
            if (this.sendToAgent.size() == 1)
                notify();
        }
    }

    synchronized Event takeForAgent() {
        Event e = null;
        try {
            e = this.sendToAgent.take();
        } catch (InterruptedException e1) {
            e1.printStackTrace();
        }
        return e;
    }

    synchronized void clearAgentQueue() {
        this.sendToAgent.clear();
        this.gameInProgress = false;
    }

    public boolean pendingAgentEvent() {
        return !this.sendToAgent.isEmpty();
    }

    void makeRunnable() {
        this.pageOpen = true;
    }

    void stopRunning() {
        this.pageOpen = false;
    }

    boolean isPageOpen() {
        return this.pageOpen;
    }

    boolean isActivePage() {
        return this.activePage;
    }

    void setActivePage(boolean activePage) {
        this.activePage = activePage;
    }

    private LinkedBlockingQueue<Event> flush(LinkedBlockingQueue<Event> queue) {
        LinkedBlockingQueue<Event> flushed = new LinkedBlockingQueue<>();
        while (!queue.isEmpty()) {
            try {
                Event e = queue.take();
                if (!e.isFlushable())
                    flushed.put(e);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return flushed;
    }
}