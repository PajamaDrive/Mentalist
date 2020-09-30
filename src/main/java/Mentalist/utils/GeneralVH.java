package Mentalist.utils;

import java.util.LinkedList;
import javax.websocket.Session;

public abstract class GeneralVH extends GeneralNegotiator {
    int agentID = 1;

    protected boolean safeForMultiAgent = false;

    boolean AgentVsAgent = false;

    public GeneralVH(String username, GameSpec game, Session session) {
        super(username, game, session);
    }

    public abstract LinkedList<Event> getEventResponse(Event paramEvent);

    public abstract String getArtName();

    public abstract String agentDescription();

    public int getID() {
        return this.agentID;
    }

    public boolean safeForMultiAgent() {
        return this.safeForMultiAgent;
    }

    public void setAgentVsAgent(String choice) {
        if (choice.equals("agent"))
            this.AgentVsAgent = true;
    }

    public boolean getAgentVsAgent() {
        return this.AgentVsAgent;
    }
}