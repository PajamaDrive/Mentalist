package Mentalist.utils;

import java.util.Collections;
import java.util.LinkedList;
import javax.websocket.Session;

public class VHRunner implements Runnable {
    private final Mediator MEDIATOR;

    private GeneralVH vh;

    private GameBridgeUtils utils;

    private Session wsSession;

    int agentID;

    LinkedList<Event> eventList = new LinkedList<>();

    public VHRunner(int id, Mediator m, GeneralVH VH, GameBridgeUtils u) {
        this.agentID = id;
        this.vh = VH;
        this.MEDIATOR = m;
        this.utils = u;
    }

    void setSession(Session s) {
        this.wsSession = s;
    }

    void setVH(GeneralVH agent) {
        this.vh = agent;
        this.vh.agentID = this.agentID;
    }

    public void run() {
        try {
            while (this.MEDIATOR.isPageOpen()) {
                if (this.eventList.isEmpty() && !this.MEDIATOR.pendingAgentEvent()) {
                    synchronized (this.MEDIATOR) {
                        this.MEDIATOR.wait(12000L);
                        if (!this.MEDIATOR.isActivePage())
                            this.MEDIATOR.stopRunning();
                        this.MEDIATOR.setActivePage(false);
                    }
                    continue;
                }
                Event e = null;
                synchronized (this.MEDIATOR) {
                    if (this.MEDIATOR.pendingAgentEvent())
                        e = this.MEDIATOR.takeForAgent();
                }
                if (e != null) {
                    boolean timedOut = (e.getType() == Event.EventClass.TIME && Integer.parseInt(e.getMessage()) >= this.MEDIATOR.spec.getTotalTime());
                    boolean agreed = (this.utils.getAstate() == GameBridgeUtils.AcceptanceState.FULL_ACCEPTANCE);
                    boolean quit = (this.utils.getQstate() == GameBridgeUtils.QuitState.FULL_QUIT);
                    if (timedOut || agreed || quit) {
                        this.eventList.clear();
                    } else {
                        LinkedList<Event> newResponses = this.vh.getEventResponse(e);
                        newResponses.removeAll(Collections.singleton(null));
                        for (int i = 0; i < newResponses.size(); i++) {
                            if (((Event)newResponses.get(i)).isPriority()) {
                                flush(this.eventList);
                                break;
                            }
                        }
                        this.eventList.addAll(newResponses);
                    }
                }
                emptyQueue(this.eventList, this.wsSession);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        ServletUtils.log("Thread " + this.agentID + " has ended.", ServletUtils.DebugLevels.DEBUG);
    }

    private void emptyQueue(LinkedList<Event> q, Session session) throws InterruptedException {
        if (q != null && !q.isEmpty()) {
            Event e = q.pop();
            Thread.sleep(e.getDelay());
            if (this.MEDIATOR.gameInProgress) {
                if (this.utils.getNMode() != GameBridgeUtils.NegotiationMode.HUMAN_HUMAN)
                    e.setOwner(this.agentID);
                this.utils.processEvent(e, session);
                Thread.sleep(1100L);
            } else {
                q.clear();
            }
        }
    }

    private void flush(LinkedList<Event> eventList) {
        int i = 0;
        while (i < eventList.size()) {
            if (((Event)eventList.get(i)).isFlushable()) {
                eventList.remove(i);
                continue;
            }
            i++;
        }
    }
}