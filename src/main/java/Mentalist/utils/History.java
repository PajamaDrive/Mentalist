package Mentalist.utils;

import java.util.LinkedList;

public class History {
    private LinkedList<Event> opponentHistory = new LinkedList<>();

    private LinkedList<Event> userHistory = new LinkedList<>();

    private LinkedList<Event> systemHistory = new LinkedList<>();

    private LinkedList<Event> totalHistory = new LinkedList<>();

    private LinkedList<GeneralNegotiator> attachedNegotiators = new LinkedList<>();

    public static final int SYSTEM_ID = -1;

    public static final int USER_ID = 0;

    public static final int OPPONENT_ID = 1;

    public void addNegotiator(GeneralNegotiator negotiator) {
        this.attachedNegotiators.add(negotiator);
    }

    public LinkedList<Event> getOpponentHistory() {
        return this.opponentHistory;
    }

    public LinkedList<Event> getUserHistory() {
        return this.userHistory;
    }

    public LinkedList<Event> getOpponentHistory(int agentID) {
        if (agentID == 1)
            return getUserHistory();
        if (agentID == 0)
            return getOpponentHistory();
        return null;
    }

    public LinkedList<Event> getHistory() {
        return this.totalHistory;
    }

    protected void updateHistory(Event e) {
        if ((e.getType() == Event.EventClass.SEND_MESSAGE && e.getMessage() != null) || e.getType() != Event.EventClass.SEND_MESSAGE) {
            if (e.getOwner() == 1) {
                this.opponentHistory.add(e);
                this.totalHistory.add(e);
            } else if (e.getOwner() == 0) {
                this.userHistory.add(e);
                this.totalHistory.add(e);
            } else if (e.getOwner() == -1) {
                this.systemHistory.add(e);
                this.totalHistory.add(e);
            } else {
                throw new IllegalArgumentException("ERROR: History update attempt with invalid target ID");
            }
            for (GeneralNegotiator user : this.attachedNegotiators)
                user.setHistory(this);
        }
    }

    public String toString() {
        String ans = "";
        ans = ans + "UserHistory:\n\n";
        for (Event e : this.userHistory) {
            if (!e.getType().equals(Event.EventClass.OFFER_IN_PROGRESS))
                ans += e.getType().toString() +  ": ";
            if (e.getType().equals(Event.EventClass.SEND_MESSAGE))
                ans += e.getMessage() + "\n";
            if (e.getType().equals(Event.EventClass.SEND_OFFER))
                ans += e.getOffer() + "\n";
            if (e.getType().equals(Event.EventClass.SEND_EXPRESSION))
                ans += e.getMessage() + "\n";
        }
        ans = ans + "\n\nOpponentHistory:\n\n";
        for (Event e : this.opponentHistory) {
            if (!e.getType().equals(Event.EventClass.OFFER_IN_PROGRESS))
                ans += e.getType().toString() +  ": ";
            if (e.getType().equals(Event.EventClass.SEND_MESSAGE))
                ans += e.getMessage() + "\n";
            if (e.getType().equals(Event.EventClass.SEND_OFFER))
                ans += e.getOffer() + "\n";
            if (e.getType().equals(Event.EventClass.SEND_EXPRESSION))
                ans += e.getMessage() + "\n";
        }
        ans = ans + "\n\nCombined Log:\n\n";
        for (Event e : this.totalHistory) {
            if (!e.getType().equals(Event.EventClass.OFFER_IN_PROGRESS))
                ans += e.getOwner() == 0 ? "You --> " + e.getType().toString() + ": " : "Sam --> " + e.getType().toString() + ": " ;
            if (e.getType().equals(Event.EventClass.SEND_MESSAGE))
                ans += e.getMessage() + "\n";
            if (e.getType().equals(Event.EventClass.SEND_OFFER))
                ans += e.getOffer() + "\n";
            if (e.getType().equals(Event.EventClass.SEND_EXPRESSION))
                ans += e.getMessage() + "\n";
        }
        return ans;
    }
}