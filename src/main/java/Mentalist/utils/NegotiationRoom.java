package Mentalist.utils;

import com.google.gson.Gson;
import javax.servlet.http.HttpSession;
import javax.websocket.Session;

public class NegotiationRoom {
    UserSession user1;

    UserSession user2;

    GameBridgeUtils gbu1;

    GameBridgeUtils gbu2;

    public class UserSession {
        HttpSession httpSession;

        Session session;

        UserSession(HttpSession httpSession, Session session) {
            this.httpSession = httpSession;
            this.session = session;
        }

        public HttpSession getHttpSession() {
            return this.httpSession;
        }

        public Session getWsSession() {
            return this.session;
        }

        public String toString() {
            return "" + this.httpSession + " " + this.httpSession;
        }
    }

    public void transmitMsg(Session session, String msg) {
        System.out.println(msg);
        if (session != this.user1.session) {
            WebSocketUtils.send((new Gson()).toJson(new WebSocketUtils(). new JsonObject( "MSG", msg)), this.user1.session);
        } else if (session != this.user2.session) {
            WebSocketUtils.send((new Gson()).toJson(new WebSocketUtils(). new JsonObject( "MSG", msg)), this.user2.session);
        }
    }

    public void setGBU(HttpSession httpSession, GameBridgeUtils gbu) {
        if (this.user1.httpSession == httpSession)
            this.gbu1 = gbu;
        if (this.user2.httpSession == httpSession)
            this.gbu2 = gbu;
    }

    public void registerUser(HttpSession httpSession, Session session) {
        if (this.user1 == null) {
            this.user1 = new UserSession(httpSession, session);
        } else if (this.user2 == null) {
            this.user2 = new UserSession(httpSession, session);
        }
    }

    public UserSession getAdversary(HttpSession httpSession, Session session) {
        if (this.user1.httpSession == httpSession && this.user1.session == session)
            return this.user2;
        if (this.user2.httpSession == httpSession && this.user2.session == session)
            return this.user1;
        return null;
    }

    public UserSession getAdversary(HttpSession httpSession) {
        if (this.user1.httpSession == httpSession)
            return this.user2;
        if (this.user2.httpSession == httpSession)
            return this.user1;
        return null;
    }

    public void exitInWaiting(HttpSession httpSession, Session session) {
        if (this.user1.httpSession == httpSession && this.user1.session == session) {
            this.user1 = null;
        } else if (this.user2.httpSession == httpSession && this.user2.session == session) {
            this.user2 = null;
        }
    }

    public void exitInNegotiation(HttpSession httpSession) {
        UserSession remainUser = (httpSession == this.user1.httpSession) ? this.user2 : this.user1;
        WebSocketUtils.send((new Gson()).toJson(new WebSocketUtils().new JsonObject( "END", "The other user exited")), remainUser.session);
    }

    public boolean available() {
        return (this.user1 == null || this.user2 == null);
    }

    public boolean findUserByHttpSession(HttpSession httpSession) {
        return (this.user1.httpSession == httpSession || this.user2.httpSession == httpSession);
    }

    void finalNotification(HttpSession httpSession) {
        if (httpSession == this.gbu1.getHttpSession()) {
            this.gbu2.sendFinalNotification();
        } else if (httpSession == this.gbu2.getHttpSession()) {
            this.gbu1.sendFinalNotification();
        }
    }

    void handleClose(HttpSession httpSession) {
        if (httpSession == this.gbu1.getHttpSession()) {
            this.gbu2.handleClose();
        } else if (httpSession == this.gbu2.getHttpSession()) {
            this.gbu1.handleClose();
        }
    }

    public void sendToOpponent(HttpSession httpSession, Event e) {
        Event newEvent = preProcessEvent(e);
        if (httpSession == this.gbu1.getHttpSession()) {
            this.gbu2.processEvent(newEvent, this.user2.getWsSession());
        } else if (httpSession == this.gbu2.getHttpSession()) {
            this.gbu1.processEvent(newEvent, this.user1.getWsSession());
        }
    }

    Event preProcessEvent(Event e) {
        Event event = null;
        int owner = -2;
        if (e.getOwner() == -1) {
            ServletUtils.log("Send System events to opponent", ServletUtils.DebugLevels.DEBUG);
            return event;
        }
        owner = e.getOwner() ^ 0x1;
        if (e.getType().equals(Event.EventClass.SEND_OFFER)) {
            Offer o = e.getOffer();
            Offer newOffer = new Offer(o.getIssueCount());
            for (int i = 0; i < o.getIssueCount(); i++) {
                int[] item = o.getItem(i);
                int[] flipItem = new int[3];
                flipItem[0] = item[2];
                flipItem[1] = item[1];
                flipItem[2] = item[0];
                newOffer.setItem(i, flipItem);
            }
            event = new Event(owner, e.getType(), newOffer, 0);
        } else if (e.getType().equals(Event.EventClass.SEND_MESSAGE)) {
            event = new Event(owner, e.getType(), e.getMessage(), 0);
            event.encodePreferenceData(e.getPreference());
        } else if (e.getType().equals(Event.EventClass.SEND_EXPRESSION)) {
            event = new Event(owner, e.getType(), e.getMessage(), 0, 0);
        } else if (e.getType().equals(Event.EventClass.FORMAL_ACCEPT) || e.getType().equals(Event.EventClass.FORMAL_QUIT) || e
                .getType().equals(Event.EventClass.OFFER_IN_PROGRESS)) {
            event = new Event(owner, e.getType(), 0);
        } else {
            ServletUtils.log("We shouldn't process those events", ServletUtils.DebugLevels.DEBUG);
        }
        return event;
    }

    public void updateSocket(HttpSession httpSession, Session session) {
        if (this.user1.httpSession == httpSession) {
            this.user1.session = session;
        } else if (this.user2.httpSession == httpSession) {
            this.user2.session = session;
        }
        if (this.gbu1 != null)
            this.gbu1.setSessionOther(this.user2.httpSession, this.user2.session);
        if (this.gbu2 != null)
            this.gbu2.setSessionOther(this.user1.httpSession, this.user1.session);
    }

    public void doSomething() {
        if (this.user1 == null || this.user2 == null)
            return;
    }

    public String getUserString() {
        String res = "USER1: ";
        if (this.user1 != null)
            res = res + res + " ";
        res = res + "USER2: ";
        if (this.user2 != null)
            res = res + res;
        return res;
    }
}